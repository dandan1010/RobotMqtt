package com.retron.robotmqtt.bean;

public class DialogCheckboxBean {
    private String taskName;
    private boolean isClick;

    public DialogCheckboxBean(String taskName, boolean isClick){
        this.taskName = taskName;
        this.isClick = isClick;
    }

    public DialogCheckboxBean(){

    }

    public String getTaskName() {
        return this.taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public boolean isClick() {
        return this.isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }
}
