# ltechmqtt
MQTT Nodejs support for LTSYS WIFI LED Driver CV-800220-WF03-A

# Installation
## NPM
npm install
## Configuration
Edit `cfg.js` with mqtt broker data and id of the device:
```
exports.mqttServer = { address: 'tcp://x.x.x.x', username: 'user', password: 'password' };
exports.deviceMacAddress = 'aabbccddeeff';
```

# Run
`node ltechmqtt.js` or `./ltechmqtt.js`

# Integration
Integration by mqtt.

## Send command
MQTT topic: `/ltech/deviceMacAddress/command`

### Commands
status: ON | OFF

turnBright

turnStandby

turnFullWhite

(...) - needs addition according to: CmdDateBussiness.java


## Receive state
MQTT topic: `/ltech/deviceMacAddress/state`
