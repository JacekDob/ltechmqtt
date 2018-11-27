#!/usr/bin/env node


// listening and sending from port, if 0 assigned automatically
var SPORT = 0;
var SHOST = '0.0.0.0';

var CPORT = 5987;
var CHOST = ''; // received during search;
var CBROADCAST = '255.255.255.255';


var dgram = require('dgram');
var mqtt = require('mqtt');

var cfg = require('./cfg.js');

var deviceMacAddress = cfg.deviceMacAddress;
var mqttServer = cfg.mqttServer;

var lastCommand = new Date();

var sessionTimeout = 30000;

var queue = [];


var publishOptions = {
        qos: 0,
        retain: true
};

var publishOptionsLow = {
        qos: 0,
        retain: false
};

// MQTT topic /ltech/deviceMacAddress/command
// MQTT topic /ltech/deviceMacAddress/state
var topic = 'ltech';

var server = dgram.createSocket({type: 'udp4', reuseAddr: true});

server.on('listening', function () {
    var address = server.address();

    server.setBroadcast(true);

    logV('UDP Server listening on ' + address.address + ":" + address.port);

    phase1();
});

var pwd12 = '00';
var pwd34 = '00';

var sessionId;

var sessionCounter = 0;

var commandCounter = 0;

logI("Handling device: " + deviceMacAddress);

server.on('message', function (message, remote) {
    logD(remote.address + ':' + remote.port +' - ' + Buffer.from(message).toString('hex') + ", " + message.length + " bytes");

    if (message[0] == 0x18) {
    	// pahse1: device found: 
	// REQUEST:  13000000240397ea023136306661326166313934386638303031363066613261663139343866383031
	// RESPONSE: 180000003a01f0fe6b17b6a0002031363066613261663139343866383030313630666132616631393438663830310100011763050009786c696e6b5f646576
    	//		         (    MAC    )									           ^^
	if (message[52] == 0x05) {
            if (message.length == 63) {
	        logV('VALID: phase1 response');
		CHOST = remote.address;
		logD('Device IP=' + CHOST)
		phase2();
            } else {
	        logW('INVALID: phase1 response');
            }
    	// phase2:: 
	// REQUEST: 930000002598870097eafa4c9b5fe88f559834b67200aee471e7fa4c9b5fe88f559834b67200aee471e7
    	//				 (    MAC    )									       ^^	
	} else if (message[52] = 0x15) {
            if (message.length == 63) {
	        logV('VALID: phase2 response');
		CHOST = remote.address;
		logD('Device IP=' + CHOST)
		phase3();
            } else {
	        logW('INVALID: phase2 response');
            }	
	}
    }


    //REQUEST:  930000002598870097eafa4c9b5fe88f559834b67200aee471e7fa4c9b5fe88f559834b67200aee471e7
    //RESPOSNE: 9b00000003988700
    if (message[0] == 0x9b) {
        if (message.length == 8) {
            logV('VALID: phase3 response');
                phase4();
        } else {
            logW('INVALID: phase3 response');
        }
    }
    // session registered
    if (message[0] == 0x28) {
	sessionId = Buffer.from(message).readUInt16BE(19);
	logV('sessionId: ' + hex(sessionId, 2, 'BE'));
    }


    //keep alive response: d800000007f0fe6b17b6a001
    //                               (   MAC    )
    if (message[0] == 0xd8 ) { //
	if (message.length == 12 && Buffer.from(message).toString('hex').substring(10, 10 + 12) == deviceMacAddress) {
	    logV("VALID: Keep alive response for " + Buffer.from(message).toString('hex').substring(10, 10 + 12));
        } else {
	    logW("INVALID: Keep alive response");
        }
    }

    // command response
    if (message[0] == 0x8b) {
	if (message.length == 8) {
	    if (Buffer.from(message).readUInt16BE(5) == sessionCounter) {
			if (message[7] == 0x00) { // if last byte is 0x00 then command is successfull, if 0x01 then unsuccessfull (i.e. session invalid)
			        logV("VALID: Command response");
				queue.splice(0, 1);
				processQueue();
			} else {
			        logV("INVALID: Command response, session invalid.");
			        logI("Restaring session.");
				phase1();
			}
	    } else {
		var deviceCounter = Buffer.from(message).readUInt16LE(5);
	        logV("UNKNOWN: Command response, device counter: 0x" + hex(deviceCounter, 2, 'LE'));
	    }
	} else {
	    logV("INVALID: Command response, length not equals 8 bytes");
	}
    }

    // getInfo response: 8000000031f0fe6b17b6a005020066bb01c0000000d1154f67726f647a656e6965204856204c454420524742d401000100000000abeb
    //                             (   MAC    )
    if (message[0] == 0x80) {
	if (message.length == 54) {
		if (message[16] == 0xc1) {
		        logV("VALID: Command getInfo response");
			logD("message[16] = " + hex(message[16], 1));
			var onoff = message[44];
			logD('GetInfo onoff: 0x' + hex(onoff, 1));
			var status = onoff == 0xd4 ? false : true;
			var statusState = status ? 'ON' : 'OFF';
			logV('State: ' + statusState);
		    clientMQTT.publish('/' + topic + '/' + deviceMacAddress + '/state/status', statusState, publishOptions);
		}
        } else {
	        logV("INVALID: Command getInfo response");
        }
    }
});

