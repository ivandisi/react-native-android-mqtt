# react-native-android-mqtt

MQTT wrapper for react native


## Usage
```javascript
import MQTT from 'react-native-android-mqtt';

// Scan the network where the device is connected
const result = await NetScan.findDevices();

// Scan the network by Ip (for example 192.168.0.1)
const result = await NetScan.findDevicesFromIp(ip);

// Find a device Ip by MAC
const result = await NetScan.findDeviceByMAC(MAC);

const result = await NetScan.findDeviceByMACwithNetworkIP(ip, MAC);

// Port Scan by IP (TCP)
const result = await NetScan.scanOpenTCPPorts(IP,timeout);

// Port Scan by IP (UDP)
const result = await NetScan.scanOpenUDPPorts(IP,timeout);
```


