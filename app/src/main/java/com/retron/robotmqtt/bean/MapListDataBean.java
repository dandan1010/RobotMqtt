package com.retron.robotmqtt.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class MapListDataBean implements Serializable {
    private String type;

    private ArrayList<MapDataBean> sendMapName;

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public ArrayList<MapDataBean> getSendMapName() {
        return sendMapName;
    }

    public void setSendMapName(ArrayList<MapDataBean> sendMapName) {
        this.sendMapName = sendMapName;
    }

    public class MapDataBean implements Serializable{
        private String map_Name;
        private int grid_height;
        private int grid_width;
        private double origin_x;
        private double origin_y;
        private double resolution;
        private ArrayList<PointDataBean> point;
        private VirtualDataBean virtualDataBeans;
        private DownLoadMapBean downLoadMapBean;

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

        public String getMap_Name() {
            return map_Name;
        }

        public void setMap_Name(String map_Name) {
            this.map_Name = map_Name;
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

        @Override
        public String toString() {
            return "MapDataBean{" +
                    "map_Name='" + map_Name + '\'' +
                    ", grid_height=" + grid_height +
                    ", grid_width=" + grid_width +
                    ", origin_x=" + origin_x +
                    ", origin_y=" + origin_y +
                    ", resolution=" + resolution +
                    ", point=" + point +
                    ", virtualDataBeans=" + virtualDataBeans +
                    ", downLoadMapBean=" + downLoadMapBean +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "MapListDataBean{" +
                "type='" + type + '\'' +
                ", sendMapName=" + sendMapName +
                '}';
    }
}
