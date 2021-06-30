package com.retron.robotmqtt.bean;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class SettingsDataBean implements Serializable {
    /*{"type":"get_setting","robot_name":"","get_navigationSpeedLevel":0,"get_playPathSpeedLevel":0,"get_low_battery":30,"get_voice_level":0,"get_working_mode":2,"get_charging_mode":true,"map_Name":"","versionCode":"V1.000.1","robotVersionCode":"GS-Conbox-06-WXCW-571-DABAI-V5_V2-4-26"}*/
    private String type;
    private int get_navigationSpeedLevel;
    private int get_playPathSpeedLevel;
    private int get_low_battery;
    private int get_voice_level;
    private int get_working_mode;
    private String get_charging_mode;
    private String map_name_uuid;
    private String map_name;
    private String versionCode;
    private String robotVersionCode;
    private String robot_name;

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
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

    public int getGet_low_battery() {
        return get_low_battery;
    }

    public int getGet_navigationSpeedLevel() {
        return get_navigationSpeedLevel;
    }

    public int getGet_playPathSpeedLevel() {
        return get_playPathSpeedLevel;
    }

    public int getGet_voice_level() {
        return get_voice_level;
    }

    public int getGet_working_mode() {
        return get_working_mode;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public String getGet_charging_mode() {
        return get_charging_mode;
    }

    public String getRobotVersionCode() {
        return robotVersionCode;
    }

    public void setGet_charging_mode(String get_charging_mode) {
        this.get_charging_mode = get_charging_mode;
    }

    public void setGet_low_battery(int get_low_battery) {
        this.get_low_battery = get_low_battery;
    }

    public void setGet_navigationSpeedLevel(int get_navigationSpeedLevel) {
        this.get_navigationSpeedLevel = get_navigationSpeedLevel;
    }

    public void setGet_playPathSpeedLevel(int get_playPathSpeedLevel) {
        this.get_playPathSpeedLevel = get_playPathSpeedLevel;
    }

    public void setGet_voice_level(int get_voice_level) {
        this.get_voice_level = get_voice_level;
    }

    public void setGet_working_mode(int get_working_mode) {
        this.get_working_mode = get_working_mode;
    }

    public void setRobotVersionCode(String robotVersionCode) {
        this.robotVersionCode = robotVersionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getRobot_name() {
        return robot_name;
    }

    public void setRobot_name(String robot_name) {
        this.robot_name = robot_name;
    }

    @Override
    public String toString() {
        return "SettingsDataBean{" +
                "type='" + type + '\'' +
                ", get_navigationSpeedLevel=" + get_navigationSpeedLevel +
                ", get_playPathSpeedLevel=" + get_playPathSpeedLevel +
                ", get_low_battery=" + get_low_battery +
                ", get_voice_level=" + get_voice_level +
                ", get_working_mode=" + get_working_mode +
                ", get_charging_mode='" + get_charging_mode + '\'' +
                ", map_name_uuid='" + map_name_uuid + '\'' +
                ", map_name='" + map_name + '\'' +
                ", versionCode='" + versionCode + '\'' +
                ", robotVersionCode='" + robotVersionCode + '\'' +
                ", robot_name='" + robot_name + '\'' +
                '}';
    }
}
