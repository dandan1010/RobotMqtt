package com.retron.robotmqtt.bean;

public class HistoryBean {
    private String mapName;
    private String taskName;
    private String time;
    private String date;
    private String pointStatus;

    public HistoryBean(String mapName, String taskName, String time, String date,String pointStatus) {
        this.mapName = mapName;
        this.taskName = taskName;
        this.time = time;
        this.date = date;
        this.pointStatus = pointStatus;
    }

    public String getPointStatus() {
        return pointStatus;
    }

    public void setPointStatus(String pointStatus) {
        this.pointStatus = pointStatus;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