server.bind(SPORT, SHOST);

var client = server;

function phase1() {
	logV("phase1: search");
	var message = new Buffer('13000000240397ea023136306661326166313934386638303031363066613261663139343866383031', 'hex');
	sendUdp(message, CBROADCAST, CPORT);
}

function phase2() {
	logV("phase2: register?");
        var message = new Buffer('130000000a0397ea11'+deviceMacAddress, 'hex');
	sendUdp(message, CBROADCAST, CPORT);
}

function phase3() {
	logV("phase3: register2?");
	var message = new Buffer('930000002598870097eafa4c9b5fe88f559834b67200aee471e7fa4c9b5fe88f559834b67200aee471e7', 'hex');
	sendUdp(message, CBROADCAST, CPORT);
}

var keepAliveInterval;

function phase4() {
	logV("phase4: start session");
	var message = new Buffer('230000001601fa4c9b5fe88f559834b67200aee471e797ea000064', 'hex');
	sendUdp(message, CBROADCAST, CPORT);
	lastCommand = new Date();
	setTimeout(getInfo, 500);
	setTimeout(processQueue, 3000);
	if (keepAliveInterval)
		clearInterval(keepAliveInterval);
	keepAliveInterval = setInterval(keepAlive, sessionTimeout);
}

function keepAlive() {
	logV("keepAlive");
	// app does keepAlive, but this does not return current state
	/*
	nextSessionCounter();
	var message = new Buffer('d300000002'+hex(sessionId,2, 'BE'), 'hex');
	sendUdp(message, CHOST, CPORT);
	*/
	getInfo();
}

function sendUdp(message, host, port) {
        //logD("Sending:    " + message.toString('hex') + ", " + message.length + " bytes");
        client.send(message, 0, message.length, port, host, function(err, bytes) {
            if (err) throw err;
            logD('UDP message ' + message.toString('hex') + ', ' + bytes +' bytes sent to ' + host +':'+ port);
        });
}

function nextSessionCounter() {
	sessionCounter++;
	if (sessionCounter >= 65536)
	    sessionCounter = 0;
	logD('sessionCounter: ' + sessionCounter + ' = 0x' + hex(sessionCounter,2,'BE'));
}

function nextCommandCounter() {
	commandCounter++;
	if (commandCounter >= 256)
	    commandCounter = 0;
	commandCounterHex = Buffer.from([commandCounter]).toString('hex');
	logD('commandCounter: ' + commandCounter + ' = 0x' + commandCounterHex);
}

