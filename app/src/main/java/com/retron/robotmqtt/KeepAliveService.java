package com.retron.robotmqtt;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.retron.robotmqtt.bean.AddPointPositionDataBean;
import com.retron.robotmqtt.bean.CurrentPositionDataBean;
import com.retron.robotmqtt.bean.CurrentTaskDataBean;
import com.retron.robotmqtt.bean.DownLoadMapBean;
import com.retron.robotmqtt.bean.HistoryBean;
import com.retron.robotmqtt.bean.HistoryTaskDataBean;
import com.retron.robotmqtt.bean.MapListDataBean;
import com.retron.robotmqtt.bean.PointDataBean;
import com.retron.robotmqtt.bean.RobotHealthyBean;
import com.retron.robotmqtt.bean.RobotStatusDataBean;
import com.retron.robotmqtt.bean.RobotTaskErrorBean;
import com.retron.robotmqtt.bean.SchedulerTaskListBean;
import com.retron.robotmqtt.bean.SendPointPosition;
import com.retron.robotmqtt.bean.SettingsDataBean;
import com.retron.robotmqtt.bean.VirtualDataBean;
import com.retron.robotmqtt.contrast.Contrast;
import com.retron.robotmqtt.data.DeviceInfoViewModel;
import com.retron.robotmqtt.data.MapViewModel;
import com.retron.robotmqtt.data.TaskViewModel;
import com.retron.robotmqtt.mqtt.MQTTService;
import com.retron.robotmqtt.utils.AlarmUtils;
import com.retron.robotmqtt.utils.Content;
import com.retron.robotmqtt.utils.GsonUtils;
import com.retron.robotmqtt.utils.SharedPrefUtil;

