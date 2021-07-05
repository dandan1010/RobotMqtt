package com.retron.robotmqtt.mqtt;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.retron.robotmqtt.KeepAliveService;
import com.retron.robotmqtt.bean.MapListDataBean;
import com.retron.robotmqtt.bean.VirtualDataBean;
import com.retron.robotmqtt.contrast.Contrast;
import com.retron.robotmqtt.utils.Content;
import com.retron.robotmqtt.utils.GsonUtils;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

public class MqttHandleMsg implements MqttCallback {
    private static final String TAG = "MqttHandleMsg";
    private GsonUtils mGsonUtils;
    private Context mContext;
    private Gson mGson;
    private Contrast mContrast;

    public MqttHandleMsg(Context mContext) {
        this.mGsonUtils = new GsonUtils();
        this.mGson = new Gson();
        this.mContext = mContext;
        this.mContrast = new Contrast();
    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String str1 = new String(message.getPayload());
        Log.d(TAG, "messageArrived : " + str1);
        handleMessage(str1);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    private void handleMessage(String message) {
        switch (mGsonUtils.getType(message)) {
            case Content.SENDMAPNAME:
                //地图列表
                MapListDataBean mapList = mGson.fromJson(message, MapListDataBean.class);
                mContrast.contrastMapList(mapList, mGsonUtils);
                break;
            case Content.USE_MAP:
                try {
                    JSONObject jsonObject = new JSONObject(message);
                    Log.d(TAG, "USE_MAP : " + jsonObject.toString());
                    KeepAliveService.webSocket.send(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Content.UPDATE:
                //
                break;
            default:
                Log.e(TAG, "NO USER MESSAGE " + message);
                break;
        }
    }
}
