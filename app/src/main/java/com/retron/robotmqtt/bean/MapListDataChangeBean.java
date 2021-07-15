package com.retron.robotmqtt.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class MapListDataChangeBean {
    private String type;
    private MapDataBean mapDataBean;

    public MapDataBean getMapDataBean() {
        return mapDataBean;
    }

    public void setMapDataBean(MapDataBean mapDataBean) {
        this.mapDataBean = mapDataBean;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static class MapDataBean implements Serializable {
        private String map_name_uuid;
        private String map_name;
        private int grid_height;
        private int grid_width;
        private double origin_x;
        private double origin_y;
        private double resolution;
        private String map_link;
        private String map_md5;
        private String dump_link;
        private String dump_md5;
        private ArrayList<ContrastPointDataBean> point;
        private VirtualDataBean virtualDataBeans;
        private DownLoadMapBean downLoadMapBean;

        public String getMap_link() {
            return map_link;
        }

        public void setMap_link(String map_link) {
            this.map_link = map_link;
        }

        public String getMap_md5() {
            return map_md5;
        }

        public void setMap_md5(String map_md5) {
            this.map_md5 = map_md5;
        }

        public String getDump_link() {
            return dump_link;
        }

        public void setDump_link(String dump_link) {
            this.dump_link = dump_link;
        }

        public String getDump_md5() {
            return dump_md5;
        }

        public void setDump_md5(String dump_md5) {
            this.dump_md5 = dump_md5;
        }


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

        public ArrayList<ContrastPointDataBean> getPoint() {
            return point;
        }

        public void setPoint(ArrayList<ContrastPointDataBean> point) {
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
                    "map_name_uuid='" + map_name_uuid + '\'' +
                    ", map_name='" + map_name + '\'' +
                    ", grid_height=" + grid_height +
                    ", grid_width=" + grid_width +
                    ", origin_x=" + origin_x +
                    ", origin_y=" + origin_y +
                    ", resolution=" + resolution +
                    ", map_link='" + map_link + '\'' +
                    ", map_md5='" + map_md5 + '\'' +
                    ", dump_link='" + dump_link + '\'' +
                    ", dump_md5='" + dump_md5 + '\'' +
                    ", point=" + point +
                    ", virtualDataBeans=" + virtualDataBeans +
                    ", downLoadMapBean=" + downLoadMapBean +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "MapListDataChangeBean{" +
                "type='" + type + '\'' +
                ", mapDataBean=" + mapDataBean +
                '}';
    }
}
