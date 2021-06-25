package com.retron.robotmqtt.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.retron.robotmqtt.bean.CurrentTaskDataBean;
import com.retron.robotmqtt.bean.HistoryTaskDataBean;
import com.retron.robotmqtt.bean.RobotTaskErrorBean;
import com.retron.robotmqtt.bean.SchedulerTaskListBean;

public class TaskViewModel extends AndroidViewModel {

    public MutableLiveData<SchedulerTaskListBean> taskList = new MutableLiveData<>();
    public MutableLiveData<CurrentTaskDataBean> currentTask = new MutableLiveData<>();
    public MutableLiveData<HistoryTaskDataBean> history = new MutableLiveData<>();
    public MutableLiveData<RobotTaskErrorBean> taskError = new MutableLiveData<>();

    public TaskViewModel(@NonNull Application application) {
        super(application);
    }

    /*public TaskViewModel(@NonNull Application application, LiveData<SchedulerTaskListBean> taskList) {
        super(application);
        this.taskList = taskList;
        this.application = application;
    }*/

    public SchedulerTaskListBean getTaskList() {
        return taskList.getValue();
    }

    public void setTaskList(SchedulerTaskListBean taskList) {
        android.util.Log.d("TaskViewModel", "setTaskList");
        this.taskList.postValue(taskList);
    }

    public void setCurrentTask(CurrentTaskDataBean currentTask) {
        this.currentTask.postValue(currentTask);
    }

    public CurrentTaskDataBean getCurrentTask(){
        return this.currentTask.getValue();
    }

    public HistoryTaskDataBean getHistoryTask(){
        return this.history.getValue();
    }

    public void setHistoryTask(HistoryTaskDataBean historyTask){
        this.history.postValue(historyTask);
    }

    public RobotTaskErrorBean getrobotError(){
        return this.taskError.getValue();
    }

    public void setrobotError(RobotTaskErrorBean taskErrorBean){
        this.taskError.postValue(taskErrorBean);
    }
}
