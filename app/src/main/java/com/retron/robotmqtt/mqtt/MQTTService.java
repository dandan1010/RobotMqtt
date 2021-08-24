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
import com.retron.robotmqtt.utils.SharedPrefUtil;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONException;
import org.json.JSONObject;


public class MQTTService extends Service {
    public static final String TAG = MQTTService.class.getSimpleName();
    private static MqttAndroidClient client;
    private MqttConnectOptions conOpt;
    private String host = "tcp://36.152.128.5:1883";//真机，实际是电脑端服务器部署的ip地址，不是手机的ip地址
    private static String attributesTopic = "v1/devices/me/attributes";//订阅的topic
    private static String telemetryTopic ="v1/devices/me/telemetry";
    private static String rpcTopic ="v1/devices/me/rpc/request/+";
    private static String requstTopic ="v1/devices/me/rpc/request/1";
    private static String responseTopic ="v1/devices/me/rpc/response/1";
    private String clientId = "";//不同客户端需要修改值
    private static String PROVISION_REQUEST_TOPIC = "/provision/request";
    private static String PROVISION_RESPONSE_TOPIC = "/provision/response";
    public static String DEFAULT_USERNAME;

    private static final String deviceName = "deviceName";
    private static final String deviceNameValue = "device_name";
    private static final String provisionDeviceKey = "provisionDeviceKey";
    private static final String provisionDeviceKeyValue = "lqmcw5ncumlxebh26lgx";
    private static final String provisionDeviceSecret = "provisionDeviceSecret";
    private static final String provisionDeviceSecretValue = "zfhlyi5jm3xv798vjjb2";


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
    public static void publishRpc(String msg) {
        Integer qos = 3;
        Boolean retained = false;
        try {
            client.publish(requstTopic, msg.getBytes(), qos.intValue(), retained.booleanValue());
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
        DEFAULT_USERNAME = SharedPrefUtil.getInstance(this).getToken("Token");
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
        //设置mqtt是否自动重连
        conOpt.setAutomaticReconnect(true);
        // 用户名
        conOpt.setUserName(DEFAULT_USERNAME);
        // 密码
        //conOpt.setPassword(passWord.toCharArray());

        // last will message
        boolean doConnect = true;
        if (doConnect) {
            doClientConnection();
        }
    }

    public static void disConnect() {
        try {
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        disConnect();
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
                client.subscribe(requstTopic, 3);
                client.subscribe(responseTopic, 4);
                if ("provision".equals(DEFAULT_USERNAME)) {
                    client.subscribe(PROVISION_RESPONSE_TOPIC, 3);
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put(deviceName,deviceNameValue);
                        jsonObject.put(provisionDeviceKey, provisionDeviceKeyValue);
                        jsonObject.put(provisionDeviceSecret, provisionDeviceSecretValue);
                        client.publish(PROVISION_REQUEST_TOPIC, jsonObject.toString().getBytes(), 0, false);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Intent intentServer = new Intent(MQTTService.this, KeepAliveService.class);
                    stopService(intentServer);
                    startService(intentServer);
                }
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
