package com.retron.robotmqtt.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class HistoryTaskDataBean implements Serializable {
    /*{"type":"robot_task_history","robot_task_history":[{"dbTaskMapName":"0604","taskName":"Temp","time":"4","date":"2021-06-04 15:26","dbStartBattery":"27%","dbEndBattery":"27%","dbTaskIndex":"3","dbTaskPointState":"point11:REACHED ; point2,0:REACHED ; Initialize,3:UNREACHED "},{"dbTaskMapName":"0604","taskName":"Temp","time":"-1","date":"2021-06-04 15:38","dbStartBattery":"25%","dbEndBattery":"25%","dbTaskIndex":"1","dbTaskPointState":""}],"dbAreaTotalCount":18,"dbTaskTotalCount":1,"dbTimeTotalCount":264000,"dbAreaCurrentCount":0,"dbTaskCurrentCount":0,"dbTimeCurrentCount":0}*/
    /*{"type":"robot_task_history","robot_task_history":[{"dbTaskMapName":"mapmap","taskName":"1622297834383","time":"0","date":"2021-05-29 22:19","dbStartBattery":"43%","dbEndBattery":"43%","dbTaskIndex":"1"},{"dbTaskMapName":"mapmap","taskName":"Temp","time":"3","date":"2021-05-31 19:37","dbStartBattery":"99%","dbEndBattery":"99%","dbTaskIndex":"2"},{"dbTaskMapName":"mapmap","taskName":"Temp","time":"3","date":"2021-05-31 19:45","dbStartBattery":"97%","dbEndBattery":"97%","dbTaskIndex":"1"},{"dbTaskMapName":"mapmap","taskName":"Temp","time":"2","date":"2021-05-31 19:58","dbStartBattery":"98%","dbEndBattery":"98%","dbTaskIndex":"1"},{"dbTaskMapName":"mapmap","taskName":"Temp","time":"3","date":"2021-05-31 20:02","dbStartBattery":"98%","dbEndBattery":"98%","dbTaskIndex":"1"},{"dbTaskMapName":"mapmap","taskName":"Temp","time":"0","date":"2021-05-31 20:10","dbStartBattery":"98%","dbEndBattery":"98%","dbTaskIndex":"1"}],
    "dbAreaTotalCount":63,"dbTaskTotalCount":6,"dbTimeTotalCount":873530,"dbAreaCurrentCount":0,"dbTaskCurrentCount":0,"dbTimeCurrentCount":0}*/

    private String type;
    private ArrayList<RobotHistoryTask> robot_task_history;
    private long dbAreaTotalCount;
    private long dbTaskTotalCount;
    private long dbTimeTotalCount;
    private long dbAreaCurrentCount;//area count in current month
    private long dbTaskCurrentCount;//task count in current month
    private long dbTimeCurrentCount;//time count in current month

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public long getDbAreaCurrentCount() {
        return dbAreaCurrentCount;
    }

    public void setDbAreaCurrentCount(long dbAreaCurrentCount) {
        this.dbAreaCurrentCount = dbAreaCurrentCount;
    }

    public long getDbAreaTotalCount() {
        return dbAreaTotalCount;
    }

    public void setDbAreaTotalCount(long dbAreaTotalCount) {
        this.dbAreaTotalCount = dbAreaTotalCount;
    }

    public long getDbTaskTotalCount() {
        return dbTaskTotalCount;
    }

    public void setDbTaskTotalCount(long dbTaskTotalCount) {
        this.dbTaskTotalCount = dbTaskTotalCount;
    }

    public long getDbTimeTotalCount() {
        return dbTimeTotalCount;
    }

    public void setDbTimeTotalCount(long dbTimeTotalCount) {
        this.dbTimeTotalCount = dbTimeTotalCount;
    }

    public long getDbTaskCurrentCount() {
        return dbTaskCurrentCount;
    }

    public void setDbTaskCurrentCount(long dbTaskCurrentCount) {
        this.dbTaskCurrentCount = dbTaskCurrentCount;
    }

    public long getDbTimeCurrentCount() {
        return dbTimeCurrentCount;
    }

    public void setDbTimeCurrentCount(long dbTimeCurrentCount) {
        this.dbTimeCurrentCount = dbTimeCurrentCount;
    }

    public ArrayList<RobotHistoryTask> getRobot_task_history() {
        return robot_task_history;
    }

    public void setRobot_task_history(ArrayList<RobotHistoryTask> robot_task_history) {
        this.robot_task_history = robot_task_history;
    }

    public class RobotHistoryTask implements Serializable{
        /*{"dbTaskMapName":"mapmap","taskName":"1622297834383","time":"0","date":"2021-05-29 22:19","dbStartBattery":"43%","dbEndBattery":"43%","dbTaskIndex":"1"}*/
        private String dbMapNameUuid;
        private String dbTaskMapName;
        private String taskName;
        private String time;
        private String date;
        private String dbStartBattery;
        private String dbEndBattery;
        private String dbTaskIndex;

        public String getDbMapNameUuid() {
            return dbMapNameUuid;
        }

        public void setDbMapNameUuid(String dbMapNameUuid) {
            this.dbMapNameUuid = dbMapNameUuid;
        }

        public String getDbTaskPointState() {
            return dbTaskPointState;
        }

        public void setDbTaskPointState(String dbTaskPointState) {
            this.dbTaskPointState = dbTaskPointState;
        }

        private String dbTaskPointState;

        public void setTime(String time) {
            this.time = time;
        }

        public String getDate() {
            return date;
        }

        public String getDbTaskMapName() {
            return dbTaskMapName;
        }

        public String getTaskName() {
            return taskName;
        }

        public String getTime() {
            return time;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public void setDbTaskMapName(String dbTaskMapName) {
            this.dbTaskMapName = dbTaskMapName;
        }

        public void setTaskName(String taskName) {
            this.taskName = taskName;
        }

        public String getDbEndBattery() {
            return dbEndBattery;
        }

        public String getDbStartBattery() {
            return dbStartBattery;
        }

        public String getDbTaskIndex() {
            return dbTaskIndex;
        }

        public void setDbTaskIndex(String dbTaskIndex) {
            this.dbTaskIndex = dbTaskIndex;
        }

        public void setDbEndBattery(String dbEndBattery) {
            this.dbEndBattery = dbEndBattery;
        }

        public void setDbStartBattery(String dbStartBattery) {
            this.dbStartBattery = dbStartBattery;
        }

        @Override
        public String toString() {
            return "RobotHistoryTask{" +
                    "dbMapNameUuid='" + dbMapNameUuid + '\'' +
                    ", dbTaskMapName='" + dbTaskMapName + '\'' +
                    ", taskName='" + taskName + '\'' +
                    ", time='" + time + '\'' +
                    ", date='" + date + '\'' +
                    ", dbStartBattery='" + dbStartBattery + '\'' +
                    ", dbEndBattery='" + dbEndBattery + '\'' +
                    ", dbTaskIndex='" + dbTaskIndex + '\'' +
                    ", dbTaskPointState='" + dbTaskPointState + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "HistoryTaskDataBean{" +
                "type='" + type + '\'' +
                ", robot_task_history=" + robot_task_history +
                ", dbAreaTotalCount=" + dbAreaTotalCount +
                ", dbTaskTotalCount=" + dbTaskTotalCount +
                ", dbTimeTotalCount=" + dbTimeTotalCount +
                ", dbAreaCurrentCount=" + dbAreaCurrentCount +
                ", dbTaskCurrentCount=" + dbTaskCurrentCount +
                ", dbTimeCurrentCount=" + dbTimeCurrentCount +
                '}';
    }
}
