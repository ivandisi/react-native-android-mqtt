# react-native-android-mqtt

MQTT wrapper for react native


## Usage
```javascript
import MQTT from 'react-native-android-mqtt';

// Start Connection
const result = await MQTT.connect(serverUri, clientID, username, password, reconnect, cleanSession);

// Send message
const result = await MQTT.sendMessage(publishTopic, publishMessage);

// Subscribe topc
const result = await MQTT.subscribeTopic(topic);

// Listen events

// Complete connection
'mqtt_connectComplete'

// Message arrived
'mqtt_messageArrived'

// Delivery completed
'mqtt_deliveryComplete'

// Connection lost
'mqtt_connectionLost'


```