var cmdGetInfo = '21';
var cmdTurn = '15';

var valTurnOn = 'A1';
var valTurnOff = 'A0';

function doBashCommand() {
	if (process.argv[2] == 'on') {
		turnOn();
	} else if (process.argv[2] == 'off') {
		turnOff();
	}
	setTimeout(process.exit, 1000);
}

function getInfo() {
	logI('GetInfo');
	sendCommand('c0', cmdGetInfo, '00');
}

function turnOn() {
	logI('TurnOn');
	sendCommand('c1', cmdTurn, valTurnOn);
	getInfo();
}

function turnOff() {
	logI('TurnOff');
	sendCommand('c1', cmdTurn, valTurnOff);
	getInfo();
}

function turnFullWhite() {
	logI('TurnFullWhite');
	setColor(210, 255, 255, 255, 255, 255);
}

function turnBright() {
	logI('TurnBright');
	setColor(210, 255, 255, 64, 255, 255);
}

function turnStandby() {
	logI('TurnStandby');
	var val = Buffer('20b1000115161616161616161200000000000000', 'hex');
	logD(val);
	sendCommand('c0', '12', val.toString('hex'));

}

function setColor(type, brightness, r, g, b, gray) {
	logI('SetColor');
	// type:
	// colorBrightnessType = 209
	// colorRGBType = 210
	// colorGreyType = 211
	// { 102, -69, (cmdCount), -63, pwd12, pwd34, 17, (6), (type), (brightness), (r), (g), (b), (gray), -21};
	var val = Buffer.from([ type, brightness, r, g, b, gray]);
	logD(val);
	sendCommand('c1', '11', val.toString('hex'));
}

function processQueue() {
	logV('Processing queue: ' + queue.length);
	if (queue.length > 0) {
		var cmd = queue[0];
		doSend(cmd.cmdPrefix, cmd.cmd, cmd.val);
		// command is removed from the queue when response is confirmed
	}
}

function sendCommand(cmdPrefix, cmd, val) {
	queue.push({ cmdPrefix: cmdPrefix, cmd: cmd, val: val });
	processQueue();
}

function doSend(cmdPrefix, cmd, val) {
	nextSessionCounter();
	nextCommandCounter();

	if (keepAliveInterval)
		clearInterval(keepAliveInterval);
        keepAliveInterval = setInterval(keepAlive, sessionTimeout);

	var len = Buffer.from([val.length / 2]).toString('hex');

	var sessionPreamble = '83000000';
	var cmdPreamble = '66bb';
	var cmdSuamble = 'eb';
	var sessionPayload = hex(sessionId,2,'BE')+hex(sessionCounter,2,'BE')+'00'+cmdPreamble+cmdPrefix+hex(commandCounter,1)+pwd12+pwd34+cmd+len+val+cmdSuamble;
	var sessionPayloadLength = sessionPayload.length / 2;

	// 8300 0000 0f99 0017 9100 66bb 01c0 0000 2101 00eb
	var message = sessionPreamble + Buffer.from([sessionPayloadLength]).toString('hex') + sessionPayload;
	var bufferMessage = new Buffer(message, 'hex');

	sendUdp(bufferMessage, CHOST, CPORT);
}

function hex(val, bytes, order) {
        var b = new Buffer(bytes);
	if (bytes == 1)
		b.writeUInt8(val, 0);
	if (order == 'LE') {
	    if (bytes == 2)
	        b.writeUInt16LE(val, 0);
	} else if (order == 'BE'){
	    if (bytes == 2)
	        b.writeUInt16BE(val, 0);
	}
        var s = b.toString('hex');
	//logD('Converting to hex: val=' + val + ' decimal, bytes=' + bytes + ', str=0x' + s);
	return s;
}

