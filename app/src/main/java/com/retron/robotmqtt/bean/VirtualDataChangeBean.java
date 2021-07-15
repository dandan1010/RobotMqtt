package com.retron.robotmqtt.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class VirtualDataChangeBean implements Serializable {
    /*{"type":"send_virtual","send_virtual":[[{"virtual_x":322,"virtual_y":525},{"virtual_x":709,"virtual_y":536}],[{"virtual_x":940,"virtual_y":537},{"virtual_x":934,"virtual_y":354}],[{"virtual_x":1018,"virtual_y":505},{"virtual_x":1019,"virtual_y":350}],[{"virtual_x":937,"virtual_y":365},{"virtual_x":1022,"virtual_y":366}],[{"virtual_x":705,"virtual_y":538},{"virtual_x":705,"virtual_y":538},{"virtual_x":939,"virtual_y":536}],[{"virtual_x":491,"virtual_y":736},{"virtual_x":495,"virtual_y":722},{"virtual_x":529,"virtual_y":723},{"virtual_x":535,"virtual_y":737}],[{"virtual_x":1054,"virtual_y":746},{"virtual_x":1056,"virtual_y":707},{"virtual_x":1020,"virtual_y":707},{"virtual_x":1016,"virtual_y":688},{"virtual_x":1016,"virtual_y":688}],[{"virtual_x":1064,"virtual_y":748},{"virtual_x":1060,"virtual_y":791}],[{"virtual_x":988,"virtual_y":795},{"virtual_x":988,"virtual_y":769},{"virtual_x":974,"virtual_y":769},{"virtual_x":970,"virtual_y":800}],[{"virtual_x":936,"virtual_y":788},{"virtual_x":936,"virtual_y":775},{"virtual_x":915,"virtual_y":775},{"virtual_x":918,"virtual_y":804}],[{"virtual_x":935,"virtual_y":745},{"virtual_x":937,"virtual_y":754},{"virtual_x":957,"virtual_y":754},{"virtual_x":957,"virtual_y":746}],[{"virtual_x":886,"virtual_y":695},{"virtual_x":922,"virtual_y":695},{"virtual_x":920,"virtual_y":662}],[{"virtual_x":694,"virtual_y":766},{"virtual_x":665,"virtual_y":768},{"virtual_x":667,"virtual_y":790}],[{"virtual_x":795,"virtual_y":751},{"virtual_x":793,"virtual_y":799}],[{"virtual_x":645,"virtual_y":770},{"virtual_x":610,"virtual_y":771},{"virtual_x":605,"virtual_y":797}],[{"virtual_x":588,"virtual_y":760},{"virtual_x":550,"virtual_y":758},{"virtual_x":550,"virtual_y":789}],[{"virtual_x":462,"virtual_y":771},{"virtual_x":475,"virtual_y":768},{"virtual_x":472,"virtual_y":797}],[{"virtual_x":328,"virtual_y":680},{"virtual_x":295,"virtual_y":679},{"virtual_x":299,"virtual_y":576},{"virtual_x":331,"virtual_y":580}],[{"virtual_x":437,"virtual_y":617},{"virtual_x":451,"virtual_y":619}],[{"virtual_x":450,"virtual_y":619},{"virtual_x":450,"virtual_y":650}],[{"virtual_x":1061,"virtual_y":793},{"virtual_x":1059,"virtual_y":791},{"virtual_x":1056,"virtual_y":793},{"virtual_x":1062,"virtual_y":793}],[{"virtual_x":893,"virtual_y":691},{"virtual_x":355,"virtual_y":690}]]}
     */
    private String type;
    private String map_name_uuid;
    private String map_name;
    private ArrayList<ArrayList<Point>> send_virtual;

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

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public ArrayList<ArrayList<Point>> getSend_virtual() {
        return send_virtual;
    }

    public void setSend_virtual(ArrayList<ArrayList<Point>> send_virtual) {
        this.send_virtual = send_virtual;
    }

    public class Point{
        private double virtual_x;
        private double virtual_y;

        public double getVirtual_x() {
            return virtual_x;
        }

        public void setVirtual_x(double virtual_x) {
            this.virtual_x = virtual_x;
        }

        public double getVirtual_y() {
            return virtual_y;
        }

        public void setVirtual_y(double virtual_y) {
            this.virtual_y = virtual_y;
        }

        @Override
        public String toString() {
            return "Point{" +
                    "virtual_x=" + virtual_x +
                    ", virtual_y=" + virtual_y +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "VirtualDataBean{" +
                "type='" + type + '\'' +
                ", map_name_uuid='" + map_name_uuid + '\'' +
                ", map_name='" + map_name + '\'' +
                ", send_virtual=" + send_virtual +
                '}';
    }
}
