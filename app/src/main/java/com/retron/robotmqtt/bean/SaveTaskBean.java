package com.retron.robotmqtt.bean;

public class SaveTaskBean {
    private String point_Name;
    private int point_time;

    public SaveTaskBean(){

    }

    public SaveTaskBean(String point_Name, int point_time){
        this.point_Name = point_Name;
        this.point_time = point_time;
    }

    public String getPoint_Name() {
        return point_Name;
    }

    public void setPoint_Name(String point_Name) {
        this.point_Name = point_Name;
    }

    public int getPoint_time() {
        return point_time;
    }

    public void setPoint_time(int point_time) {
        this.point_time = point_time;
    }

    @Override
    public String toString() {
        return "SaveTaskBean{" +
                "point_Name='" + point_Name + '\'' +
                ", point_time=" + point_time +
                '}';
    }
}