import org.eclipse.paho.android.service.MqttService;
import org.jetbrains.annotations.NotNull;
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
    private JSONObject jsonObject;
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
    private Contrast mContrast;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        gson = new Gson();
        mContrast = new Contrast();
        gsonUtils = new GsonUtils();
        mAlarmUtils = new AlarmUtils(mContext);
        currentMapListDataBean = new MapListDataBean();
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

    }

    private void handleMessage(String message) {
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
                    //mapViewModel.setMapList(mMapListDataBean);
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
//            case Content.SENDPOINTPOSITION:
//                //当前地图点列表
//                SendPointPosition points = gson.fromJson(message, SendPointPosition.class);
//                mapViewModel.setAllPoints(points);
//                break;
            case Content.SENDGPSPOSITION:
                CurrentPositionDataBean currPoint = gson.fromJson(message, CurrentPositionDataBean.class);
                Log.d(TAG, "SENDGPSPOSITION point is " + currPoint);
                if (deviceInfoViewModel.getCurrentPoint() == null || !currPoint.toString().equals(deviceInfoViewModel.getCurrentPoint().toString())) {
                    deviceInfoViewModel.setCurrentPoint(currPoint);
                }
                break;
            case Content.ROBOT_TASK_HISTORY:
                HistoryTaskDataBean history = gson.fromJson(message, HistoryTaskDataBean.class);
                if (taskViewModel.getHistoryTask() == null || !history.toString().equals(taskViewModel.getHistoryTask().toString())) {
                    taskViewModel.setHistoryTask(history);
                    //年月日 时分秒
                }
                break;
            case Content.ROBOT_TASK_STATE:
                CurrentTaskDataBean currTask = gson.fromJson(message, CurrentTaskDataBean.class);
                if (taskViewModel.getCurrentTask() == null && !currTask.toString().equals(taskViewModel.getCurrentTask().toString())) {
                    taskViewModel.setCurrentTask(currTask);
                }
                break;
            case Content.GET_ALL_TASK_STATE:
                SchedulerTaskListBean taskListBean = gson.fromJson(message, SchedulerTaskListBean.class);
                if (taskViewModel.getTaskList() == null || !taskListBean.toString().equals(taskViewModel.getTaskList().toString())) {
                    taskViewModel.setTaskList(taskListBean);
                }
                break;
            case Content.UPDATE:
                //
                break;
            case Content.STATUS:
                Log.d(TAG, "Content.STATUS message is " + message);
                RobotStatusDataBean robotStatus = gson.fromJson(message, RobotStatusDataBean.class);
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
            case Content.UPDATE_FILE_LENGTH:
                try {
                    jsonObject = new JSONObject(message);
                    int downloadLength = jsonObject.getInt(Content.UPDATE_FILE_LENGTH);
                    deviceInfoViewModel.setDownloadLength(downloadLength);
                } catch (JSONException e) {
                    e.printStackTrace();
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
                        Log.d("SENDMAPNAME", "8888" + mapViewModel.getMapList());
                        mMapListDataBean.setSendMapName(mapDataBeans);
                        mapViewModel.setMapList(mMapListDataBean);
//                    } else if (mContrast.contractVirtual(currentMapListDataBean, mapViewModel)) {
//                        Log.d("SENDMAPNAME", "1010" + mapViewModel.getMapList());
//                        mMapListDataBean.setSendMapName(mapDataBeans);
//                        mapViewModel.setMapList(mMapListDataBean);
                    }
                    Log.d("SENDMAPNAME", "1212" + mapViewModel.getMapList());
                    Log.d(TAG, "mapViewModel.wall: " + currentMapListDataBean.toString() + ", \nmodel " + mapViewModel.getMapList().toString());
                }

                break;
            case Content.REQUEST_MSG:
                try {
                    jsonObject = new JSONObject(message);
                    String request_msg = jsonObject.getString(Content.REQUEST_MSG);
                    if (request_msg != null) {
                        Log.d(TAG, "handleMessage: " + message);
                        if (request_msg.equals("initialize_fail") || request_msg.equals("initialize_success") || request_msg.equals("initializing")) {
                            deviceInfoViewModel.setInitStatus(request_msg);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
//            case Content.DOWNLOAD_MAP:
//                DownLoadMapBean downLoadMapBean = gson.fromJson(message, DownLoadMapBean.class);
//                mapViewModel.setDownLoadMap(downLoadMapBean);
//                break;
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
            default:
                Log.e(TAG, "NO USER MESSAGE " + message);
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
                Log.d(TAG, "发送setting--mqtt");
            }
        });

        deviceInfoViewModel.robotHealthy.observeForever(new Observer<RobotHealthyBean>() {
            @Override
            public void onChanged(RobotHealthyBean robotHealthyBean) {
                if (!reportHealthyTime) {
                    reportHealthyTime = true;
                    boolean hasErrorMsg = false;
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("type", Content.ROBOT_HEALTHY);
                        jsonObject.put(Content.REPORT_TIME, mAlarmUtils.getTime(System.currentTimeMillis()));
                        JSONObject errorMsg = new JSONObject();
                        String message = robotHealthyBean.getRobot_healthy().replace("{", "").replace("}", "");
                        for (int i = 0; i < message.split(",").length; i++) {
                            Log.d(TAG, "ROBOTTHREAD22 : " + message.split(",")[i]);
                            if (message.split(",")[i].endsWith("false")
                                    && !message.split(",")[i].split(":")[0].equals("cannotRotate")
                                    && !message.split(",")[i].split(":")[0].equals("localizationLost")) {
                                errorMsg.put(message.split(",")[i].split(":")[0],
                                        message.split(",")[i].split(":")[1]);
                                hasErrorMsg = true;
                            }
                        }
                        jsonObject.put(Content.ROBOT_HEALTHY, errorMsg);
                        reportHealthyTime = false;
                        if (hasErrorMsg) {
                            Log.d(TAG, "发送robotHealthy--mqtt : " + jsonObject.toString());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        deviceInfoViewModel.currentPoint.observeForever(new Observer<CurrentPositionDataBean>() {
            @Override
            public void onChanged(CurrentPositionDataBean currentPositionDataBean) {
                Log.d(TAG, "currentPoint: " + currentPositionDataBean.toString());
                Log.d(TAG, "发送currentPoint--mqtt");
                String msg = "{\"type\":\"ping\",\"address\":\"10.7.5.135\"}";
                System.out.print("数据： " + msg + "\n");
                    MQTTService.publish(msg);


            }
        });

//地图列表
        mapViewModel.mapList.observeForever(new Observer<MapListDataBean>() {
            @Override
            public void onChanged(MapListDataBean mapListDataBean) {
                Log.d(TAG, "mapList: " + mMapListDataBean.getSendMapName().toString());
                Log.d(TAG, "发送mapList--mqtt");
            }
        });


//地图图片文件
        mapViewModel.downLoadMap.observeForever(new Observer<DownLoadMapBean>() {
            @Override
            public void onChanged(DownLoadMapBean downLoadMapBean) {
                Log.d(TAG, "onChanged downLoadMap.wall: " + downLoadMapBean.toString());
                for (int i = 0; i < mMapListDataBean.getSendMapName().size(); i++) {
                    if (downLoadMapBean.getMap_Name_uuid().equals(mMapListDataBean.getSendMapName().get(i).getMap_name_uuid())) {
                        mMapListDataBean.getSendMapName().get(i).setDownLoadMapBean(downLoadMapBean);
                    }
                }
                Log.d(TAG, "downLoadMap.wall: " + mMapListDataBean.getSendMapName().toString());
            }
        });

//任务
        taskViewModel.currentTask.observeForever(new Observer<CurrentTaskDataBean>() {
            @Override
            public void onChanged(CurrentTaskDataBean currentTaskDataBean) {
                Log.d(TAG, "发送currentTask--mqtt");
            }
        });
        taskViewModel.taskList.observeForever(new Observer<SchedulerTaskListBean>() {
            @Override
            public void onChanged(SchedulerTaskListBean schedulerTaskListBean) {
                Log.d(TAG, "发送taskList--mqtt");
            }
        });
        taskViewModel.taskError.observeForever(new Observer<RobotTaskErrorBean>() {
            @Override
            public void onChanged(RobotTaskErrorBean taskErrorBean) {
                Log.d(TAG, "发送taskError--mqtt");
            }
        });
        taskViewModel.history.observeForever(new Observer<HistoryTaskDataBean>() {
            @Override
            public void onChanged(HistoryTaskDataBean historyTaskDataBean) {
                long time = SharedPrefUtil.getInstance(mContext).getHistoryTime(Content.HISTORY_TIME);
                if (time == 0) {
                    SharedPrefUtil.getInstance(mContext).setHistoryTime(Content.HISTORY_TIME, System.currentTimeMillis());
                    Log.d(TAG, "发送history time = 0--mqtt" + historyTaskDataBean.toString());
                } else {
                    ArrayList<HistoryTaskDataBean.RobotHistoryTask> robot_task_history = historyTaskDataBean.getRobot_task_history();
                    for (int i = 0; i < robot_task_history.size(); i++) {
                        long timestamp = mAlarmUtils.stringToTimestamp(robot_task_history.get(i).getDate());
                        if (timestamp >= time) {
                            SharedPrefUtil.getInstance(mContext).setHistoryTime(Content.HISTORY_TIME, System.currentTimeMillis());
                            Log.d(TAG, "发送history--mqtt" + robot_task_history.get(i).toString());
                        }
                    }
                }
            }
        });
    }

}
