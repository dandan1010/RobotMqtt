package com.retron.robotmqtt;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.retron.robotmqtt.bean.CurrentPositionDataBean;
import com.retron.robotmqtt.bean.RobotTaskDataBean;
import com.retron.robotmqtt.bean.DownLoadMapBean;
import com.retron.robotmqtt.bean.HistoryTaskDataBean;
import com.retron.robotmqtt.bean.MapListDataBean;
import com.retron.robotmqtt.bean.PointDataBean;
import com.retron.robotmqtt.bean.RobotHealthyBean;
import com.retron.robotmqtt.bean.RobotStatusDataBean;
import com.retron.robotmqtt.bean.RobotTaskErrorBean;
import com.retron.robotmqtt.bean.SchedulerTaskListBean;
import com.retron.robotmqtt.bean.SettingsDataBean;
import com.retron.robotmqtt.bean.VirtualDataBean;
import com.retron.robotmqtt.manager.HandlerThreadManager;
import com.retron.robotmqtt.data.DeviceInfoViewModel;
import com.retron.robotmqtt.data.MapViewModel;
import com.retron.robotmqtt.data.TaskViewModel;
import com.retron.robotmqtt.mqtt.MQTTService;
import com.retron.robotmqtt.mqtt.MqttHandleMsg;
import com.retron.robotmqtt.sqlite.SqLiteOpenHelperUtils;
import com.retron.robotmqtt.utils.AlarmUtils;
import com.retron.robotmqtt.utils.Content;
import com.retron.robotmqtt.utils.GsonUtils;
import com.retron.robotmqtt.utils.Md5Utils;
import com.retron.robotmqtt.utils.SharedPrefUtil;

