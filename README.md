# react-native-android-mqtt

MQTT wrapper for react native of Paho MQTT Android Client

https://www.eclipse.org/paho/index.php?page=clients/python/docs/index.php




## Usage
```javascript
import MQTT from 'react-native-android-mqtt';




// Setup Connection
const result = await MQTT.setConnectionOptions(username, password, autoReconnect, cleanSession, keepAlive, maxInFlight, connectionTimeout);

//Default values are:
// CLEAN_SESSION_DEFAULT	true
// CONNECTION_TIMEOUT_DEFAULT	30 (in seconds)
// KEEP_ALIVE_INTERVAL_DEFAULT 60 (in seconds)
// MAX_INFLIGHT_DEFAULT	10

// Start Connection
const result = await MQTT.connect(serverUri, clientID);

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


