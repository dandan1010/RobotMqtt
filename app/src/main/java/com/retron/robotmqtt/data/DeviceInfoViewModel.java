package com.retron.robotmqtt.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.retron.robotmqtt.bean.RobotHealthyBean;
import com.retron.robotmqtt.utils.Content;
import com.retron.robotmqtt.bean.CurrentPositionDataBean;
import com.retron.robotmqtt.bean.RobotStatusDataBean;
import com.retron.robotmqtt.bean.SettingsDataBean;

public class DeviceInfoViewModel  extends AndroidViewModel {

    public MutableLiveData<RobotStatusDataBean> robotStatus = new MutableLiveData<>();
    public MutableLiveData<SettingsDataBean> settings = new MutableLiveData<>();
    public MutableLiveData<String> connected = new MutableLiveData<>(Content.CONNECTING);
    public MutableLiveData<CurrentPositionDataBean> currentPoint = new MutableLiveData<>();
    //status see initialize_fail, initialize_success, initializing in Content.java
    public MutableLiveData<String> initStatus = new MutableLiveData<>();
    public MutableLiveData<RobotHealthyBean> robotHealthy = new MutableLiveData<>();

    public MutableLiveData<Integer> downloadLength = new MutableLiveData<>();

    public RobotHealthyBean getRobotHealthy(){
        return this.robotHealthy.getValue();
    }

    public void setRobotHealthy(RobotHealthyBean robotHealthy){
        this.robotHealthy.postValue(robotHealthy);
    }

    public String getInitStatus(){
        return this.initStatus.getValue();
    }

    public void setInitStatus(String status){
        this.initStatus.postValue(status);
    }

    public CurrentPositionDataBean getCurrentPoint() {
        return currentPoint.getValue();
    }

    public void setCurrentPoint(CurrentPositionDataBean currentPoint) {
        this.currentPoint.postValue(currentPoint);
    }

    /*private MutableLiveData<String> get_charging_mode = new MutableLiveData<>();

    public void setChargingMode(String chargingMode){
        this.get_charging_mode.setValue(chargingMode);
    }

    private LiveData<SettingsDataBean> settings = Transformations.map(get_charging_mode, new Function<String, SettingsDataBean>() {
        @Override
        public SettingsDataBean apply(String input) {
            return setChargingMode(input);
        }
    })*/

    public DeviceInfoViewModel(@NonNull Application application) {
        super(application);
    }

    public void setRobotStatus(RobotStatusDataBean robotStatus) {
        this.robotStatus.postValue(robotStatus);
    }

    public RobotStatusDataBean getRobotStatus(){
        return this.robotStatus.getValue();
    }

    public void setSettings(SettingsDataBean settings) {
        if(!settings.equals(this.settings.getValue())) {
            this.settings.postValue(settings);
        }
    }

    public SettingsDataBean getSettings(){
        return this.settings.getValue();
    }

    public String getConnectStatus(){
        android.util.Log.d("DeviceInfoViewModel", " getConnectStatus " + this.connected.getValue());
        return this.connected.getValue();
        //boolean isConnected = this.connected.getValue().booleanValue();
        //return isConnected;
    }

    public void setConnected(String connected){
        /*android.util.Log.d("DeviceInfoViewModel", " setConnected " + connected);
        if(connected){
            this.connected.postValue(Content.CONN_OK);
        } else {
            this.connected.postValue(Content.CONN_NO);
        }*/
        this.connected.postValue(connected);
    }

    public void setDownloadLength(int downloadLength) {
        this.downloadLength.postValue(downloadLength);
    }

    public int getDownloadLength(){
        return this.downloadLength.getValue();
    }
}
