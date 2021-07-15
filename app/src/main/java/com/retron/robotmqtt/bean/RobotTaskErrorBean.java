package com.retron.robotmqtt.bean;

import java.util.List;

public class RobotTaskErrorBean {

    /**
     * report_time : 2021-07-06 17:26:02
     * robot_task_error : [{"point_Name":"charging","error_msg":"UNREACHED","map_name":"kj","map_name_uuid":"1625551200461","ros_bag":"2021-07-06_17:24:32_0.bag"}]
     * type : robot_task_error
     */
    private String report_time;
    private List<Robot_task_errorEntity> robot_task_error;
    private String type;

    public void setReport_time(String report_time) {
        this.report_time = report_time;
    }

    public void setRobot_task_error(List<Robot_task_errorEntity> robot_task_error) {
        this.robot_task_error = robot_task_error;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReport_time() {
        return report_time;
    }

    public List<Robot_task_errorEntity> getRobot_task_error() {
        return robot_task_error;
    }

    public String getType() {
        return type;
    }

    public class Robot_task_errorEntity {
        /**
         * point_Name : charging
         * error_msg : UNREACHED
         * map_name : kj
         * map_name_uuid : 1625551200461
         * ros_bag : 2021-07-06_17:24:32_0.bag
         */
        private String point_Name;
        private String error_msg;
        private String map_name;
        private String map_name_uuid;
        private String ros_bag;

        public void setPoint_Name(String point_Name) {
            this.point_Name = point_Name;
        }

        public void setError_msg(String error_msg) {
            this.error_msg = error_msg;
        }

        public void setMap_name(String map_name) {
            this.map_name = map_name;
        }

        public void setMap_name_uuid(String map_name_uuid) {
            this.map_name_uuid = map_name_uuid;
        }

        public void setRos_bag(String ros_bag) {
            this.ros_bag = ros_bag;
        }

        public String getPoint_Name() {
            return point_Name;
        }

        public String getError_msg() {
            return error_msg;
        }

        public String getMap_name() {
            return map_name;
        }

        public String getMap_name_uuid() {
            return map_name_uuid;
        }

        public String getRos_bag() {
            return ros_bag;
        }
    }
}