function log(level, message) {
        var date = new Date();
        date.setTime(date.getTime() - date.getTimezoneOffset() * 60 * 1000);

        var d = '[' + date.toISOString().replace(/T/, ' ').replace(/\..+/, '') + ']';
        var l = '[' + level + ']';
        console.log(d + ' ' + l + ' ' + message);
}

function logL(message) {
        log('LOG ', message);
}

function logI(message) {
        log('INFO', message);
}

function logV(message) {
        log('VERB', message);
}

function logD(message) {
        log('DEBU', message);
}

function logE(message) {
        log('ERRO', message);
}

function logW(message) {
        log('WARN', message);
}

var clientMQTT = mqtt.connect(mqttServer.address, {
                username: mqttServer.username,
                password: mqttServer.password,
                will: {
                        topic: '/' + topic + '/status',
                        payload: 'OFF',
                        qos: 0,
                        retain: true
                }
        });


clientMQTT.on('connect', function () {
        logV('MQTT connected');
	connectDate = new Date();
        clientMQTT.subscribe('/' + topic + '/#');
        clientMQTT.publish('/' + topic + '/status', 'ON', publishOptions);

        logV('MQTT subscribed and published status');
});

clientMQTT.on('message', function (topic, message) {
	logV(topic + ' ' + message);

	var now = new Date();
	if (now.getTime() - connectDate.getTime() < 500) {
		return;
	}

	var root = topic.split('/')[1];
	var device = topic.split('/')[2];
	var commandstatus = topic.split('/')[3];
	var action = topic.split('/')[4];


	logD('root: ' + root);
	logD('device: ' + device);
	logD('commandstatus: ' + commandstatus);
	logD('action: ' + action);
	logD('message: ' + message);

	if (!root || !device || !commandstatus || !action || !message) {
		logV('skipping');
		return;
	}

	if (commandstatus == 'command') {
		if (action == 'status') {
			if (message == 'ON')
				turnOn();
			else if (message == 'OFF')
				turnOff();
		} else if (action == 'turnBright') {
			turnBright();
		} else if (action == 'turnStandby') {
			turnStandby();
		} else if (action == 'turnFullWhite') {
			turnFullWhite();
		}
	}
});



process.stdin.resume(); //so the program will not close instantly

function exitHandler(options, err) {
        logV('exitHandler');
        if (options.cleanup) {
                logV('clean');
        }
        if (err && err.stack)
                logE(err.stack);
        if (options.exit)
                process.exit();
}

//do something when app is closing
process.on('exit', exitHandler.bind(null, {
                cleanup: true
        }));

//catches ctrl+c event
process.on('SIGINT', exitHandler.bind(null, {
                exit: true
        }));

// catches "kill pid" (for example: nodemon restart)
process.on('SIGUSR1', exitHandler.bind(null, {
                exit: true
        }));
process.on('SIGUSR2', exitHandler.bind(null, {
                exit: true
        }));

//catches uncaught exceptions
process.on('uncaughtException', exitHandler.bind(null, {
                exit: true
        }));

process.on('beforeExit', function () {
        logV('beforeExit fired')
})
process.on('exit', function () {
        logV('exit fired')
})

// signals
process.on('SIGUSR1', function () {
        logV('SIGUSR1 fired')
        process.exit(1)
})
process.on('SIGTERM', function () {
        logV('SIGTERM fired')
        process.exit(1)
})
process.on('SIGPIPE', function () {
        logV('SIGPIPE fired')
})
process.on('SIGHUP', function () {
        logV('SIGHUP fired')
        process.exit(1)
})
process.on('SIGTERM', function () {
        logV('SIGTERM fired')
        process.exit(1)
})
process.on('SIGINT', function () {
        logV('SIGINT fired')
        process.exit(1)
})
process.on('SIGBREAK', function () {
        logV('SIGBREAK fired')
})
process.on('SIGWINCH', function () {
        logV('SIGWINCH fired')
})

