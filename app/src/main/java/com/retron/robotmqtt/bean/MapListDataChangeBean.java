package com.retron.robotmqtt.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class MapListDataChangeBean {
    private String type;
    private ArrayList<MapListDataBean.MapDataBean> sendMapName;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<MapListDataBean.MapDataBean> getSendMapName() {
        return sendMapName;
    }

    public void setSendMapName(ArrayList<MapListDataBean.MapDataBean> sendMapName) {
        this.sendMapName = sendMapName;
    }

    public static class MapDataBean implements Serializable {
        private String map_name_uuid;
        private String map_name;
        private int grid_height;
        private int grid_width;
        private double origin_x;
        private double origin_y;
        private double resolution;
        private ArrayList<PointDataBean> point;
        private VirtualDataBean virtualDataBeans;
        private DownLoadMapBean downLoadMapBean;

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

        public DownLoadMapBean getDownLoadMapBean() {
            return downLoadMapBean;
        }

        public void setDownLoadMapBean(DownLoadMapBean downLoadMapBean) {
            this.downLoadMapBean = downLoadMapBean;
        }

        public VirtualDataBean getVirtualDataBeans() {
            return virtualDataBeans;
        }

        public void setVirtualDataBeans(VirtualDataBean virtualDataBeans) {
            this.virtualDataBeans = virtualDataBeans;
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

        public double getResolution() {
            return resolution;
        }

        public void setResolution(double resolution) {
            this.resolution = resolution;
        }

        public ArrayList<PointDataBean> getPoint() {
            return point;
        }

        public void setPoint(ArrayList<PointDataBean> point) {
            this.point = point;
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

    }

}
