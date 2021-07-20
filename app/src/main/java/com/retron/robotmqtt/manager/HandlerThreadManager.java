package com.retron.robotmqtt.manager;

import android.content.Context;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.retron.robotmqtt.KeepAliveService;
import com.retron.robotmqtt.bean.ContrastPointDataBean;
import com.retron.robotmqtt.bean.MapListDataBean;
import com.retron.robotmqtt.bean.MapListDataChangeBean;
import com.retron.robotmqtt.bean.PointDataBean;
import com.retron.robotmqtt.bean.SchedulerTaskListBean;
import com.retron.robotmqtt.bean.UploadLogBean;
import com.retron.robotmqtt.mqtt.MQTTService;
import com.retron.robotmqtt.utils.Content;
import com.retron.robotmqtt.utils.GsonUtils;
import com.retron.robotmqtt.utils.Md5Utils;
import com.retron.robotmqtt.utils.SharedPrefUtil;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HandlerThreadManager implements Handler.Callback {

    public static HandlerThreadManager handlerThreadManager;
    private Context mContext;
    private ArrayList<MapListDataChangeBean> changeBeans;
    private GsonUtils gsonUtils;
    private final String renameMapName = "renameMapName";
    private final String deleteMapName = "deleteMapName";
    private final String addMapName = "addMapName";
    private final String deletePosition = "deletePosition";
    private final String addPosition = "addPosition";
    private int addMapCount = 0;
    private Gson gson;
    private HandlerThread handlerThread;
    private Handler childHandler;

    public HandlerThreadManager(Context mContext) {
        this.mContext = mContext;
        gsonUtils = new GsonUtils();
        gson = new Gson();
        handlerThread = new HandlerThread("download");
        handlerThread.start();
        //子线程Handler
        childHandler = new Handler(handlerThread.getLooper(), this);

    }

    public static HandlerThreadManager getInstance(Context context) {
        if (handlerThreadManager == null) {
            synchronized (SharedPrefUtil.class) {
                if (handlerThreadManager == null) {
                    handlerThreadManager = new HandlerThreadManager(context);
                }
            }
        }
        return handlerThreadManager;
    }

    public void checkMapList() {
        Log.d("threadPoolExecutor", "runnable" + changeBeans.toString());
        if (changeBeans != null) {
            ArrayList<String> arrayList = new ArrayList<>();
            for (int i = 0; i < changeBeans.size(); i++) {
                Log.d("threadPoolExecutor", "type : " + changeBeans.get(i).getType());
                switch (changeBeans.get(i).getType()) {
                    case renameMapName:
                        Log.d("threadPoolExecutor", "修改地图名字：" + changeBeans.get(i).getMapDataBean().getMap_name());
                        KeepAliveService.webSocket.send(gsonUtils.sendRobotMsg(Content.MQTT_RENAME_MAP,
                                changeBeans.get(i).getMapDataBean().getMap_name_uuid(),
                                changeBeans.get(i).getMapDataBean().getMap_name()));
                        gsonUtils.setMapNameUuid(changeBeans.get(i).getMapDataBean().getMap_name_uuid());
                        gsonUtils.setMapName(changeBeans.get(i).getMapDataBean().getMap_name());
                        KeepAliveService.webSocket.send(gsonUtils.putVirtualObstacle(Content.MQTT_UPDATA_VIRTUAL,
                                changeBeans.get(i).getMapDataBean().getVirtualDataBeans()));
                        ArrayList<ContrastPointDataBean> point = changeBeans.get(i).getMapDataBean().getPoint();
                        for (int j = 0; j < point.size(); j++) {
                            switch (point.get(j).getType()) {
                                case deletePosition:
                                    gsonUtils.setMapNameUuid(changeBeans.get(i).getMapDataBean().getMap_name_uuid());
                                    gsonUtils.setMapName(changeBeans.get(i).getMapDataBean().getMap_name());
                                    KeepAliveService.webSocket.send(gsonUtils.deletePosition(
                                            Content.DELETE_POSITION, changeBeans.get(i).getMapDataBean().getPoint().get(j).getPoint_Name()));
                                    break;
                                default:
                                    break;
                            }
                        }
                        for (int j = 0; j < point.size(); j++) {
                            switch (point.get(j).getType()) {
                                case addPosition:
                                    KeepAliveService.webSocket.send(gsonUtils.sendRobotMsg(
                                            Content.MQTT_ADD_POINT,
                                            changeBeans.get(i).getMapDataBean().getMap_name_uuid(),
                                            point.get(j)));
                                    break;
                                default:
                                    break;
                            }
                        }
                        break;
                    case deleteMapName:
                        Log.d("threadPoolExecutor", "删除地图：" + changeBeans.get(i).getMapDataBean().getMap_name_uuid());
                        arrayList.add(changeBeans.get(i).getMapDataBean().getMap_name_uuid());
                        break;
                    case addMapName:
                        Log.d("threadPoolExecutor", "addMapName：" + changeBeans.get(i).getMapDataBean().getMap_name_uuid());
                        downloadMap(changeBeans.get(i).getMapDataBean().getMap_name_uuid(), changeBeans.get(i).getMapDataBean().getDump_md5());
                        break;
                    default:
                        Log.d("threadPoolExecutor", "default");
                        ArrayList<ContrastPointDataBean> pointDataBeans = changeBeans.get(i).getMapDataBean().getPoint();
                        gsonUtils.setMapNameUuid(changeBeans.get(i).getMapDataBean().getMap_name_uuid());
                        gsonUtils.setMapName(changeBeans.get(i).getMapDataBean().getMap_name());
                        KeepAliveService.webSocket.send(gsonUtils.putVirtualObstacle(Content.MQTT_UPDATA_VIRTUAL,
                                changeBeans.get(i).getMapDataBean().getVirtualDataBeans()));
                        for (int j = 0; j < pointDataBeans.size(); j++) {
                            switch (pointDataBeans.get(j).getType()) {
                                case deletePosition:
                                    gsonUtils.setMapNameUuid(changeBeans.get(i).getMapDataBean().getMap_name_uuid());
                                    gsonUtils.setMapName(changeBeans.get(i).getMapDataBean().getMap_name());
                                    KeepAliveService.webSocket.send(gsonUtils.deletePosition(
                                            Content.DELETE_POSITION, changeBeans.get(i).getMapDataBean().getPoint().get(j).getPoint_Name()));
                                    break;
                                default:
                                    break;
                            }
                        }
                        for (int j = 0; j < pointDataBeans.size(); j++) {
                            switch (pointDataBeans.get(j).getType()) {
                                case addPosition:
                                    KeepAliveService.webSocket.send(gsonUtils.sendRobotMsg(
                                            Content.MQTT_ADD_POINT,
                                            changeBeans.get(i).getMapDataBean().getMap_name_uuid(),
                                            pointDataBeans.get(j)));
                                    break;
                                default:
                                    break;
                            }
                        }
                        break;
                }
            }
            Log.d("threadPoolExecutor", "删除地图array ：" + arrayList.toString());
            if (arrayList.size() != 0) {
                KeepAliveService.webSocket.send(gsonUtils.sendRobotMsg(Content.DELETE_MAP, arrayList));
                arrayList.clear();
            }
            for (int i = 0; i < changeBeans.size(); i++) {
                Log.d("threadPoolExecutor", "type : " + changeBeans.get(i).getType());
                switch (changeBeans.get(i).getType()) {
                    case renameMapName:
                    case deleteMapName:
                        changeBeans.remove(i);
                        break;
                    default:
                        break;
                }
            }
        }
        childHandler.sendEmptyMessageDelayed(3, 3000);
    }

    public void checkAddMapPoint(String mapUuid) {
        if (changeBeans != null) {
            for (int i = 0; i < changeBeans.size(); i++) {
                if (mapUuid.equals(changeBeans.get(i).getMapDataBean().getMap_name_uuid())) {
                    gsonUtils.setMapNameUuid(changeBeans.get(i).getMapDataBean().getMap_name_uuid());
                    gsonUtils.setMapName(changeBeans.get(i).getMapDataBean().getMap_name());
                    KeepAliveService.webSocket.send(gsonUtils.putVirtualObstacle(Content.MQTT_UPDATA_VIRTUAL,
                            changeBeans.get(i).getMapDataBean().getVirtualDataBeans()));
                    ArrayList<ContrastPointDataBean> point = changeBeans.get(i).getMapDataBean().getPoint();
                    for (int j = 0; j < point.size(); j++) {
                        switch (point.get(j).getType()) {
                            case addPosition:
                                Log.d("新地图添加点", point.get(j) + ",    "+ changeBeans.get(i).getMapDataBean().getMap_name_uuid());
                                KeepAliveService.webSocket.send(gsonUtils.sendRobotMsg(
                                        Content.MQTT_ADD_POINT,
                                        changeBeans.get(i).getMapDataBean().getMap_name_uuid(),
                                        point.get(j)));
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
            changeBeans.clear();
        }
    }

    public void contrastMapList(MapListDataBean mapListDataBean) {
        Log.d("地图", "地图 --" + gson.toJson(mapListDataBean));
        changeBeans = new ArrayList<>();
        if (mapListDataBean.getSendMapName().size() == 0
                && KeepAliveService.mapViewModel != null
                && KeepAliveService.mapViewModel.getMapList() != null
                && KeepAliveService.mapViewModel.getMapList().getSendMapName().size() != 0) {
            //无地图，删除所有地图
            Log.d("地图", deleteMapName + ",修改地图名字为： 清空");
            for (int i = 0; i < KeepAliveService.mapViewModel.getMapList().getSendMapName().size(); i++) {
                MapListDataChangeBean mapListDataChangeBean = new MapListDataChangeBean();
                mapListDataChangeBean.setType(deleteMapName);
                MapListDataChangeBean.MapDataBean mapDataBean = new MapListDataChangeBean.MapDataBean();
                //mapDataBean.setPoint(contrastPoint(KeepAliveService.mapViewModel.getMapList().getSendMapName().get(i).getPoint(), mapListDataBean.getSendMapName().get(j).getPoint(), mapListDataChangeBean));
                //mapDataBean.setVirtualDataBeans(mapListDataBean.getSendMapName().get(j).getVirtualDataBeans());
                mapDataBean.setMap_name_uuid(KeepAliveService.mapViewModel.getMapList().getSendMapName().get(i).getMap_name_uuid());
                mapDataBean.setMap_name(KeepAliveService.mapViewModel.getMapList().getSendMapName().get(i).getMap_name());
                mapListDataChangeBean.setMapDataBean(mapDataBean);
                changeBeans.add(mapListDataChangeBean);
            }
        } else if (KeepAliveService.mapViewModel != null
                && KeepAliveService.mapViewModel.getMapList() != null
                && KeepAliveService.mapViewModel.getMapList().getSendMapName().size() != 0) {
            ArrayList<MapListDataBean.MapDataBean> arrayList = KeepAliveService.mapViewModel.getMapList().getSendMapName();
            Log.d("地图", "循环aaa ： ");
            List<Integer> index = new ArrayList<>();
//            if (arrayList.size() >= mapListDataBean.getSendMapName().size()) {
            for (int i = 0; i < arrayList.size(); i++) {
                for (int j = 0; j < mapListDataBean.getSendMapName().size(); j++) {
                    Log.d("地图", "循环 ： " + (arrayList.get(i).getMap_name_uuid().equals(mapListDataBean.getSendMapName().get(j).getMap_name_uuid())));
                    if (arrayList.get(i).getMap_name_uuid().equals(mapListDataBean.getSendMapName().get(j).getMap_name_uuid())) {
                        MapListDataChangeBean mapListDataChangeBean = new MapListDataChangeBean();
                        if (!arrayList.get(i).getMap_name().equals(mapListDataBean.getSendMapName().get(j).getMap_name())) {
                            //修改地图名字
                            Log.d("地图", renameMapName + ",修改地图名字为： " + arrayList.get(i).getMap_name()
                                    + ",   " + mapListDataBean.getSendMapName().get(j).getMap_name());
                            mapListDataChangeBean.setType(renameMapName);
                        } else {
                            mapListDataChangeBean.setType("aaa");
                        }
                        index.add(j);
                        MapListDataChangeBean.MapDataBean mapDataBean = new MapListDataChangeBean.MapDataBean();
                        mapDataBean.setPoint(contrastPoint(arrayList.get(i).getPoint(), mapListDataBean.getSendMapName().get(j).getPoint(), mapListDataChangeBean));
                        mapDataBean.setVirtualDataBeans(mapListDataBean.getSendMapName().get(j).getVirtualDataBeans());
                        mapDataBean.setMap_name_uuid(mapListDataBean.getSendMapName().get(j).getMap_name_uuid());
                        mapDataBean.setMap_name(mapListDataBean.getSendMapName().get(j).getMap_name());
                        mapListDataChangeBean.setMapDataBean(mapDataBean);
                        changeBeans.add(mapListDataChangeBean);
                        break;
                    } else if (j == mapListDataBean.getSendMapName().size() - 1) {
                        //删除地图 arrayList.get(i)
                        Log.d("地图", deleteMapName + ",删除地图1111" + arrayList.get(i).getMap_name() + ",    " + arrayList.get(i).getMap_name_uuid());
                        MapListDataChangeBean mapListDataChangeBean = new MapListDataChangeBean();
                        mapListDataChangeBean.setType(deleteMapName);
                        MapListDataChangeBean.MapDataBean mapDataBean = new MapListDataChangeBean.MapDataBean();
                        mapDataBean.setPoint(contrastPoint(arrayList.get(i).getPoint(), mapListDataBean.getSendMapName().get(j).getPoint(), mapListDataChangeBean));
                        mapDataBean.setVirtualDataBeans(mapListDataBean.getSendMapName().get(j).getVirtualDataBeans());
                        mapDataBean.setMap_name_uuid(arrayList.get(i).getMap_name_uuid());
                        mapDataBean.setMap_name(arrayList.get(i).getMap_name());
                        mapListDataChangeBean.setMapDataBean(mapDataBean);
                        changeBeans.add(mapListDataChangeBean);
                    }
                }
            }
            for (int j = 0; j < mapListDataBean.getSendMapName().size(); j++) {
                if (!index.contains(j)) {
                    Log.d("地图", addMapName + ",添加地图" + mapListDataBean.getSendMapName().get(j).getMap_name());
                    MapListDataChangeBean mapListDataChangeBean = new MapListDataChangeBean();
                    mapListDataChangeBean.setType(addMapName);
                    MapListDataChangeBean.MapDataBean mapDataBean = new MapListDataChangeBean.MapDataBean();
                    mapDataBean.setPoint(contrastPoint(null, mapListDataBean.getSendMapName().get(j).getPoint(), mapListDataChangeBean));
                    mapDataBean.setVirtualDataBeans(mapListDataBean.getSendMapName().get(j).getVirtualDataBeans());
                    mapDataBean.setMap_name_uuid(mapListDataBean.getSendMapName().get(j).getMap_name_uuid());
                    mapDataBean.setMap_name(mapListDataBean.getSendMapName().get(j).getMap_name());
                    mapListDataChangeBean.setMapDataBean(mapDataBean);
                    Log.d("地图", addMapName + ",地图对比" + mapListDataChangeBean.toString());
                    changeBeans.add(mapListDataChangeBean);
                }
            }
            index.clear();
        }
        Log.d("地图", addMapName + ",地图对比完成");
        childHandler.sendEmptyMessage(1);
    }

    private ArrayList<ContrastPointDataBean> contrastPoint
            (ArrayList<PointDataBean> pointModel, ArrayList<PointDataBean> point, MapListDataChangeBean
                    bean) {
        ArrayList<ContrastPointDataBean> dataBeans = new ArrayList<>();
        List<Integer> index = new ArrayList<>();
        Log.d("地图22", addPosition + ",删除旧点：" + (pointModel == null));
        if (pointModel != null) {
            for (int i = 0; i < pointModel.size(); i++) {
                for (int j = 0; j < point.size(); j++) {
                    if (pointModel.get(i).getPoint_Name().equals(point.get(j).getPoint_Name())) {
                        Log.d("地图：", "点内容" + pointModel.get(i).toString().equals(point.get(j).toString()));
                        if (!pointModel.get(i).toString().equals(point.get(j).toString())) {//内容不一样，删除之前的点新加
                            ContrastPointDataBean pointDataBean = new ContrastPointDataBean();
                            pointDataBean.setType(addPosition);
                            pointDataBean.setPoint_Name(point.get(j).getPoint_Name());
                            pointDataBean.setPoint_type(point.get(j).getPoint_type());
                            pointDataBean.setPoint_time(point.get(j).getPoint_time());
                            pointDataBean.setPoint_x(point.get(j).getPoint_x());
                            pointDataBean.setPoint_y(point.get(j).getPoint_y());
                            pointDataBean.setAngle(point.get(j).getAngle());
                            dataBeans.add(pointDataBean);
                            ContrastPointDataBean pointDataBean1 = new ContrastPointDataBean();
                            pointDataBean1.setType(deletePosition);
                            pointDataBean.setPoint_Name(pointModel.get(i).getPoint_Name());
                            dataBeans.add(pointDataBean1);
                            Log.d("地图22", addPosition + ",删除旧点：" + pointModel.get(i).getPoint_Name() + ",  添加新点:  " + point.get(j).getPoint_Name());
                        }
                        index.add(j);
                        break;
                    } else if (j == point.size() - 1) {
                        ContrastPointDataBean pointDataBean = new ContrastPointDataBean();
                        pointDataBean.setType(deletePosition);
                        pointDataBean.setPoint_Name(pointModel.get(i).getPoint_Name());
                        dataBeans.add(pointDataBean);
                        Log.d("地图333", deletePosition + ",删除旧点：" + pointModel.get(i).getPoint_Name());
                    }
                }
            }
        } else if (point != null) {
            Log.d("地图", "点数据" + point.size() + ",    " + index.size());
            for (int j = 0; j < point.size(); j++) {
                Log.d("地图", "点数据index ----" + index.contains(j));
                if (index.size() == 0 || !index.contains(j)) {
                    ContrastPointDataBean pointDataBean = new ContrastPointDataBean();
                    pointDataBean.setType(addPosition);
                    Log.d("地图", "点数据name ----" + point.get(j).getPoint_Name());
                    pointDataBean.setPoint_Name(point.get(j).getPoint_Name());
                    Log.d("地图", "点数据type ----" + point.get(j).getPoint_type());
                    pointDataBean.setPoint_type(point.get(j).getPoint_type());
                    Log.d("地图", "点数据time ----" + point.get(j).getPoint_time());
                    pointDataBean.setPoint_time(point.get(j).getPoint_time());
                    Log.d("地图", "点数据x ----" + point.get(j).getPoint_x());
                    pointDataBean.setPoint_x(point.get(j).getPoint_x());
                    Log.d("地图", "点数据y ----" + point.get(j).getPoint_y());
                    pointDataBean.setPoint_y(point.get(j).getPoint_y());
                    Log.d("地图", "点数据angle ----" + point.get(j).getAngle());
                    pointDataBean.setAngle(point.get(j).getAngle());
                    dataBeans.add(pointDataBean);
                    Log.d("地图4444", addPosition + ",添加点：" + point.get(j).getPoint_Name());
                }
                index.clear();
            }
        }
        Log.d("地图","点数据" + dataBeans.size());
        return dataBeans;
    }

    public void uploadMapDump(String s) {
        Message message = childHandler.obtainMessage();
        message.what = 2;
        message.obj = s;
        childHandler.sendMessage(message);
    }

    public void contrastTaskList(SchedulerTaskListBean taskListBean) {
        Log.d("contrastTaskList", taskListBean.toString());
        SchedulerTaskListBean taskListModel = KeepAliveService.taskViewModel.getTaskList();
        ArrayList<Integer> index = new ArrayList<>();
        for (int i = 0; i < taskListModel.getSchedulerTaskBeanList().size(); i++) {
            Log.d("contrastTaskList", taskListModel.getSchedulerTaskBeanList().size()
                    + ",    " + taskListBean.getSchedulerTaskBeanList().size());
            for (int j = 0; j < taskListBean.getSchedulerTaskBeanList().size(); j++) {
                if (taskListModel.getSchedulerTaskBeanList().get(i).toString()
                        .equals(taskListBean.getSchedulerTaskBeanList().get(j).toString())) {
                    Log.d("contrastTaskList", "任务相同");
                    index.add(j);
                    break;
                } else if (j == taskListBean.getSchedulerTaskBeanList().size() - 1) {
                    Log.d("contrastTaskList", "删除任务" + taskListModel.getSchedulerTaskBeanList().get(i).getDbAlarmTime());
                    KeepAliveService.webSocket.send(gsonUtils.deleteTask(
                            Content.DELETETASKQUEUE,
                            taskListModel.getSchedulerTaskBeanList().get(i).getMap_name_uuid(),
                            taskListModel.getSchedulerTaskBeanList().get(i).getMap_name(),
                            taskListModel.getSchedulerTaskBeanList().get(i).getTask_Name()));
                }
            }
        }
        for (int j = 0; j < taskListBean.getSchedulerTaskBeanList().size(); j++) {
            if (!index.contains(j)) {
                String[] split = taskListBean.getSchedulerTaskBeanList().get(j).getDbAlarmCycle().split(",");
                List<String> cycle = new ArrayList<>();
                for (int k = 0; k < split.length; k++) {
                    if (!TextUtils.isEmpty(split[k])) {
                        cycle.add(split[k]);
                    }
                }
                Log.d("contrastTaskList", "添加任务" + taskListBean.getSchedulerTaskBeanList().get(j).getDbAlarmTime());
                Log.d("contrastTaskList", "添加任务" + taskListBean.getSchedulerTaskBeanList().get(j).getPoint().toString());
                String putSaveTaskMsg = gsonUtils.putSaveTaskMsg(taskListBean.getSchedulerTaskBeanList().get(j).getMap_name(),
                        taskListBean.getSchedulerTaskBeanList().get(j).getMap_name_uuid(),
                        taskListBean.getSchedulerTaskBeanList().get(j).getTask_Name(),
                        taskListBean.getSchedulerTaskBeanList().get(j).getDbAlarmTime(),
                        "" + taskListBean.getSchedulerTaskBeanList().get(j).isDbAlarmIsRun(),
                        taskListBean.getSchedulerTaskBeanList().get(j).getPoint(),
                        cycle);
                KeepAliveService.webSocket.send(putSaveTaskMsg);
            }
        }
    }

    public void uploadLog(UploadLogBean uploadLogBean) {
        Message message = childHandler.obtainMessage();
        message.what = 4;
        message.obj = uploadLogBean;
        childHandler.sendMessage(message);
    }

    public void downloadMap(String mapUuid, String md5) {
        Message message = childHandler.obtainMessage();
        message.what = 5;
        Bundle bundle = new Bundle();
        bundle.putString("mapUuid", mapUuid);
        bundle.putString("md5", md5);
        message.setData(bundle);
        childHandler.sendMessage(message);
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        switch (msg.what) {
            case 1:
                checkMapList();
                break;
            case 2:
                FTPManager.getInstance(mContext).connect();
                FTPManager.getInstance(mContext).uploadFile(
                        "/sdcard/robotMap/" + (String) msg.obj + ".tar.gz",
                        "/robotMap/");
                FTPManager.getInstance(mContext).closeFTP();
                break;
            case 3:
                KeepAliveService.webSocket.send(gsonUtils.putRobotMsg(Content.GETMAPLIST, ""));
                break;
            case 4:
                FTPManager.getInstance(mContext).connect();
                UploadLogBean uploadLogBean = (UploadLogBean) msg.obj;
                if (uploadLogBean != null) {
                    File file = new File("/sdcard/robotLogZip/");
                    if (file.exists()) {
                        for (int i = 0; i < file.listFiles().length; i++) {
                            Log.d("文件名字： ", file.listFiles()[i].getName());
                            FTPManager.getInstance(mContext).uploadFile(
                                    "/sdcard/robotLogZip/" + file.listFiles()[i].getName(),
                                    "/robotLog/");
                        }
                        for (int i = 0; i < file.listFiles().length; i++) {
                            file.listFiles()[i].delete();
                        }
                    }
                    FTPManager.getInstance(mContext).closeFTP();

                }
                break;
            case 5:
                Bundle bundle = msg.getData();
                FTPManager.getInstance(mContext).connect();
                FTPManager.getInstance(mContext).downloadFile("/sdcard/robotMap/" + bundle.getString("mapUuid") + ".tar.gz",
                        "/robotMap/" + bundle.getString("mapUuid") + ".tar.gz", bundle.getString("mapUuid") + ".tar.gz");
                FTPManager.getInstance(mContext).closeFTP();
                break;
            default:
                break;
        }

        return false;
    }


}
