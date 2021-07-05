package com.retron.robotmqtt.utils;

import android.util.Log;

import com.retron.robotmqtt.bean.DrawLineBean;
import com.retron.robotmqtt.bean.SaveTaskBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GsonUtils {
    private static final String TAG = "GsonUtils";
    private static final String TYPE = "type";

    private JSONObject jsonObject;

    public String getType(String message) {
        String type = null;
        if (message == null) {
            Log.d(TAG, "getType message is null");
            return null;
        }
        try {
            JSONObject jsonObject = new JSONObject(message);
            type = jsonObject.getString(TYPE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "getType message is " + type);
        return type;
    }

    public String sendRobotMsg(String type, String msg) {
        jsonObject = new JSONObject();
        try {
            jsonObject.put(TYPE, type);
            jsonObject.put(type, msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("sendRobotMsg ", jsonObject.toString());
        return jsonObject.toString();
    }

    public String sendRobotMsg(String type, long msg) {
        jsonObject = new JSONObject();
        try {
            jsonObject.put(TYPE, type);
            jsonObject.put(type, msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("sendRobotMsg ", jsonObject.toString());
        return jsonObject.toString();
    }

    public String sendRobotMsg(String type, boolean msg) {
        jsonObject = new JSONObject();
        try {
            jsonObject.put(TYPE, type);
            jsonObject.put(type, msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("sendRobotMsg ", jsonObject.toString());
        return jsonObject.toString();
    }

    public String sendRobotMsg(String type, List<String> list) {
        jsonObject = new JSONObject();
        try {
            jsonObject.put("topicFilter","/sensor/data");
            JSONObject json = new JSONObject();
            json.put("type","json");
            json.put("deviceNameJsonExpression","${serialNumber}");
            json.put("deviceTypeJsonExpression","${sensorType}");
            json.put("timeout", 5000);
            jsonObject.put("converter",json);
            JSONObject object = new JSONObject();
            object.put(TYPE, type);
            object.put("key","String");
            object.put("value","${sensorModel}");
            jsonObject.put("attributes", object);
            JSONArray jsonArray = new JSONArray(list);
            jsonObject.put(type, jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("sendRobotMsg ", jsonObject.toString());
        return jsonObject.toString();
    }

}
