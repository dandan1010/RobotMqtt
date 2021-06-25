package com.retron.robotmqtt.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class AddPointPositionDataBean implements Serializable {
    private String type;
    private ArrayList<PointDataBean> point;

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setPoint(ArrayList<PointDataBean> point) {
        this.point = point;
    }

    public ArrayList<PointDataBean> getPoint() {
        return point;
    }
}
