package it.id.mqtt;

import android.app.Activity;
import android.content.ComponentName;
import android.util.SparseArray;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.bridge.WritableArray; 
import com.facebook.react.modules.core.DeviceEventManagerModule;

import android.content.ComponentName;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.annotation.Nullable;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MQTTModule extends ReactContextBaseJavaModule implements MqttCallbackExtended{


  private MqttAndroidClient mqttAndroidClient;
  private ReactContext context;


  public MQTTModule(ReactApplicationContext reactContext) {
      super(reactContext);
      context = reactContext;
  }

  @Override
  public String getName() {
      return "MQTT";
  }

  @ReactMethod
  public void connect(final String serverUri, final String clientID, final boolean reconnect, final boolean cleanSession, final Promise promise) {
      if (!TextUtils.isEmpty(serverUri) && !TextUtils.isEmpty(clientID)) {
          
        mqttAndroidClient = new MqttAndroidClient(((ReactApplicationContext)context).getApplicationContext(), serverUri, clientID);
        mqttAndroidClient.setCallback(this);

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(reconnect);
        mqttConnectOptions.setCleanSession(cleanSession);

        try {
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
       
                    promise.resolve(true);
                }
    
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    promise.resolve("ConnectionError: " + exception.getMessage());
                    exception.printStackTrace();
                }
            });
        } catch (Exception exception) {
            promise.resolve("ConnectionError: " + exception.getMessage());
            exception.printStackTrace();
        }

      } else {
          promise.resolve(null);
      }
  }

  private void sendEvent(String eventName, @Nullable WritableMap params) {
    context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
  }

  @Override
  public void connectComplete(boolean reconnect, String serverURI) {
    WritableMap params = Arguments.createMap();
    params.putString("connectionCompleted", serverURI);
    params.putBoolean("reconnection", reconnect);
    sendEvent("mqtt_connectComplete", params);
  }

  @Override
  public void connectionLost(Throwable cause) {
    WritableMap params = Arguments.createMap();
    params.putString("connectionLost", cause.getMessage()); 
    sendEvent("connectionLost", params);
  }

  @Override
  public void messageArrived(String topic, MqttMessage message) throws Exception {
    WritableMap params = Arguments.createMap();
    params.putString("message", message.toString());
    params.putString("topic", topic);
    sendEvent("mqtt_messageArrived", params);
  }

  @Override
  public void deliveryComplete(IMqttDeliveryToken token) {
    WritableMap params = Arguments.createMap();
    try {
        params.putString("token", token.getMessage().toString());
    } catch (Exception exception) {
        exception.printStackTrace();
    }
    sendEvent("mqtt_deliveryComplete", params);
  }

  @ReactMethod
  public void subscribeTopic(final String topic, final Promise promise) {
    if (!TextUtils.isEmpty(topic)) {
        try {
            mqttAndroidClient.subscribe(topic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    promise.resolve(true); // TO CHECK, is usefull this token?
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    promise.resolve("SubscribeError: " + exception.getMessage());
                }
            });
        } catch (MqttException exception) {
            promise.resolve("SubscribeError: " + exception.getMessage());
            exception.printStackTrace();
        }
    } else {
        promise.resolve(null);
    }
  }

  @ReactMethod
  public void sendMessage(final String publishTopic, final String publishMessage, final Promise promise) {
      if (!TextUtils.isEmpty(publishMessage)) {
        try {
            MqttMessage message = new MqttMessage();
            message.setPayload(publishMessage.getBytes());

            if (!mqttAndroidClient.isConnected()) {
                promise.resolve("PublishingError: not connected");
            } else {
                mqttAndroidClient.publish(publishTopic, message);
                promise.resolve(true);
            }
        } catch (MqttException exception) {
            promise.resolve("PublishingError: " + exception.getMessage());
            
        }
      } else {
          promise.resolve(null);
      }
  }

}