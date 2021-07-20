package com.retron.robotmqtt.mqtt;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.retron.robotmqtt.KeepAliveService;
import com.retron.robotmqtt.bean.MapListDataBean;
import com.retron.robotmqtt.bean.SchedulerTaskListBean;
import com.retron.robotmqtt.bean.UploadLogBean;
import com.retron.robotmqtt.manager.HandlerThreadManager;
import com.retron.robotmqtt.utils.Content;
import com.retron.robotmqtt.utils.GsonUtils;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MqttHandleMsg implements MqttCallback {
    private static final String TAG = "MqttHandleMsg";
    private GsonUtils mGsonUtils;
    private Context mContext;
    private Gson mGson;
    public static UploadLogBean uploadLogBean;

    public MqttHandleMsg(Context mContext) {
        this.mGsonUtils = new GsonUtils();
        this.mGson = new Gson();
        this.mContext = mContext;
    }

    public static void downloadMapResult(String fileName) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type",Content.UPLOAD_MAP);
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(fileName);
            jsonObject.put(Content.UPLOAD_MAP, jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        KeepAliveService.webSocket.send(jsonObject.toString());
    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String str1 = new String(message.getPayload());
        Log.d(TAG, "messageArrived : " + str1);
        /*{"method":"execute_task","params":{"type":"execute_task","task_Name":"Temp","map_name_uuid":"1624866409310","map_name":"mapname"}}*/
        handleMessage(str1);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    private void handleMessage(String message) {
        try {
            JSONObject jsonObject = new JSONObject(message);
            String params = jsonObject.getString("params");
            JSONObject jsonParams = new JSONObject(params);
            JSONArray jsonArray;
            switch (mGsonUtils.getMethod(message)) {
                case Content.syncMap:
                    //地图列表
                    MapListDataBean mapList = mGson.fromJson(params, MapListDataBean.class);
                    HandlerThreadManager.getInstance(mContext).contrastMapList(mapList);
                    break;
                case Content.USE_MAP:
                    Log.d(TAG, "USE_MAP : " + jsonParams.toString());
                    KeepAliveService.webSocket.send(jsonParams.toString());
                    break;
                case Content.sync_task_list:
                    SchedulerTaskListBean taskListBean = mGson.fromJson(params, SchedulerTaskListBean.class);
                    HandlerThreadManager.getInstance(mContext).contrastTaskList(taskListBean);
                    break;
                case Content.syncSetting:
                    KeepAliveService.webSocket.send(params);
                    break;
                case Content.initialize:
                    KeepAliveService.webSocket.send(mGsonUtils.putRobotMsg(Content.initialize, ""));
                    break;
                case Content.send_map_dump:
                    String mapUuid = jsonParams.getString(Content.MAP_NAME_UUID);
                    String md5 = jsonParams.getString(Content.dump_md5);
                    HandlerThreadManager.getInstance(mContext).uploadMapDump(mapUuid);
                    break;
                case Content.move:
                    KeepAliveService.webSocket.send(mGsonUtils.putRobotMsg(Content.STARTMOVE, params));
                    break;
                case Content.sync_map_dump://上传dump文件
                    String mapNameUuid = jsonParams.getString(Content.MAP_NAME_UUID);
                    MQTTService.publish(mGsonUtils.sendMapbinary(Content.sync_map_dump, mapNameUuid));
                    break;
                case Content.upload_log:
                    uploadLogBean = mGson.fromJson(params, UploadLogBean.class);
                    ArrayList<String> arrayList = new ArrayList<>();
                    for (int i = 0; i < uploadLogBean.getMap().size(); i++) {
                        arrayList.add(uploadLogBean.getMap().get(i).getRos_bag());
                    }
                    KeepAliveService.webSocket.send(mGsonUtils.downloadlog(Content.MQTT_DOWNLOAD_LOG, arrayList));
                    break;

                case Content.reset:
                    KeepAliveService.webSocket.send(mGsonUtils.putRobotMsg(Content.RESET_ROBOT, ""));
                    break;
                case Content.execute_task:
                    KeepAliveService.webSocket.send(mGsonUtils.putRobotMsg(
                            Content.STARTTASKQUEUE,
                            jsonParams.getString(Content.MAP_NAME_UUID),
                            jsonParams.getString(Content.MAP_NAME),
                            jsonParams.getString(Content.TASK_NAME)));
                    break;
                case Content.ota:
                    jsonArray = jsonParams.getJSONArray("update");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String packageName = jsonArray.getJSONObject(i).getString("update_file_name");
                        if (packageName.startsWith("robotmqtt")) {
                            Log.d(TAG, "下载robotmqtt ： " + jsonArray.getJSONObject(i).getString("otafile_link"));

                        } else {
                            Log.d(TAG, "下载robot ： " + jsonArray.getJSONObject(i).getString("otafile_link"));
                        }
                    }
                    break;
                default:
                    Log.e(TAG, "NO USER MESSAGE " + message);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
