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
