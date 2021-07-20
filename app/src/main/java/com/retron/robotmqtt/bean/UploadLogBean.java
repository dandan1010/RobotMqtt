package com.retron.robotmqtt.bean;

import java.util.List;

public class UploadLogBean {

    /**
     * type : upload_log
     * map : [{"ros_bag_md5":"xxxxxxxxx","ros_bag_link":"ftp://xxxxxxx","ros_bag":"lkmnbvfd_20210625"},{"ros_bag_md5":"xxxxxxxxx","ros_bag_link":"ftp://xxxxxxx","ros_bag":"lkmnbvfd_20210626"}]
     */
    private String type;
    private List<MapEntity> map;

    public void setType(String type) {
        this.type = type;
    }

    public void setMap(List<MapEntity> map) {
        this.map = map;
    }

    public String getType() {
        return type;
    }

    public List<MapEntity> getMap() {
        return map;
    }

    public class MapEntity {
        /**
         * ros_bag_md5 : xxxxxxxxx
         * ros_bag_link : ftp://xxxxxxx
         * ros_bag : lkmnbvfd_20210625
         */
        private String ros_bag_md5;
        private String ros_bag_link;
        private String ros_bag;

        public void setRos_bag_md5(String ros_bag_md5) {
            this.ros_bag_md5 = ros_bag_md5;
        }

        public void setRos_bag_link(String ros_bag_link) {
            this.ros_bag_link = ros_bag_link;
        }

        public void setRos_bag(String ros_bag) {
            this.ros_bag = ros_bag;
        }

        public String getRos_bag_md5() {
            return ros_bag_md5;
        }

        public String getRos_bag_link() {
            return ros_bag_link;
        }

        public String getRos_bag() {
            return ros_bag;
        }
    }
}
