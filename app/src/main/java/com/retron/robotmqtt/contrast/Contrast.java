package com.retron.robotmqtt.contrast;

import android.util.Log;

import com.retron.robotmqtt.bean.MapListDataBean;
import com.retron.robotmqtt.bean.PointDataBean;
import com.retron.robotmqtt.bean.VirtualDataBean;
import com.retron.robotmqtt.data.MapViewModel;

import java.util.ArrayList;
import java.util.List;

public class Contrast {

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

    public boolean contractVirtual(MapListDataBean mapListDataBean, MapViewModel mapViewModel) {

       // if (mapListDataBean.getSendMapName().get(0).getVirtualDataBeans().toString().equals(mapViewModel.wall.toString()))

        boolean isBreak = false;
        int flagW = 0;
        if (mapViewModel.getMapList() != null) {
            if (mapListDataBean.getSendMapName().size() == mapViewModel.getMapList().getSendMapName().size()) {
                for (int i = 0; i < mapListDataBean.getSendMapName().size(); i++) {
                    //地图名字
                    ArrayList<MapListDataBean.MapDataBean> mSendMapNames = mapViewModel.getMapList().getSendMapName();

                    for (int j = 0; j < mSendMapNames.size(); j++) {
                        if (mapListDataBean.getSendMapName().get(i).getMap_name_uuid()
                                .equals(mSendMapNames.get(j).getMap_name_uuid())) {
                            if (mapListDataBean.getSendMapName().get(i).getMap_name()
                                    .equals(mSendMapNames.get(j).getMap_name())) {
                                //比较虚拟墙
                                ArrayList<ArrayList<VirtualDataBean.Point>> virtualDataBeans = mapListDataBean.getSendMapName().get(i).getVirtualDataBeans().getSend_virtual();
                                ArrayList<ArrayList<VirtualDataBean.Point>> virtualDataBeansModel = mSendMapNames.get(j).getVirtualDataBeans().getSend_virtual();
                                Log.d("contractVirtual", "size---" + virtualDataBeans.toString() +"\n model : " + virtualDataBeansModel.toString());
                                flagW = 0;
                                if (virtualDataBeans.toString().equals(virtualDataBeansModel.toString())) {
                                    Log.d("contractVirtual", "continue");
                                    continue;
                                } else {
                                    Log.d("contractVirtual", "3333 ddddd is different");
                                    return true;
                                }
//                                if (virtualDataBeans.size() == virtualDataBeansModel.size()) {
//                                    for (int q = 0; q < virtualDataBeans.size(); q++) {
//                                        Log.d("contractVirtual", "size---" + virtualDataBeans.size() + ", q : " +  q);
//                                        for (int w = 0; w < virtualDataBeansModel.size(); w++) {
//                                            if (virtualDataBeans.get(q).size() == virtualDataBeansModel.get(w).size()) {
//                                                Log.d("contractVirtual", "size---" + virtualDataBeansModel.get(w).size() + ", w : " +  w);
//                                                for (int qq = 0; qq < virtualDataBeans.get(q).size(); qq++) {
//                                                    Log.d("contractVirtual", "size---" + virtualDataBeans.get(q).size() + ", qq : " +  qq);
//                                                    for (int ww = 0; ww < virtualDataBeansModel.get(w).size(); ww++) {
//                                                        if (virtualDataBeans.get(q).get(qq).getVirtual_x() == virtualDataBeansModel.get(w).get(ww).getVirtual_x()) {
//                                                            if (virtualDataBeans.get(q).get(qq).getVirtual_y() == virtualDataBeansModel.get(w).get(ww).getVirtual_y()) {
//                                                                Log.d("contractVirtual", "break---" + isBreak);
//                                                                break;
//                                                            }
//                                                        }
////                                                        if (ww == virtualDataBeansModel.get(w).size() - 1) {
////                                                            Log.d("contractVirtual", "Virtual ddddd size is different");
////                                                            return true;
////                                                        }
//                                                    }
////                                                    if (virtualDataBeansModel.size() - 1 == w) {
//////                                                    flagW++;
////                                                    break;
////                                                }
//                                                    if (qq == virtualDataBeans.get(q).size() - 1) {
//                                                        Log.d("contractVirtual", "Virtual 1111 size is different");
//                                                        return true;
//                                                    }
//                                                }
////                                                Log.d("contractVirtual", "Virtual www break");
////                                                if (virtualDataBeansModel.size() - 1 == w) {
////                                                    flagW++;
////                                                    break;
////                                                }
//                                            } else {
//                                                Log.d("contractVirtual", "Virtual 2222 size is different");
//                                                return true;
//                                            }
//                                        }
//
//                                    }
//                                } else {
//                                    Log.d("contractVirtual", "4444 size is different");
//                                    return true;
//                                }
                            }
                        }
                    }
                }
            } else {
                Log.d("contractVirtual", "3333 size is different");
                return true;
            }
        } else {
            Log.d("contractVirtual", "5555 size is different");
            return true;
        }
        Log.d("contractVirtual", "false");
        return false;
    }
}
