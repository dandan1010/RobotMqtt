package com.retron.robotmqtt.utils;

import android.text.TextUtils;
import android.util.Log;

import com.retron.robotmqtt.bean.ContrastPointDataBean;
import com.retron.robotmqtt.bean.DrawLineBean;
import com.retron.robotmqtt.bean.MapListDataChangeBean;
import com.retron.robotmqtt.bean.PointDataBean;
import com.retron.robotmqtt.bean.SaveTaskBean;
import com.retron.robotmqtt.bean.VirtualDataBean;
import com.retron.robotmqtt.bean.VirtualDataChangeBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GsonUtils {
    private static final String TAG = "GsonUtils";
    private static final String TYPE = "type";
    private static final String METHOD = "method";

    private JSONObject jsonObject;
    private String mapNameUuid;
    private String mapName;

    public void setMapNameUuid(String mapNameUuid) {
        this.mapNameUuid = mapNameUuid;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

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

    public String getMethod(String message) {
        String type = null;
        if (message == null) {
            Log.d(TAG, "getType message is null");
            return null;
        }
        try {
            JSONObject jsonObject = new JSONObject(message);
            type = jsonObject.getString(METHOD);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "getType message is " + type);
        return type;
    }

    public String sendRobotMsg(String type, String msg) {
        jsonObject = new JSONObject();
        try {
            JSONObject json = new JSONObject(msg);
            jsonObject.put(type, json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("sendRobotMsg11111 ", jsonObject.toString());
        return jsonObject.toString();
    }

    public String sendRobotMsg(String type, JSONArray msg) {
        jsonObject = new JSONObject();
        try {
            jsonObject.put(type, msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("sendJsonArray ", jsonObject.toString());
        return jsonObject.toString();
    }

    public String sendResponse(String type, String msg) {
        jsonObject = new JSONObject();
        try {
            jsonObject.put(type, msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("sendRobotMsg11111 ", jsonObject.toString());
        return jsonObject.toString();
    }

    public String sendMapbinary(String type,String mapuuid) {
        jsonObject = new JSONObject();
        String filePath = "/sdcard/robotMap/" + mapuuid+ ".tar.gz";
        File file = new File(filePath);
        try {
            JSONObject json = new JSONObject();
            json.put(Content.MAP_NAME_UUID, mapuuid);
            json.put(Content.dump_md5, Md5Utils.getMd5ByFile(file));
            json.put(Content.data, FileUtils.fileToBase64(filePath));
            jsonObject.put(type, json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("sendRobotMsg11111 ", jsonObject.toString());
        return jsonObject.toString();
    }

    public String sendRobotMsg(String type, String oldMapUuid, String newMapName) {
        jsonObject = new JSONObject();
        try {
            jsonObject.put(TYPE, type);
            jsonObject.put(Content.MAP_NAME_UUID, oldMapUuid);
            jsonObject.put(Content.NEW_MAP_NAME, newMapName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("renameMap ", jsonObject.toString());
        return jsonObject.toString();
    }

    public String putRobotMsg(String type, String msg) {
        jsonObject = new JSONObject();
        try {
            jsonObject.put(TYPE, type);
            jsonObject.put(Content.MAP_NAME_UUID, mapNameUuid);
            jsonObject.put(Content.MAP_NAME, mapName);
            if (!TextUtils.isEmpty(msg)) {
                JSONObject js = new JSONObject(msg);
                jsonObject.put(type, js);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("sendRobotMsg ", jsonObject.toString());
        return jsonObject.toString();
    }

    public String putRobotString(String type, String msg) {
        jsonObject = new JSONObject();
        try {
            jsonObject.put(TYPE, type);
            jsonObject.put(Content.MAP_NAME_UUID, mapNameUuid);
            jsonObject.put(Content.MAP_NAME, mapName);
            jsonObject.put(type, msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("sendRobotMsg ", jsonObject.toString());
        return jsonObject.toString();
    }

    public String deletePosition(String type, String pointName) {
        jsonObject = new JSONObject();
        try {
            jsonObject.put(TYPE, type);
            jsonObject.put(Content.MAP_NAME_UUID, mapNameUuid);
            jsonObject.put(Content.MAP_NAME, mapName);
            jsonObject.put(Content.POINT_NAME, pointName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("sendRobotMsg ", jsonObject.toString());
        return jsonObject.toString();
    }

    public String sendRobotMsg(String type, List<String> list) {
        jsonObject = new JSONObject();
        try {
            jsonObject.put(TYPE, type);
            JSONArray jsonArray = new JSONArray(list);
            jsonObject.put(type, jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("renameMap ", jsonObject.toString());
        return jsonObject.toString();
    }

    public String downloadlog(String type, List<String> list) {
        jsonObject = new JSONObject();
        try {
            jsonObject.put(TYPE, type);
            JSONArray jsonArray = new JSONArray(list);
            jsonObject.put(Content.FILE_NAME, jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("downloadlog ", jsonObject.toString());
        return jsonObject.toString();
    }

    public String sendRobotMsg(String type, String mapUuid, ContrastPointDataBean contrastPointDataBean) {
        jsonObject = new JSONObject();
        try {
            jsonObject.put(TYPE, type);
            jsonObject.put(Content.MAP_NAME_UUID, mapUuid);
            jsonObject.put(Content.POINT_NAME, contrastPointDataBean.getPoint_Name());
            jsonObject.put(Content.POINT_X, contrastPointDataBean.getPoint_x());
            jsonObject.put(Content.POINT_Y, contrastPointDataBean.getPoint_y());
            jsonObject.put(Content.POINT_TYPE, contrastPointDataBean.getPoint_type());
            jsonObject.put(Content.POINT_TIME, contrastPointDataBean.getPoint_time());
            jsonObject.put(Content.ANGLE, contrastPointDataBean.getAngle());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("point msg ", jsonObject.toString());
        return jsonObject.toString();
    }

    public String sendRobotMsg(String type, ArrayList<MapListDataChangeBean> dataChangeBean) {
        jsonObject = new JSONObject();
        try {
            jsonObject.put(TYPE, type);
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i< dataChangeBean.size();i++) {
                jsonArray.put(dataChangeBean.get(i).getMapDataBean().getMap_name_uuid());
            }
            jsonObject.put(type, jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("MapListDataChangeBean ", jsonObject.toString());
        return jsonObject.toString();
    }

    public String putVirtualObstacle(String type, VirtualDataBean virtualObstacleBean) {
        jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            jsonObject.put(TYPE, type);
            jsonObject.put(Content.MAP_NAME_UUID, mapNameUuid);
            jsonObject.put(Content.MAP_NAME, mapName);
            if (virtualObstacleBean != null && virtualObstacleBean.getSend_virtual() != null) {
                for (int i = 0; i < virtualObstacleBean.getSend_virtual().size(); i++) {
                    JSONArray jsArray = new JSONArray();
                    for (int j = 0; j < virtualObstacleBean.getSend_virtual().get(i).size(); j++) {
                        JSONObject js = new JSONObject();
                        js.put(Content.VIRTUAL_X, virtualObstacleBean.getSend_virtual().get(i).get(j).getVirtual_x());
                        js.put(Content.VIRTUAL_Y, virtualObstacleBean.getSend_virtual().get(i).get(j).getVirtual_y());
                        jsArray.put(j, js);
                    }
                    jsonArray.put(i, jsArray);
                }
                jsonObject.put(type, jsonArray);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "putVirtualObstacle : " + jsonObject.toString());
        return jsonObject.toString();
    }

    public String putSaveTaskMsg(String mapName, String mapUid, String taskName, String taskTime, String isRun, List<PointDataBean> tasks, List<String> repeatDays){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(TYPE, Content.SAVETASKQUEUE);
            jsonObject.put(Content.MAP_NAME, mapName);
            jsonObject.put(Content.MAP_NAME_UUID, mapUid);
            jsonObject.put(Content.TASK_NAME, taskName);
            jsonObject.put(Content.dbAlarmTime, taskTime);
            jsonObject.put(Content.dbAlarmIsRun, isRun);
            JSONArray jsonTask = new JSONArray();
            if (tasks != null && tasks.size() != 0) {
                for (int i = 0; i < tasks.size(); i++) {
                    JSONObject object = new JSONObject();
                    object.put(Content.POINT_NAME, tasks.get(i).getPoint_Name());
                    object.put(Content.POINT_X, tasks.get(i).getPoint_x());
                    object.put(Content.POINT_Y, tasks.get(i).getPoint_y());
                    object.put(Content.ANGLE, tasks.get(i).getAngle());
                    object.put(Content.POINT_TYPE, tasks.get(i).getPoint_type());
                    object.put(Content.POINT_TIME, tasks.get(i).getPoint_time());
                    jsonTask.put(object);
                }
            }
            jsonObject.put(Content.SAVETASKQUEUE, jsonTask);
            if (repeatDays != null) {
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < repeatDays.size(); i++) {
                    jsonArray.put(i, repeatDays.get(i));
                }
                jsonObject.put(Content.dbAlarmCycle, jsonArray);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public String deleteTask(String deletetaskqueue, String map_name_uuid, String map_name, String task_name) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(TYPE, deletetaskqueue);
            jsonObject.put(Content.MAP_NAME, mapName);
            jsonObject.put(Content.MAP_NAME_UUID, map_name_uuid);
            jsonObject.put(Content.TASK_NAME, task_name);
            jsonObject.put(Content.MAP_NAME, map_name);
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public String putRobotMsg(String starttaskqueue, String mapNameUuid, String mapName, String task_name) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(TYPE, starttaskqueue);
            jsonObject.put(Content.MAP_NAME, mapName);
            jsonObject.put(Content.MAP_NAME_UUID, mapNameUuid);
            jsonObject.put(Content.TASK_NAME, task_name);
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();

    }
}
