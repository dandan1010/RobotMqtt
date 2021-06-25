package com.retron.robotmqtt.bean;

import java.io.Serializable;

public class CurrentPositionDataBean implements Serializable {
    /*{"type":"sendGpsPosition","robot_x":155,"robot_y":44,"grid_height":320,"grid_width":224,"origin_x":-8.699999809265137,"origin_y":-2.3499999046325684,"resolution":0.05000000074505806,"angle":171.45908700692397}*/

    private String type;
    private double robot_x;
    private double robot_y;
    private int grid_height;
    private int grid_width;
    private double origin_x;
    private double origin_y;
    private double resolution;
    private double angle;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getRobot_x() {
        return robot_x;
    }

    public void setRobot_x(double robot_x) {
        this.robot_x = robot_x;
    }

    public double getRobot_y() {
        return robot_y;
    }

    public void setRobot_y(double robot_y) {
        this.robot_y = robot_y;
    }

    public int getGrid_height() {
        return grid_height;
    }

    public void setGrid_height(int grid_height) {
        this.grid_height = grid_height;
    }

    public int getGrid_width() {
        return grid_width;
    }

    public void setGrid_width(int grid_width) {
        this.grid_width = grid_width;
    }

    public double getOrigin_x() {
        return origin_x;
    }

    public void setOrigin_x(double origin_x) {
        this.origin_x = origin_x;
    }

    public double getOrigin_y() {
        return origin_y;
    }

    public void setOrigin_y(double origin_y) {
        this.origin_y = origin_y;
    }

    public double getResolution() {
        return resolution;
    }

    public void setResolution(double resolution) {
        this.resolution = resolution;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    @Override
    public String toString() {
        return "CurrentPositionDataBean{" +
                "type='" + type + '\'' +
                ", robot_x=" + robot_x +
                ", robot_y=" + robot_y +
                ", grid_height=" + grid_height +
                ", grid_width=" + grid_width +
                ", origin_x=" + origin_x +
                ", origin_y=" + origin_y +
                ", resolution=" + resolution +
                ", angle=" + angle +
                '}';
    }
}
