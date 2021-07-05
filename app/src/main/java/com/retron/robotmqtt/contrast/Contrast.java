package com.retron.robotmqtt.contrast;

import android.util.Log;

import com.retron.robotmqtt.KeepAliveService;
import com.retron.robotmqtt.bean.MapListDataBean;
import com.retron.robotmqtt.bean.MapListDataChangeBean;
import com.retron.robotmqtt.bean.PointDataBean;
import com.retron.robotmqtt.bean.VirtualDataBean;
import com.retron.robotmqtt.data.MapViewModel;
import com.retron.robotmqtt.utils.Content;
import com.retron.robotmqtt.utils.GsonUtils;

import java.util.ArrayList;
import java.util.List;

public class Contrast {

    public static ArrayList<MapListDataChangeBean> beanArrayList = new ArrayList<>();
    public boolean contrastMapList(MapListDataBean mapListDataBean, MapViewModel mapViewModel) {
        Log.d("contrastMapList", "contrastMapList");
        //Log.d("contrastMapList", ""+ mapListDataBean.getSendMapName().size() + ",   " + mapViewModel.getMapList().getSendMapName().size());
        if (mapViewModel.getMapList() != null) {
            Log.d("contrastMapList", mapViewModel.getMapList().toString());
            Log.d("contrastMapList", mapViewModel.getMapList().getSendMapName().toString());
            if (mapListDataBean.getSendMapName().size() == mapViewModel.getMapList().getSendMapName().size()) {
                Log.d("contrastMapList", "11111");
                for (int i = 0; i < mapListDataBean.getSendMapName().size(); i++) {
                    //地图名字
                    ArrayList<MapListDataBean.MapDataBean> mSendMapNames = mapViewModel.getMapList().getSendMapName();
                    for (int j = 0; j < mSendMapNames.size(); j++) {
                        if (mapListDataBean.getSendMapName().get(i).getMap_name_uuid()
                                .equals(mSendMapNames.get(j).getMap_name_uuid())) {
                            if (mapListDataBean.getSendMapName().get(i).getMap_name()
                                    .equals(mSendMapNames.get(j).getMap_name())) {
                                //比较点
                                ArrayList<PointDataBean> pointDataBeans = mapListDataBean.getSendMapName().get(i).getPoint();
                                ArrayList<PointDataBean> pointDataBeansModel = mSendMapNames.get(j).getPoint();
                                if (pointDataBeans.size() == pointDataBeansModel.size()) {
                                    for (int k = 0; k < pointDataBeans.size(); k++) {
                                        for (int m = 0; m < pointDataBeansModel.size(); m++) {
                                            if (pointDataBeans.get(k).getPoint_Name().equals(pointDataBeansModel.get(m).getPoint_Name())) {
                                                if (pointDataBeans.get(k).getPoint_x() == pointDataBeansModel.get(m).getPoint_x()) {
                                                    if (pointDataBeans.get(k).getPoint_y() == pointDataBeansModel.get(m).getPoint_y()) {
                                                        if (pointDataBeans.get(k).getPoint_type() == pointDataBeansModel.get(m).getPoint_type()) {
                                                            if (pointDataBeans.get(k).getPoint_time() == pointDataBeansModel.get(m).getPoint_time()) {
                                                                if (pointDataBeans.get(k).getAngle() == pointDataBeansModel.get(m).getAngle()) {
                                                                    Log.d("contrastMapList", "continue");
                                                                    continue;
                                                                } else {
                                                                    Log.d("contrastMapList", "Point_Angle is different !");
                                                                    return true;
                                                                }
                                                            } else {
                                                                Log.d("contrastMapList", "Point_time is different !");
                                                                return true;
                                                            }
                                                        } else {
                                                            Log.d("contrastMapList", "Point_type is different !");
                                                            return true;
                                                        }
                                                    } else {
                                                        Log.d("contrastMapList", "Point_y is different !");
                                                        return true;
                                                    }
                                                } else {
                                                    Log.d("contrastMapList", "Point_x is different !");
                                                    return true;
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    Log.d("contrastMapList", "3333 size is different");
                                    return true;
                                }

                            }
                        }
                    }
                }
            } else {
                Log.d("contrastMapList", "3333 size is different");
                return true;
            }
        } else {
            Log.d("contrastMapList", "5555 size is different");
            return true;
        }
        Log.d("contrastMapList", "FALSE ");
        return false;
    }


    public void contrastMapList(MapListDataBean mapListDataBean, GsonUtils gsonUtils) {
        ArrayList<MapListDataChangeBean> changeBeans = new ArrayList<>();
        if (mapListDataBean.getSendMapName().size() == 0
                && KeepAliveService.mapViewModel != null
                && KeepAliveService.mapViewModel.getMapList() != null
                && KeepAliveService.mapViewModel.getMapList().getSendMapName().size() != 0) {
            //无地图，删除所有地图
            ArrayList<String> deleteMapName = new ArrayList<>();
            for (int i = 0; i < KeepAliveService.mapViewModel.getMapList().getSendMapName().size(); i++) {
                deleteMapName.add(KeepAliveService.mapViewModel.getMapList().getSendMapName().get(i).getMap_name_uuid());
            }
            KeepAliveService.webSocket.send(gsonUtils.sendRobotMsg(Content.DELETE_MAP, deleteMapName));
        } else if (KeepAliveService.mapViewModel != null
                && KeepAliveService.mapViewModel.getMapList() != null
                && KeepAliveService.mapViewModel.getMapList().getSendMapName().size() != 0) {
            ArrayList<MapListDataBean.MapDataBean> arrayList = KeepAliveService.mapViewModel.getMapList().getSendMapName();
            if (arrayList.size() >= mapListDataBean.getSendMapName().size()) {
                for (int i = 0; i < arrayList.size(); i++) {
                    for (int j = 0; j < mapListDataBean.getSendMapName().size(); j++) {
                        if (arrayList.get(i).getMap_name_uuid().equals(mapListDataBean.getSendMapName().get(j).getMap_name_uuid())) {
                            if (!arrayList.get(i).getMap_name().equals(mapListDataBean.getSendMapName().get(j).getMap_name())) {
                                //修改地图名字
                                Log.d("地图", "修改地图名字为： " + mapListDataBean.getSendMapName().get(j).getMap_name());
                                MapListDataChangeBean mapListDataChangeBean = new MapListDataChangeBean();
                                mapListDataChangeBean.setType("renameMapName");
                                //contrastPoint(arrayList.get(i).getPoint(), mapListDataBean.getSendMapName().get(j).getPoint(), mapListDataChangeBean);




                            }
                        } else if (j == mapListDataBean.getSendMapName().size() - 1) {
                            //删除地图 arrayList.get(i)
                            Log.d("地图", "删除地图" + arrayList.get(i).getMap_name());
                        }
                    }

                }
            } else {
                for (int j = 0; j < mapListDataBean.getSendMapName().size(); j++) {
                    for (int i = 0; i < arrayList.size(); i++) {
                        if (arrayList.get(i).getMap_name_uuid().equals(mapListDataBean.getSendMapName().get(j).getMap_name_uuid())) {
                            if (arrayList.get(i).getMap_name().equals(mapListDataBean.getSendMapName().get(j).getMap_name())) {

                            } else {
                                //修改地图名字
                                Log.d("地图11", "修改地图名字为： " + mapListDataBean.getSendMapName().get(j).getMap_name());
                            }
                            //contrastPoint(arrayList.get(i).getPoint(), mapListDataBean.getSendMapName().get(j).getPoint());
                        } else if (i == arrayList.size() - 1) {
                            //删除地图 arrayList.get(i)
                            Log.d("地图11", "添加地图" + arrayList.get(i).getMap_name());
                        }
                    }

                }
            }
        }
    }

   /* private ArrayList<PointDataBean> contrastPoint(ArrayList<PointDataBean> pointModel, ArrayList<PointDataBean> point, MapListDataChangeBean bean) {
        ArrayList<PointDataBean> dataBeans = new ArrayList<>();
        if (pointModel.size() >= point.size()) {//删除点
            for (int i = 0;i<pointModel.size();i++) {
                for (int j = 0; j < point.size(); j++) {
                    if (pointModel.get(i).getPoint_Name().equals(point.get(j).getPoint_Name())) {
                        if (!pointModel.get(i).toString().equals(point.get(j).toString())) {//内容不一样，删除之前的点新加
                            PointDataBean pointDataBean = new PointDataBean();
                            pointDataBean.setPoint_Name(point.get(j).getPoint_Name());
                            pointDataBean.setPoint_type(point.get(j).getPoint_type());
                            pointDataBean.setPoint_time(point.get(j).getPoint_time());
                            pointDataBean.setPoint_x(point.get(j).getPoint_x());
                            pointDataBean.setPoint_y(point.get(j).getPoint_y());
                            pointDataBean.setAngle(point.get(j).getAngle());
                            dataBeans.add(pointDataBean);
                        }
                    }
                    if (j == point.size() - 1 ) {
                        Log.d("地图22", "删除点：" + pointModel.get(i).getPoint_Name());
                        //bean.set
                    }
                }
                //bean.setSendMapName(dataBeans);
            }
        } else {//添加点

        }

    }
*/
}
