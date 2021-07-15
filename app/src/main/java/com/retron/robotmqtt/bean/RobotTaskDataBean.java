package com.retron.robotmqtt.bean;

import java.io.Serializable;
import java.util.List;

public class RobotTaskDataBean implements Serializable {

    /**
     * task_Name : Temp
     * robot_task_state : [{"point_state":"Skip","point_Name":"lkmn","point_time":"60"},{"point_state":"OnGoing","point_Name":"charging","point_time":"0"}]
     * map_name : kj
     * map_name_uuid : 1625551200461
     * type : robot_task_state
     */
    private String task_Name;
    private List<Robot_task_stateEntity> robot_task_state;
    private String map_name;
    private String map_name_uuid;
    private String type;

    public void setTask_Name(String task_Name) {
        this.task_Name = task_Name;
    }

    public void setRobot_task_state(List<Robot_task_stateEntity> robot_task_state) {
        this.robot_task_state = robot_task_state;
    }

    public void setMap_name(String map_name) {
        this.map_name = map_name;
    }

    public void setMap_name_uuid(String map_name_uuid) {
        this.map_name_uuid = map_name_uuid;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTask_Name() {
        return task_Name;
    }

    public List<Robot_task_stateEntity> getRobot_task_state() {
        return robot_task_state;
    }

    public String getMap_name() {
        return map_name;
    }

    public String getMap_name_uuid() {
        return map_name_uuid;
    }

    public String getType() {
        return type;
    }

    public class Robot_task_stateEntity {
        /**
         * point_state : Skip
         * point_Name : lkmn
         * point_time : 60
         */
        private String point_state;
        private String point_Name;
        private String point_time;

        public void setPoint_state(String point_state) {
            this.point_state = point_state;
        }

        public void setPoint_Name(String point_Name) {
            this.point_Name = point_Name;
        }

        public void setPoint_time(String point_time) {
            this.point_time = point_time;
        }

        public String getPoint_state() {
            return point_state;
        }

        public String getPoint_Name() {
            return point_Name;
        }

        public String getPoint_time() {
            return point_time;
        }
    }
}
