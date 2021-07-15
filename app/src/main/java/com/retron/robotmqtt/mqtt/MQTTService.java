package com.retron.robotmqtt.mqtt;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.retron.robotmqtt.KeepAliveService;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;


public class MQTTService extends Service {
    public static final String TAG = MQTTService.class.getSimpleName();
    private static MqttAndroidClient client;
    private MqttConnectOptions conOpt;
    //    private String host = "tcp://10.0.2.2:61613";//模拟器
    private String host = "tcp://36.152.128.5:1883";//真机，实际是电脑端服务器部署的ip地址，不是手机的ip地址
    private String userName = "7HJqCiVjzKN1p2iSW6AJ";
    private static String attributesTopic = "v1/devices/me/attributes";//订阅的topic
    private static String telemetryTopic ="v1/devices/me/telemetry";
    private static String rpcTopic ="v1/devices/me/rpc/request/+";
    private String clientId = "";//不同客户端需要修改值

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        init();
        return super.onStartCommand(intent, flags, startId);
    }

    public static void publish(String msg) {
        Integer qos = 0;
        Boolean retained = false;
        try {
            client.publish(attributesTopic, msg.getBytes(), qos.intValue(), retained.booleanValue());
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    public static void publishTelemetry(String msg) {
        Integer qos = 1;
        Boolean retained = false;
        try {
            client.publish(telemetryTopic, msg.getBytes(), qos.intValue(), retained.booleanValue());
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        // 服务器地址（协议+地址+端口号）
        String uri = host;
        client = new MqttAndroidClient(this, uri, clientId);
        // 设置MQTT监听并且接受消息
        MqttHandleMsg mqttHandleMsg = new MqttHandleMsg(this);

        client.setCallback(mqttHandleMsg);

        conOpt = new MqttConnectOptions();
        // 清除缓存
        conOpt.setCleanSession(true);
        // 设置超时时间，单位：秒
        conOpt.setConnectionTimeout(20);
        // 心跳包发送间隔，单位：秒
        conOpt.setKeepAliveInterval(20);
        // 用户名
        conOpt.setUserName(userName);
        // 密码
        //conOpt.setPassword(passWord.toCharArray());

        // last will message
        boolean doConnect = true;
        if (doConnect) {
            doClientConnection();
        }
    }

    @Override
    public void onDestroy() {
        try {
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    /**
     * 连接MQTT服务器
     */
    private void doClientConnection() {
        if (!client.isConnected() && isConnectIsNomarl()) {
            try {
                client.connect(conOpt, null, iMqttActionListener);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    // MQTT是否连接成功
    private IMqttActionListener iMqttActionListener = new IMqttActionListener() {
        @Override
        public void onSuccess(IMqttToken arg0) {
            Log.i(TAG, "MQTT1111 连接成功 ");
            try {
                // 订阅myTopic话题
                client.subscribe(attributesTopic, 0);
                client.subscribe(telemetryTopic, 1);
                client.subscribe(rpcTopic, 2);
                Intent intentServer = new Intent(MQTTService.this, KeepAliveService.class);
                stopService(intentServer);
                startService(intentServer);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(IMqttToken arg0, Throwable arg1) {
            // 连接失败，重连
            Log.i(TAG, "MQTT1111连接失败：" + arg1.getLocalizedMessage());
            Log.i(TAG, "MQTT1111连接失败：" + arg1.getMessage());
            arg1.printStackTrace();
        }
    };

    /**
     * 判断网络是否连接
     */
    private boolean isConnectIsNomarl() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            String name = info.getTypeName();
            Log.i(TAG, "MQTT1111当前网络名称：" + name);
            return true;
        } else {
            Log.i(TAG, "MQTT1111 没有可用网络");
            return false;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
