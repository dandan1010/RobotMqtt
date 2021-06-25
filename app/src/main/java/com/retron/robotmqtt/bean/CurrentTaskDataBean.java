package com.retron.robotmqtt.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class CurrentTaskDataBean implements Serializable {
    private String type;
    private String task_Name;
    private String map_Name;
    private ArrayList<CurrentTaskPoint> robot_task_state;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTask_Name(String task_Name) {
        this.task_Name = task_Name;
    }

    public String getTask_Name() {
        return task_Name;
    }

    public String getMap_Name() {
        return map_Name;
    }

    public void setMap_Name(String map_Name) {
        this.map_Name = map_Name;
    }

    public ArrayList<CurrentTaskPoint> getRobot_task_state() {
        return robot_task_state;
    }

    public void setRobot_task_state(ArrayList<CurrentTaskPoint> robot_task_state) {
        this.robot_task_state = robot_task_state;
    }

    public class CurrentTaskPoint implements Serializable{

        private int point_index;
        private String point_Name;
        private String point_type;
        private String point_state;

        public int getPoint_index() {
            return point_index;
        }

        public void setPoint_index(int point_index) {
            this.point_index = point_index;
        }

        public String getPoint_name() {
            return point_Name;
        }

        public void setPoint_name(String point_name) {
            this.point_Name = point_name;
        }

        public String getPoint_type() {
            return point_type;
        }

        public void setPoint_type(String point_type) {
            this.point_type = point_type;
        }

        public String getPoint_state() {
            return point_state;
        }

        public void setPoint_state(String point_state) {
            this.point_state = point_state;
        }
    }
}
