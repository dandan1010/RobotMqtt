package com.retron.robotmqtt.bean;

import java.util.List;

public class RobotTaskErrorBean {

    /**
     * report_time : 2021-11-23 18:51:30
     * type : robot_task_error
     * robot_task_error_msg : [{"error_msg":"UNREACHED","map_name":"test","point_name":"12","ros_bag":"2021_11_23_18_51_30.zip"}]
     */
    private String report_time;
    private String type;
    private List<Robot_task_error_msgEntity> robot_task_error_msg;

    public void setReport_time(String report_time) {
        this.report_time = report_time;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRobot_task_error_msg(List<Robot_task_error_msgEntity> robot_task_error_msg) {
        this.robot_task_error_msg = robot_task_error_msg;
    }

    public String getReport_time() {
        return report_time;
    }

    public String getType() {
        return type;
    }

    public List<Robot_task_error_msgEntity> getRobot_task_error_msg() {
        return robot_task_error_msg;
    }

    public class Robot_task_error_msgEntity {
        /**
         * error_msg : UNREACHED
         * map_name : test
         * point_name : 12
         * ros_bag : 2021_11_23_18_51_30.zip
         */
        private String error_msg;
        private String map_name_uuid;
        private String map_name;
        private String point_name;
        private String ros_bag;

        public String getMap_name_uuid() {
            return map_name_uuid;
        }

        public void setMap_name_uuid(String map_name_uuid) {
            this.map_name_uuid = map_name_uuid;
        }

        public void setError_msg(String error_msg) {
            this.error_msg = error_msg;
        }

        public void setMap_name(String map_name) {
            this.map_name = map_name;
        }

        public void setPoint_name(String point_name) {
            this.point_name = point_name;
        }

        public void setRos_bag(String ros_bag) {
            this.ros_bag = ros_bag;
        }

        public String getError_msg() {
            return error_msg;
        }

        public String getMap_name() {
            return map_name;
        }

        public String getPoint_name() {
            return point_name;
        }

        public String getRos_bag() {
            return ros_bag;
        }
    }
}
