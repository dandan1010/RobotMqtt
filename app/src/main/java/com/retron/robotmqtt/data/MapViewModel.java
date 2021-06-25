package com.retron.robotmqtt.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.retron.robotmqtt.bean.DownLoadMapBean;
import com.retron.robotmqtt.bean.MapListDataBean;
import com.retron.robotmqtt.bean.PointDataBean;
import com.retron.robotmqtt.bean.SendPointPosition;
import com.retron.robotmqtt.bean.VirtualDataBean;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class MapViewModel extends AndroidViewModel {
    //map, wall, points
    public MutableLiveData<VirtualDataBean> wall = new MutableLiveData<VirtualDataBean>() ;
    public MutableLiveData<SendPointPosition> allPoints = new MutableLiveData<>();
    public MutableLiveData<ByteBuffer> mapBitmap = new MutableLiveData<>();
    public MutableLiveData<MapListDataBean> mapList = new MutableLiveData<>();
    public MutableLiveData<ArrayList<PointDataBean>> currentEditMapPoint = new MutableLiveData<>();
    public MutableLiveData<ArrayList<String>> deleteFailMaps = new MutableLiveData<>();
    public MutableLiveData<DownLoadMapBean> downLoadMap = new MutableLiveData<>();

    public DownLoadMapBean getDownLoadMap() {
        return downLoadMap.getValue();
    }

    public void setDownLoadMap(DownLoadMapBean downLoadMap) {
        this.downLoadMap.postValue(downLoadMap);
    }

    public MapViewModel(@NonNull Application application){
        super(application);
    }
    public ByteBuffer getMapBitmap() {
        return mapBitmap.getValue();
    }

    public SendPointPosition getAllPoints() {
        return allPoints.getValue();
    }

    public VirtualDataBean getWall() {
        return wall.getValue();
    }

    public void setAllPoints(SendPointPosition allPoints) {
        this.allPoints.postValue(allPoints);
    }

    public void setMapBitmap(ByteBuffer mapBitmap) {
        this.mapBitmap.postValue(mapBitmap);
    }

    public void setWall(VirtualDataBean wall) {
        this.wall.postValue(wall);
    }

    public void setMapList(MapListDataBean mapList){
        this.mapList.postValue(mapList);
    }

    public MapListDataBean getMapList(){
        return this.mapList.getValue();
    }

    public ArrayList<PointDataBean> getCurrentEditMapPoint(){
        return this.currentEditMapPoint.getValue();
    }

    public void setCurrentEditMapPoint(ArrayList<PointDataBean> points){
        this.currentEditMapPoint.postValue(points);
    }

    public ArrayList<String> getDeleteFailMaps() {
        return deleteFailMaps.getValue();
    }

    public void setDeleteFailMaps(ArrayList<String> maps){
        deleteFailMaps.postValue(maps);
    }
}
