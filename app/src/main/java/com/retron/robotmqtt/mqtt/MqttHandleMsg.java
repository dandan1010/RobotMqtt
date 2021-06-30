package com.retron.robotmqtt.mqtt;

import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttHandleMsg implements MqttCallback {
    private static final String TAG = "MqttHandleMsg";
    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        Log.d(TAG, "messageArrived : " + message);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}
