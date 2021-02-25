# react-native-android-mqtt

MQTT wrapper for react native of Paho MQTT Android Client
https://www.eclipse.org/paho/index.php?page=clients/python/docs/index.php


## Usage
```javascript
import MQTT from 'react-native-android-mqtt';

// Start Connection
const result = await MQTT.connect(serverUri, clientID, username, password, reconnect, cleanSession);

// Disconnection
const result = await MQTT.disconnect();

// isConnected 
const result = await MQTT.isConnected();

// Send message
const result = await MQTT.sendMessage(publishTopic, publishMessage);

// Subscribe topc
const result = await MQTT.subscribeTopic(topic);

// UnSubscribe topc
const result = await MQTT.unsubscribeTopic(topic);

// Listen events
import { DeviceEventEmitter } from 'react-native';

...
function handleDelivery(event) {
  console.log(event);
}

...
DeviceEventEmitter.addListener('mqtt_connectionLost', handleDelivery);


// Complete connection
'mqtt_connectComplete'
// Message arrived
'mqtt_messageArrived'
// Delivery completed
'mqtt_deliveryComplete'
// Connection lost
'mqtt_connectionLost'

```