import org.eclipse.paho.android.service.MqttService;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class KeepAliveService extends LifecycleService {

    private static final String TAG = "KeepAliveService";
    private GsonUtils gsonUtils;
    private Gson gson;
    public static TaskViewModel taskViewModel;
    public static MapViewModel mapViewModel;
    public static DeviceInfoViewModel deviceInfoViewModel;
    private MapListDataBean mMapListDataBean;
    private MapListDataBean currentMapListDataBean;
    private int mapListIndex = 0;
    public static WebSocket webSocket;
    private Context mContext;
    private AlarmUtils mAlarmUtils;
    private boolean reportHealthyTime = false;
    private SqLiteOpenHelperUtils sqLiteOpenHelperUtils;


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        gson = new Gson();
        gsonUtils = new GsonUtils();
        mAlarmUtils = new AlarmUtils(mContext);
        currentMapListDataBean = new MapListDataBean();
        sqLiteOpenHelperUtils = new SqLiteOpenHelperUtils(this);
        Log.d(TAG, "onCreate");
        initModel();
        webSocket = connect();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            //数字是随便写的“40”，
            nm.createNotificationChannel(new NotificationChannel("40", "App Service", NotificationManager.IMPORTANCE_DEFAULT));
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "40");
            //其中的2，是也随便写的，正式项目也是随便写
            startForeground(2, builder.build());
        }
        initObserver();
        gsonUtils = new GsonUtils();

    }

    private void initModel() {
        taskViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(TaskViewModel.class);
        mapViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(MapViewModel.class);
        deviceInfoViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(DeviceInfoViewModel.class);
    }

    @Override
    public int onStartCommand(@NonNull @NotNull Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if ("reconnectMqtt".equals(intent.getAction())) {
            webSocket = connect();
            startService(new Intent(this, MQTTService.class));
        }
        return START_NOT_STICKY;
    }

    public WebSocket connect() {
        Log.d(TAG, "WebSocket connect ");
        OkHttpClient client = new OkHttpClient.Builder().build();
        Request request = new Request.Builder().url("ws://127.0.0.1:8887").build();

        return client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onMessage(@NotNull WebSocket webSocket, @NotNull String message) {
                super.onMessage(webSocket, message);
                Log.d(TAG, "onMessage message is " + message);
                handleMessage(message);
            }

            @Override
            public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
                super.onClosed(webSocket, code, reason);
                Log.d(TAG, "onClosed reason is " + reason);
            }

            @Override
            public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
                super.onMessage(webSocket, bytes);
                Log.d(TAG, "onMessage bytes");
                mapViewModel.setMapBitmap(bytes.asByteBuffer());
            }

            @Override
            public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
                super.onOpen(webSocket, response);
                Log.d(TAG, "onOpen");
                getTaskHistory();
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
                Log.d(TAG, "onClosing");
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, @org.jetbrains.annotations.Nullable Response response) {
                super.onFailure(webSocket, t, response);
                Log.d(TAG, "onFailure " + t.getMessage());
            }
        });
    }

    private void getTaskHistory() {
        if (webSocket != null) {
            try {
                JSONObject jsonObject = new JSONObject();
                long spTime = SharedPrefUtil.getInstance(mContext).getHistoryTime(Content.HISTORY_TIME);
                if (spTime == 0) {
                    jsonObject.put("type", Content.ROBOT_TASK_HISTORY);
                    Log.d(TAG, "HISTORY : " + jsonObject.toString());
                    webSocket.send(jsonObject.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
//        webSocket.send(gsonUtils.putRobotString(Content.GET_LOG_LIST,""));
//        HandlerThreadManager.getInstance(mContext).uploadMapDump("1626761066882");
//        HandlerThreadManager.getInstance(mContext).downloadMap("1625812703718");
    }

    /*public void updateMapDb(MapListDataBean mapList) {
        Cursor cursor = sqLiteOpenHelperUtils.searchAllMapName();
        if (cursor.getCount() >= mapList.getSendMapName().size()) {
            while (cursor.moveToNext()) {
                for (int i = 0; i < mapList.getSendMapName().size(); i++) {
                    if (cursor.getString(cursor.getColumnIndex(Content.MapNameUuid)).equals(mapList.getSendMapName().get(i).getMap_name_uuid())) {
                        File file = new File("/sdcard/robotMap/" + mapList.getSendMapName().get(i).getMap_name_uuid() + ".tar.gz");
                        if (file.exists()) {
                            String md5ByFile = Md5Utils.getMd5ByFile(file);
                            if (!md5ByFile.equals(cursor.getString(cursor.getColumnIndex(Content.MapDumpMd5)))) {
                                //发送zip
                                Log.d(TAG, "发送zip111数据库地图");
                                sqLiteOpenHelperUtils.updateDumpMd5(Content.MapNameUuid, mapList.getSendMapName().get(i).getMap_name_uuid(), md5ByFile);
                                sqLiteOpenHelperUtils.close();
                            } else {
                                break;
                            }

                        }
                    } else if (i == mapList.getSendMapName().size() - 1) {
                        Log.d(TAG, "删除数据库地图");
                        sqLiteOpenHelperUtils.deleteMapName(mapList.getSendMapName().get(i).getMap_name_uuid());
                    }
                }
            }
        } else {
            for (int i = 0; i < mapList.getSendMapName().size(); i++) {
                int flag = 0;
                while (cursor.moveToNext()) {
                    flag ++;
                    if (cursor.getString(cursor.getColumnIndex(Content.MapNameUuid)).equals(mapList.getSendMapName().get(i).getMap_name_uuid())) {
                        File file = new File("/sdcard/robotMap/" + mapList.getSendMapName().get(i).getMap_name_uuid() + ".tar.gz");
                        if (file.exists()) {
                            String md5ByFile = Md5Utils.getMd5ByFile(file);
                            if (!md5ByFile.equals(cursor.getString(cursor.getColumnIndex(Content.MapDumpMd5)))) {
                                //发送zip
                                Log.d(TAG, "发送zip222数据库地图");
                                sqLiteOpenHelperUtils.updateDumpMd5(Content.MapNameUuid, mapList.getSendMapName().get(i).getMap_name_uuid(), md5ByFile);
                                sqLiteOpenHelperUtils.close();
                            } else {
                                break;
                            }
                        }
                    } else if (flag == cursor.getCount()) {
                        Log.d(TAG, "添加数据库地图");
                        File file = new File("/sdcard/robotMap/" + mapList.getSendMapName().get(i).getMap_name_uuid() + ".tar.gz");
                        if (file.exists()) {
                            String md5ByFile = Md5Utils.getMd5ByFile(file);
                            sqLiteOpenHelperUtils.saveMapName(mapList.getSendMapName().get(i).getMap_name_uuid(), md5ByFile);
                        }
                    }
                }
            }
        }
    }*/

    private void handleMessage(String message) {
        JSONObject jsonObject = null;
        switch (gsonUtils.getType(message)) {
            case Content.SENDMAPNAME:
                //地图列表
                MapListDataBean mapList = gson.fromJson(message, MapListDataBean.class);
                if (mapViewModel.getMapList() == null || !mapList.toString().equals(mapViewModel.getMapList().toString())) {
                    Log.d("SENDMAPNAME", "11111" + mapViewModel.getMapList());
                    mMapListDataBean = mapList;
                    Log.d("SENDMAPNAME", "2222" + mapViewModel.getMapList());
                    ArrayList<MapListDataBean.MapDataBean> mapDataBeans = putDataBean(mapList);
                    currentMapListDataBean.setSendMapName(mapDataBeans);
                    currentMapListDataBean.setType(mapList.getType());
                }
                Log.d("SENDMAPNAME", "3333" + mapViewModel.getMapList());
                break;
            case Content.GETPOSITION:
                System.out.println("GETPOSITION: " + message);
                break;
            case Content.TEST_SENSOR_CALLBACK:
                try {
                    jsonObject = new JSONObject(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Content.SENDGPSPOSITION:
                CurrentPositionDataBean currPoint = gson.fromJson(message, CurrentPositionDataBean.class);
                if (deviceInfoViewModel.getCurrentPoint() == null || !currPoint.toString().equals(deviceInfoViewModel.getCurrentPoint().toString())) {
                    deviceInfoViewModel.setCurrentPoint(currPoint);
                }
                break;
            case Content.ROBOT_TASK_HISTORY:
                HistoryTaskDataBean history = gson.fromJson(message, HistoryTaskDataBean.class);
                if (taskViewModel.getHistoryTask() == null || !history.toString().equals(taskViewModel.getHistoryTask().toString())) {
                    taskViewModel.setHistoryTask(history);
                }
                break;
            case Content.ROBOT_TASK_STATE:
                RobotTaskDataBean currTask = gson.fromJson(message, RobotTaskDataBean.class);
                if (taskViewModel.getCurrentTask() == null || !currTask.toString().equals(taskViewModel.getCurrentTask().toString())) {
                    taskViewModel.setCurrentTask(currTask);
                }
                break;
            case Content.GET_ALL_TASK_STATE:
                SchedulerTaskListBean taskListBean = gson.fromJson(message, SchedulerTaskListBean.class);
                if (taskViewModel.getTaskList() == null || !taskListBean.toString().equals(taskViewModel.getTaskList().toString())) {
                    taskViewModel.setTaskList(taskListBean);
                }
                break;
            case Content.STATUS:
                RobotStatusDataBean robotStatus = gson.fromJson(message, RobotStatusDataBean.class);
                if (!Content.scanning_map.equals(Content.robot_statue)) {
                    Content.robot_statue = robotStatus.getRobot_status();
                }
                if (deviceInfoViewModel.getRobotStatus() == null || !robotStatus.toString().equals(deviceInfoViewModel.getRobotStatus().toString())) {
                    deviceInfoViewModel.setRobotStatus(robotStatus);
                }
                break;
            case Content.GET_SETTING:
                Log.d(TAG, "Content.GET_SETTING message is " + message);
                SettingsDataBean settings = gson.fromJson(message, SettingsDataBean.class);
                if (deviceInfoViewModel.getSettings() == null || !settings.toString().equals(deviceInfoViewModel.getSettings().toString())) {
                    deviceInfoViewModel.setSettings(settings);
                }
                break;
            case Content.SEND_MQTT_VIRTUAL:
                VirtualDataBean virtualDataBean = gson.fromJson(message, VirtualDataBean.class);
                for (int i = 0; i < currentMapListDataBean.getSendMapName().size(); i++) {
                    if (virtualDataBean.getMap_name_uuid().equals(currentMapListDataBean.getSendMapName().get(i).getMap_name_uuid())) {
                        currentMapListDataBean.getSendMapName().get(i).setVirtualDataBeans(virtualDataBean);
                        mapListIndex++;
                    }
                }
                if (mapListIndex >= currentMapListDataBean.getSendMapName().size()) {
                    mapListIndex = 0;
                    ArrayList<MapListDataBean.MapDataBean> mapDataBeans = putDataBean(currentMapListDataBean);
                    if (mapViewModel.getMapList() == null || !currentMapListDataBean.toString().equals(mapViewModel.getMapList().toString())) {
                        mMapListDataBean.setSendMapName(mapDataBeans);
                        mapViewModel.setMapList(mMapListDataBean);
//                    } else if (mThreadPool.contractVirtual(currentMapListDataBean, mapViewModel)) {
//                        Log.d("SENDMAPNAME", "1010" + mapViewModel.getMapList());
//                        mMapListDataBean.setSendMapName(mapDataBeans);
//                        mapViewModel.setMapList(mMapListDataBean);
                    }
                }

                break;
            case Content.REQUEST_MSG:
                try {
                    jsonObject = new JSONObject(message);
                    String request_msg = jsonObject.getString(Content.REQUEST_MSG);
                    if (request_msg != null) {
                        if (request_msg.equals("initialize_fail") || request_msg.equals("initialize_success") || request_msg.equals("initializing")) {
                            deviceInfoViewModel.setInitStatus(request_msg);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Content.ROBOT_HEALTHY:
                RobotHealthyBean robotHealthyBean = gson.fromJson(message, RobotHealthyBean.class);
                if (deviceInfoViewModel.getRobotHealthy() == null || robotHealthyBean.toString().equals(deviceInfoViewModel.getRobotHealthy().toString())) {
                    deviceInfoViewModel.setRobotHealthy(robotHealthyBean);
                }
                break;
            case Content.ROBOT_TASK_ERROR:
                RobotTaskErrorBean robotTaskErrorBean = gson.fromJson(message, RobotTaskErrorBean.class);
                taskViewModel.setRobotTaskErrorBean(robotTaskErrorBean);
                break;
            case Content.UPLOADMAPSYN:
                try {
                    jsonObject = new JSONObject(message);
                    String mapNameUuid = jsonObject.getString(Content.MAP_NAME_UUID);
                    JSONObject json = new JSONObject();
                    json.put(Content.MAP_NAME_UUID, mapNameUuid);
                    if ("successed".equals(jsonObject.getString("status"))) {
                        jsonObject.put(Content.result, Content.response_ok);
                        HandlerThreadManager.getInstance(mContext).checkAddMapPoint(mapNameUuid);
                    } else {
                        jsonObject.put(Content.result, Content.response_fail);
                    }
                    MQTTService.publish(gsonUtils.sendRobotMsg(Content.result, jsonObject.toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Content.GET_LOG_LIST:
                Log.d(TAG, "LOG列表： " + gsonUtils.sendRobotMsg(Content.GET_LOG_LIST, message));
                MQTTService.publish(gsonUtils.sendRobotMsg(Content.GET_LOG_LIST, message));
                break;
            case Content.COMPRESSED:
                HandlerThreadManager.getInstance(mContext).uploadLog(MqttHandleMsg.uploadLogBean);
                break;
            case Content.SCANNING_MAP:
                try {
                    jsonObject = new JSONObject(message);
                    if (jsonObject.getBoolean(Content.SCANNING_MAP)) {
                        Content.robot_statue = Content.scanning_map;
                    } else {
                        Content.robot_statue = "";
                    }
                    break;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            default:
                break;
        }
    }

    private ArrayList<MapListDataBean.MapDataBean> putDataBean(MapListDataBean mapListDataBean) {
        ArrayList<MapListDataBean.MapDataBean> mapDataBeans = new ArrayList<>();
        //mapDataBeans.addAll(mapList.getSendMapName());
        for (int i = 0; i < mapListDataBean.getSendMapName().size(); i++) {
            MapListDataBean.MapDataBean mMap = new MapListDataBean.MapDataBean();
            ArrayList<PointDataBean> point = new ArrayList<>();
            point.addAll(mapListDataBean.getSendMapName().get(i).getPoint());
            VirtualDataBean virtualDataBeans = new VirtualDataBean();
            virtualDataBeans = mapListDataBean.getSendMapName().get(i).getVirtualDataBeans();
            DownLoadMapBean downLoadMapBean = null;

            mMap.setMap_name_uuid(mapListDataBean.getSendMapName().get(i).getMap_name_uuid());
            mMap.setMap_name(mapListDataBean.getSendMapName().get(i).getMap_name());
            mMap.setGrid_width(mapListDataBean.getSendMapName().get(i).getGrid_width());
            mMap.setGrid_height(mapListDataBean.getSendMapName().get(i).getGrid_height());
            mMap.setOrigin_x(mapListDataBean.getSendMapName().get(i).getOrigin_x());
            mMap.setOrigin_y(mapListDataBean.getSendMapName().get(i).getOrigin_y());
            mMap.setResolution(mapListDataBean.getSendMapName().get(i).getResolution());
            mMap.setDump_md5(mapListDataBean.getSendMapName().get(i).getDump_md5());
            //mMap.setDump_link(mapListDataBean.getSendMapName().get(i).getDump_link());
            mMap.setVirtualDataBeans(virtualDataBeans);
            mMap.setPoint(point);
            mMap.setDownLoadMapBean(downLoadMapBean);
            mapDataBeans.add(mMap);
        }
        return mapDataBeans;
    }

    private void initObserver() {
        Log.d(TAG, "initObserver");
//设置
        deviceInfoViewModel.settings.observeForever(new Observer<SettingsDataBean>() {
            @Override
            public void onChanged(SettingsDataBean settingsDataBean) {
                Log.d(TAG, "发送setting--mqtt : " + gsonUtils.sendRobotMsg(Content.setting, gson.toJson(settingsDataBean)));
                MQTTService.publish(gsonUtils.sendRobotMsg(Content.setting, gson.toJson(settingsDataBean)));
            }
        });

        deviceInfoViewModel.robotHealthy.observeForever(new Observer<RobotHealthyBean>() {
            @Override
            public void onChanged(RobotHealthyBean robotHealthyBean) {
                if (!reportHealthyTime) {
                    reportHealthyTime = true;
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("type", Content.ROBOT_HEALTHY);
                        jsonObject.put(Content.REPORT_TIME, mAlarmUtils.getTime(System.currentTimeMillis()));
                        JSONObject errorMsg = new JSONObject();
                        String message = robotHealthyBean.getRobot_healthy().replace("{", "").replace("}", "");
                        for (int i = 0; i < message.split(",").length; i++) {
                            if (message.split(",")[i].endsWith("false")
                                    && !message.split(",")[i].split(":")[0].equals("cannotRotate")
                                    && !message.split(",")[i].split(":")[0].equals("localizationLost")) {
                                errorMsg.put(message.split(",")[i].split(":")[0],
                                        message.split(",")[i].split(":")[1]);
                            }
                        }
                        jsonObject.put(Content.ROBOT_HEALTHY, errorMsg);
                        reportHealthyTime = false;
                        Log.d("发送deviceerror", gsonUtils.sendRobotMsg(Content.device_error, jsonObject.toString()));
                        MQTTService.publishTelemetry(gsonUtils.sendRobotMsg(Content.device_error, jsonObject.toString()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        deviceInfoViewModel.currentPoint.observeForever(new Observer<CurrentPositionDataBean>() {
            @Override
            public void onChanged(CurrentPositionDataBean currentPositionDataBean) {
                Log.d("发送currentPoint", gsonUtils.sendRobotMsg(Content.gps_position, gson.toJson(currentPositionDataBean)));
                MQTTService.publishTelemetry(gsonUtils.sendRobotMsg(Content.gps_position, gson.toJson(currentPositionDataBean)));
            }
        });
        deviceInfoViewModel.robotStatus.observeForever(new Observer<RobotStatusDataBean>() {
            @Override
            public void onChanged(RobotStatusDataBean robotStatusDataBean) {
                MQTTService.publishTelemetry(gsonUtils.sendRobotMsg(Content.robotStatus, gson.toJson(robotStatusDataBean)));
            }
        });

        //地图列表
        mapViewModel.mapList.observeForever(new Observer<MapListDataBean>() {
            @Override
            public void onChanged(MapListDataBean mapListDataBean) {
                Log.d(TAG, "发送mapList: " + gsonUtils.sendRobotMsg(Content.maplist, gson.toJson(mapListDataBean)));
                MQTTService.publish(gsonUtils.sendRobotMsg(Content.maplist, gson.toJson(mapListDataBean)));

            }
        });

//任务
        taskViewModel.currentTask.observeForever(new Observer<RobotTaskDataBean>() {
            @Override
            public void onChanged(RobotTaskDataBean currentTaskDataBean) {
                Log.d(TAG, "发送currentTask--mqtt" + gsonUtils.sendRobotMsg(Content.taskStatus, gson.toJson(currentTaskDataBean)));
                MQTTService.publishTelemetry(gsonUtils.sendRobotMsg(Content.taskStatus, gson.toJson(currentTaskDataBean)));

            }
        });
        taskViewModel.taskList.observeForever(new Observer<SchedulerTaskListBean>() {
            @Override
            public void onChanged(SchedulerTaskListBean schedulerTaskListBean) {
                Log.d(TAG, "发送taskList--mqtt" + gsonUtils.sendRobotMsg(Content.tasklist, gson.toJson(schedulerTaskListBean)));
                MQTTService.publish(gsonUtils.sendRobotMsg(Content.tasklist, gson.toJson(schedulerTaskListBean)));
            }
        });
        taskViewModel.taskError.observeForever(new Observer<RobotTaskErrorBean>() {
            @Override
            public void onChanged(RobotTaskErrorBean taskErrorBean) {
                Log.d(TAG, "发送taskError--mqtt" + gsonUtils.sendRobotMsg(Content.task_error, gson.toJson(taskErrorBean)));
                MQTTService.publishTelemetry(gsonUtils.sendRobotMsg(Content.task_error, gson.toJson(taskErrorBean)));
            }
        });
        taskViewModel.history.observeForever(new Observer<HistoryTaskDataBean>() {
            @Override
            public void onChanged(HistoryTaskDataBean historyTaskDataBean) {
                Log.d(TAG, "historyTaskDataBean----" + historyTaskDataBean.toString());
                long time = SharedPrefUtil.getInstance(mContext).getHistoryTime(Content.HISTORY_TIME);
                if (time == 0) {
                    SharedPrefUtil.getInstance(mContext).setHistoryTime(Content.HISTORY_TIME, System.currentTimeMillis());
                    Log.d(TAG, "发送history time = 0--mqtt" + gsonUtils.sendRobotMsg(Content.history, gson.toJson(historyTaskDataBean)));
                    MQTTService.publish(gsonUtils.sendRobotMsg(Content.history, gson.toJson(historyTaskDataBean)));
                } else {
                    ArrayList<HistoryTaskDataBean.RobotHistoryTask> robot_task_history = historyTaskDataBean.getRobot_task_history();
                    for (int i = 0; i < robot_task_history.size(); i++) {
                        long timestamp = mAlarmUtils.stringToTimestamp(robot_task_history.get(i).getDate());
                        Log.d(TAG, "history timestamp----" + timestamp + ",    " + time);
                        if (timestamp >= time) {
                            SharedPrefUtil.getInstance(mContext).setHistoryTime(Content.HISTORY_TIME, System.currentTimeMillis());
                            Log.d(TAG, "发送history--mqtt" + gsonUtils.sendRobotMsg(Content.history, gson.toJson(robot_task_history.get(i))));
                            MQTTService.publish(gsonUtils.sendRobotMsg(Content.history, gson.toJson(robot_task_history.get(i))));
                        }
                    }
                }
            }
        });
    }

}
