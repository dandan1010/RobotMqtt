package com.retron.robotmqtt.bean;

import java.io.Serializable;

public class RobotStatusDataBean implements Serializable {

    /**
     * task_Name :
     * robot_status : Standby
     * map_name : 开户行GG
     * map_name_uuid : 1625130821949
     * emergency_stop : false
     * type : status
     * versionCode : 2
     * battery_data : 62%
     */
    private String task_Name;
    private String robot_status;
    private String map_name;
    private String map_name_uuid;
    private boolean emergency_stop;
    private String type;
    private int versionCode;
    private String battery_data;

    public void setTask_Name(String task_Name) {
        this.task_Name = task_Name;
    }

    public void setRobot_status(String robot_status) {
        this.robot_status = robot_status;
    }

    public void setMap_name(String map_name) {
        this.map_name = map_name;
    }

    public void setMap_name_uuid(String map_name_uuid) {
        this.map_name_uuid = map_name_uuid;
    }

    public void setEmergency_stop(boolean emergency_stop) {
        this.emergency_stop = emergency_stop;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public void setBattery_data(String battery_data) {
        this.battery_data = battery_data;
    }

    public String getTask_Name() {
        return task_Name;
    }

    public String getRobot_status() {
        return robot_status;
    }

    public String getMap_name() {
        return map_name;
    }

    public String getMap_name_uuid() {
        return map_name_uuid;
    }

    public boolean isEmergency_stop() {
        return emergency_stop;
    }

    public String getType() {
        return type;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public String getBattery_data() {
        return battery_data;
    }

    @Override
    public String toString() {
        return "RobotStatusDataBean{" +
                "task_Name='" + task_Name + '\'' +
                ", robot_status='" + robot_status + '\'' +
                ", map_name='" + map_name + '\'' +
                ", map_name_uuid='" + map_name_uuid + '\'' +
                ", emergency_stop=" + emergency_stop +
                ", type='" + type + '\'' +
                ", versionCode=" + versionCode +
                ", battery_data='" + battery_data + '\'' +
                '}';
    }
}
