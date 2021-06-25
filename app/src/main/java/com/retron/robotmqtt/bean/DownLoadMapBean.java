package com.retron.robotmqtt.bean;

public class DownLoadMapBean {
    private String map_Name ;
    private Object object;

    public String getMap_Name() {
        return map_Name;
    }

    public void setMap_Name(String map_Name) {
        this.map_Name = map_Name;
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
                "map_Name='" + map_Name + '\'' +
                ", object=" + object +
                '}';
    }
}
