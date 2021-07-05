package com.retron.robotmqtt.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SchedulerTaskListBean implements Serializable {
    /*{"type":"GET_ALL_TASK_STATE","task":[{"point":[{"point_Name":"point","point_x":149,"point_y":256}],"task_Name":"2.00","map_Name":"FIH_10F_New","dbAlarmTime":"02:00","dbAlarmCycle":"1"},{"point":[{"point_Name":"point","point_x":149,"point_y":256}],"task_Name":"2.00","map_Name":"FIH_10F_New","dbAlarmTime":"02:00","dbAlarmCycle":"2"},{"point":[{"point_Name":"point","point_x":149,"point_y":256}],"task_Name":"2.00","map_Name":"FIH_10F_New","dbAlarmTime":"02:00","dbAlarmCycle":"3"},{"point":[{"point_Name":"point","point_x":149,"point_y":256}],"task_Name":"2.00","map_Name":"FIH_10F_New","dbAlarmTime":"02:00","dbAlarmCycle":"4"},{"point":[{"point_Name":"point","point_x":149,"point_y":256}],"task_Name":"2.00","map_Name":"FIH_10F_New","dbAlarmTime":"02:00","dbAlarmCycle":"5"},{"point":[{"point_Name":"point","point_x":149,"point_y":256}],"task_Name":"2.00","map_Name":"FIH_10F_New","dbAlarmTime":"02:00","dbAlarmCycle":"6"},{"point":[{"point_Name":"point","point_x":149,"point_y":256}],"task_Name":"2.00","map_Name":"FIH_10F_New","dbAlarmTime":"02:00","dbAlarmCycle":"7"},{"point":[{"point_Name":"point","point_x":149,"point_y":256}],"task_Name":"3.00","map_Name":"FIH_10F_New","dbAlarmTime":"03:00","dbAlarmCycle":"1"},{"point":[{"point_Name":"point","point_x":149,"point_y":256}],"task_Name":"3.00","map_Name":"FIH_10F_New","dbAlarmTime":"03:00","dbAlarmCycle":"2"},{"point":[{"point_Name":"point","point_x":149,"point_y":256}],"task_Name":"3.00","map_Name":"FIH_10F_New","dbAlarmTime":"03:00","dbAlarmCycle":"3"},{"point":[{"point_Name":"point","point_x":149,"point_y":256}],"task_Name":"3.00","map_Name":"FIH_10F_New","dbAlarmTime":"03:00","dbAlarmCycle":"4"},{"point":[{"point_Name":"point","point_x":149,"point_y":256}],"task_Name":"3.00","map_Name":"FIH_10F_New","dbAlarmTime":"03:00","dbAlarmCycle":"5"},{"point":[{"point_Name":"point","point_x":149,"point_y":256}],"task_Name":"3.00","map_Name":"FIH_10F_New","dbAlarmTime":"03:00","dbAlarmCycle":"6"},{"point":[{"point_Name":"point","point_x":149,"point_y":256}],"task_Name":"3.00","map_Name":"FIH_10F_New","dbAlarmTime":"03:00","dbAlarmCycle":"7"},{"point":[{"point_Name":"point","point_x":149,"point_y":256}],"task_Name":"4.00","map_Name":"FIH_10F_New","dbAlarmTime":"04:00","dbAlarmCycle":"1"},{"point":[{"point_Name":"point","point_x":149,"point_y":256}],"task_Name":"4.00","map_Name":"FIH_10F_New","dbAlarmTime":"04:00","dbAlarmCycle":"2"},{"point":[{"point_Name":"point","point_x":149,"point_y":256}],"task_Name":"4.00","map_Name":"FIH_10F_New","dbAlarmTime":"04:00","dbAlarmCycle":"3"},{"point":[{"point_Name":"point","point_x":149,"point_y":256}],"task_Name":"4.00","map_Name":"FIH_10F_New","dbAlarmTime":"04:00","dbAlarmCycle":"4"},{"point":[{"point_Name":"point","point_x":149,"point_y":256}],"task_Name":"4.00","map_Name":"FIH_10F_New","dbAlarmTime":"04:00","dbAlarmCycle":"5"},{"point":[{"point_Name":"point","point_x":149,"point_y":256}],"task_Name":"4.00","map_Name":"FIH_10F_New","dbAlarmTime":"04:00","dbAlarmCycle":"6"},{"point":[{"point_Name":"point","point_x":149,"point_y":256}],"task_Name":"4.00","map_Name":"FIH_10F_New","dbAlarmTime":"04:00","dbAlarmCycle":"7"},{"point":[{"point_Name":"point","point_x":149,"point_y":256}],"task_Name":"5.00","map_Name":"FIH_10F_New","dbAlarmTime":"05:00","dbAlarmCycle":"1"},{"point":[{"point_Name":"point","point_x":149,"point_y":256}],"task_Name":"5.00","map_Name":"FIH_10F_New","dbAlarmTime":"05:00","dbAlarmCycle":"2"},{"point":[{"point_Name":"point","point_x":149,"point_y":256}],"task_Name":"5.00","map_Name":"FIH_10F_New","dbAlarmTime":"05:00","dbAlarmCycle":"3"},{"point":[{"point_Name":"point","point_x":149,"point_y":256}],"task_Name":"5.00","map_Name":"FIH_10F_New","dbAlarmTime":"05:00","dbAlarmCycle":"4"},{"point":[{"point_Name":"point","point_x":149,"point_y":256}],"task_Name":"5.00","map_Name":"FIH_10F_New","dbAlarmTime":"05:00","dbAlarmCycle":"5"},{"point":[{"point_Name":"point","point_x":149,"point_y":256}],"task_Name":"5.00","map_Name":"FIH_10F_New","dbAlarmTime":"05:00","dbAlarm*/
    private String type;
    private List<SchedulerTaskBean> task;

    public List<SchedulerTaskBean> getSchedulerTaskBeanList() {
        return task;
    }

    public void setSchedulerTaskBeanList(List<SchedulerTaskBean> schedulerTaskBeanList) {
        this.task = schedulerTaskBeanList;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public class SchedulerTaskBean implements Serializable {

        private ArrayList<PointDataBean> point;
        private String task_Name;
        private String dbAlarmCycle;//ArrayList<String> dbAlarmCycle;
        private String dbAlarmTaskName;
        private String dbAlarmTime;
        private String map_name_uuid;
        private String map_name;
        private boolean dbAlarmIsRun;

        public String getMap_name_uuid() {
            return map_name_uuid;
        }

        public void setMap_name_uuid(String map_name_uuid) {
            this.map_name_uuid = map_name_uuid;
        }

        public String getMap_name() {
            return map_name;
        }

        public void setMap_name(String map_name) {
            this.map_name = map_name;
        }

        public void setDbAlarmTime(String dbAlarmTime) {
            this.dbAlarmTime = dbAlarmTime;
        }

    /*public void setDbAlarmCycle(ArrayList<String> dbAlarmCycle) {
        this.dbAlarmCycle = dbAlarmCycle;
    }*/

        public String getDbAlarmTime() {
            return dbAlarmTime;
        }

        public ArrayList<PointDataBean> getPoint() {
            return point;
        }

    /*public ArrayList<String> getDbAlarmCycle() {
        return dbAlarmCycle;
    }*/

        public String getDbAlarmCycle() {
            return dbAlarmCycle;
        }

        public void setDbAlarmCycle(String dbAlarmCycle) {
            this.dbAlarmCycle = dbAlarmCycle;
        }

        public String getDbAlarmTaskName() {
            return dbAlarmTaskName;
        }

        public void setDbAlarmTaskName(String dbAlarmTaskName) {
            this.dbAlarmTaskName = dbAlarmTaskName;
        }

        public void setPoint(ArrayList<PointDataBean> point) {
            this.point = point;
        }

        public String getTask_Name() {
            return task_Name;
        }

        public void setTask_Name(String task_Name) {
            this.task_Name = task_Name;
        }

        public boolean isDbAlarmIsRun() {
            return dbAlarmIsRun;
        }

        public void setDbAlarmIsRun(boolean dbAlarmIsRun) {
            this.dbAlarmIsRun = dbAlarmIsRun;
        }

        @Override
        public String toString() {
            return "SchedulerTaskBean{" +
                    "point=" + point +
                    ", task_Name='" + task_Name + '\'' +
                    ", dbAlarmCycle='" + dbAlarmCycle + '\'' +
                    ", dbAlarmTaskName='" + dbAlarmTaskName + '\'' +
                    ", dbAlarmTime='" + dbAlarmTime + '\'' +
                    ", map_name_uuid='" + map_name_uuid + '\'' +
                    ", map_name='" + map_name + '\'' +
                    ", dbAlarmIsRun=" + dbAlarmIsRun +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "SchedulerTaskListBean{" +
                "type='" + type + '\'' +
                ", task=" + task +
                '}';
    }
}
