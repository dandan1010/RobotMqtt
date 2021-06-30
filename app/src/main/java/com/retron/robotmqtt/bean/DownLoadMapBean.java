package com.retron.robotmqtt.bean;

public class DownLoadMapBean {
    private String map_name ;
    private String map_Name_uuid;
    private Object object;

    public String getMap_Name_uuid() {
        return map_Name_uuid;
    }

    public void setMap_Name_uuid(String map_Name_uuid) {
        this.map_Name_uuid = map_Name_uuid;
    }

    public String getMap_name() {
        return map_name;
    }

    public void setMap_name(String map_name) {
        this.map_name = map_name;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "DownLoadMapBean{" +
                "map_name='" + map_name + '\'' +
                ", map_Name_uuid='" + map_Name_uuid + '\'' +
                ", object=" + object +
                '}';
    }
}
