package com.retron.robotmqtt.manager;

import android.content.Context;
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
import com.retron.robotmqtt.mqtt.MQTTService;
import com.retron.robotmqtt.utils.Content;
import com.retron.robotmqtt.utils.GsonUtils;
import com.retron.robotmqtt.utils.Md5Utils;
import com.retron.robotmqtt.utils.SharedPrefUtil;

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
//                        case addMapName:
//                            downloadUrl(changeBeans.get(i).getMapDataBean().getMap_name_uuid(),
//                                    changeBeans.get(i).getMapDataBean().getDump_link(),
//                                    changeBeans.get(i).getMapDataBean().getDump_md5());
//                            break;
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
                                KeepAliveService.webSocket.send(gsonUtils.sendRobotMsg(
                                        Content.MQTT_ADD_POINT,
                                        changeBeans.get(i).getMapDataBean().getMap_name_uuid(),
                                        point.get(j)));
                                break;
                            default:
                                break;
                        }
                    }
                    changeBeans.remove(i);
                }
            }
        }
    }

    public void downloadUrl(String mapNameUuid, String urlPath, String dumpMd5) {
        URL url = null;
        String dumpPath = "/sdcard/robotMap/" + mapNameUuid + ".tar.gz";
        try {
            url = new URL(urlPath);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            BufferedInputStream bis = new BufferedInputStream(urlConn.getInputStream());
            FileOutputStream fos = new FileOutputStream(dumpPath);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            byte[] buf = new byte[3 * 1024];
            int result = bis.read(buf);
            while (result != -1) {
                bos.write(buf, 0, result);
                result = bis.read(buf);
            }
            bos.flush();
            bis.close();
            fos.close();
            bos.close();
//            String savePath = "/sdcard/robotMap/" + mapNameUuid + ".tar.gz";
//            FileUtils.decoderBase64File(base64Code, savePath);
            if (dumpMd5.equals(Md5Utils.getMd5ByFile(new File(dumpMd5)))) {
                MQTTService.publish(gsonUtils.sendRobotMsg(Content.download_map_dump, "OK"));
                KeepAliveService.webSocket.send(gsonUtils.sendRobotMsg(Content.UPLOAD_MAP, changeBeans));
            } else {
                Log.d("下载地图失败： ", Md5Utils.getMd5ByFile(new File(dumpMd5)) + ",    " + dumpMd5);
                File file = new File(dumpPath);
                if (file.isFile() && file.exists()) {
                    file.delete();
                }
                MQTTService.publish(gsonUtils.sendRobotMsg(Content.download_map_dump, "fail"));
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
                        changeBeans.add(mapListDataChangeBean);
                    }
                }
                index.clear();
            }
        childHandler.sendEmptyMessage(1);
    }

    private ArrayList<ContrastPointDataBean> contrastPoint
            (ArrayList<PointDataBean> pointModel, ArrayList<PointDataBean> point, MapListDataChangeBean
                    bean) {
        ArrayList<ContrastPointDataBean> dataBeans = new ArrayList<>();
        List<Integer> index = new ArrayList<>();
        Log.d("地图22", addPosition + ",删除旧点：" + (pointModel == null)
                + ",  " + pointModel.size()
                + ",    " + point.size());
        if (pointModel == null || pointModel.size() == 0) {
            for (int j = 0; j < point.size(); j++) {
                Log.d("地图22", "进入循环----" + j);
                ContrastPointDataBean pointDataBean = new ContrastPointDataBean();
                pointDataBean.setType(addPosition);
                pointDataBean.setPoint_Name(point.get(j).getPoint_Name());
                pointDataBean.setPoint_type(point.get(j).getPoint_type());
                pointDataBean.setPoint_time(point.get(j).getPoint_time());
                pointDataBean.setPoint_x(point.get(j).getPoint_x());
                pointDataBean.setPoint_y(point.get(j).getPoint_y());
                pointDataBean.setAngle(point.get(j).getAngle());
                dataBeans.add(pointDataBean);
                Log.d("地图 4444", addPosition + ",添加点：" + point.get(j).getPoint_Name());
            }
        } else /*if (pointModel.size() >= point.size())*/ {//删除点
            for (int i = 0; i < pointModel.size(); i++) {
                for (int j = 0; j < point.size(); j++) {
                    if (pointModel.get(i).getPoint_Name().equals(point.get(j).getPoint_Name())) {
                        Log.d("地图：" , "点内容" + pointModel.get(i).toString().equals(point.get(j).toString()));
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
            for (int j = 0; j < point.size(); j++) {
                if (!index.contains(j)) {
                    ContrastPointDataBean pointDataBean = new ContrastPointDataBean();
                    pointDataBean.setType(addPosition);
                    pointDataBean.setPoint_Name(point.get(j).getPoint_Name());
                    pointDataBean.setPoint_type(point.get(j).getPoint_type());
                    pointDataBean.setPoint_time(point.get(j).getPoint_time());
                    pointDataBean.setPoint_x(point.get(j).getPoint_x());
                    pointDataBean.setPoint_y(point.get(j).getPoint_y());
                    pointDataBean.setAngle(point.get(j).getAngle());
                    dataBeans.add(pointDataBean);
                    Log.d("地图4444", addPosition + ",添加点：" + pointModel.get(j).getPoint_Name());
                }
            }
            index.clear();
        }
        return dataBeans;
    }

    public void uploadMapDump(String s) {
        Message message = childHandler.obtainMessage();
        message.what = 2;
        message.obj = s;
        childHandler.sendMessage(message);
    }

    private void uploadFile(String uploadFile) {
        Log.d("uploadFile", "uploadFile");
        String end = "/r/n";

        String Hyphens = "--";

        String boundary = "*****";
//
        try {
//
            URL url = new URL("ftp://58.240.254.188:10021");
//
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            /* 允许Input、Output，不使用Cache */

            con.setDoInput(true);
            con.setRequestProperty("Username", "njdc");
            con.setRequestProperty("Password", "njdc");
            con.setDoOutput(true);
            con.setUseCaches(false);

            /* 设定传送的method=POST */

            con.setRequestMethod("POST");

            /* setRequestProperty */

            con.setRequestProperty("Connection", "Keep-Alive");

            con.setRequestProperty("Charset", "UTF-8");

//            con.setRequestProperty("Content-Type",
//
//                    "multipart/form-data;boundary=" + boundary);

            /* 设定DataOutputStream */

            DataOutputStream ds = new DataOutputStream(con.getOutputStream());
//            String data = "username=njdc" + "&password=njdc";
//            ds.write(data.getBytes());
//            ds.flush();
//            ds.writeBytes(Hyphens + boundary + end);
//
//            ds.writeBytes("Content-Disposition: form-data; "
//
//                    + "name=/" file1 / ";filename=/"" + newName + " / "" + end);
//
//            ds.writeBytes(end);
            /* 取得文件的FileInputStream */
//            FileInputStream fStream = new FileInputStream(uploadFile);
//            /* 设定每次写入1024bytes */
//            int bufferSize = 1024;
//            byte[] buffer = new byte[bufferSize];
//            int length = -1;
//            /* 从文件读取数据到缓冲区 */
//            while ((length = fStream.read(buffer)) != -1) {
//                /* 将数据写入DataOutputStream中 */
//                ds.write(buffer, 0, length);
//            }
//            //ds.writeBytes(end);
//            //ds.writeBytes(Hyphens + boundary + Hyphens + end);
//            fStream.close();
//            ds.flush();
//            /* 取得Response内容 */
//            InputStream is = con.getInputStream();
//            int ch;
//            StringBuffer b = new StringBuffer();
//            while ((ch = is.read()) != -1) {
//                b.append((char) ch);
//            }
//            System.out.println("上传成功");
//            Toast.makeText(mContext, "上传成功", Toast.LENGTH_LONG).show();
//            ds.close();
        } catch (Exception e) {
            Log.d("uploadFile", "上传失败" + e.getMessage());

        }
    }


    public void contrastTaskList(SchedulerTaskListBean taskListBean) {
        SchedulerTaskListBean taskListModel = KeepAliveService.taskViewModel.getTaskList();

    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        switch (msg.what) {
            case 1:
                checkMapList();
                break;
            case 2:
//                uploadFile("/sdcard/robotMap/" + (String) msg.obj + ".tar.gz");
//                FTPManager.getInstance(mContext).connect();
//                FTPManager.getInstance(mContext).uploadFile(
//                        "/sdcard/robotMap/" + (String) msg.obj + ".tar.gz",
//                        "ftp://58.240.254.188:10021/robotMap");
//                FTPManager.getInstance(mContext).closeFTP();
                break;
            case 3:
                KeepAliveService.webSocket.send(gsonUtils.putRobotMsg(Content.GETMAPLIST, ""));
                break;
            default:
                break;

        }

        return false;
    }


}
