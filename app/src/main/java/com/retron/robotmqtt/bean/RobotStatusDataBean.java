package com.retron.robotmqtt.bean;

import java.io.Serializable;

public class RobotStatusDataBean implements Serializable {
    private String type;
    private String robot_status;
    private String battery_data;
    private String task_Name;
    private boolean devices_status;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBattery_data() {
        return battery_data;
    }

    public String getRobot_status() {
        return robot_status;
    }

    public void setBattery_data(String battery_data) {
        this.battery_data = battery_data;
    }

    public void setRobot_status(String robot_status) {
        this.robot_status = robot_status;
    }

    public String getTask_Name() {
        return task_Name;
    }

    public void setTask_Name(String task_Name) {
        this.task_Name = task_Name;
    }

    public boolean getDevices_status() {
        return devices_status;
    }

    public void setDevices_status(boolean devices_status) {
        this.devices_status = devices_status;
    }
}
