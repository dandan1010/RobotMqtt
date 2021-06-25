package com.retron.robotmqtt.bean;

import java.io.Serializable;

public class PointDataBean implements Serializable {
    private String point_Name;
    private int point_x;
    private int point_y;
    private double angle;
    private int point_type;//0:init point 1:charging point 2:distinct point(navigation point)
    private int point_time;

    public void setPoint_Name(String point_Name) {
        this.point_Name = point_Name;
    }

    public String getPoint_Name() {
        return point_Name;
    }

    public int getPoint_x() {
        return point_x;
    }

    public void setPoint_x(int point_x) {
        this.point_x = point_x;
    }

    public int getPoint_y() {
        return point_y;
    }

    public void setPoint_y(int point_y) {
        this.point_y = point_y;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public int getPoint_type() {
        return point_type;
    }

    public void setPoint_type(int point_type) {
        this.point_type = point_type;
    }

    public int getPoint_time() {
        return point_time;
    }

    public void setPoint_time(int point_time) {
        this.point_time = point_time;
    }


    @Override
    public String toString() {
        return "{ PointDataBean " +
                ", point_Name is " + point_Name +
                ", point_x is " + point_x +
                ", point_y is " + point_y +
                ", angle is " + angle +
                ", point_type " + point_type +
                ", point_time " + point_time +
                " }";
    }
}
