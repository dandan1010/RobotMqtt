package com.retron.robotmqtt.utils;

public final class Content {

    public static String robot_statue = "";
    public static String Initialize = "Initialize";
    public static final String Standby = "Standby";
    public static final String Charging_at = "Charging at charging";
    public static final String scanning_map = "scanning_map";


    public static final String SCANNING_MAP = "scanning_map";//机器人状态
    public static String REPORT_TIME = "report_time";//健康报告时间
    public static final String ROBOT_TASK_ERROR = "robot_task_error";//点任务报错信息
    public static final String SENDMAPNAME = "sendMapName";//返回地图列表名称
    public static final String GETPOSITION = "getPosition";//请求机器人位置
    public static final String SENDGPSPOSITION = "sendGpsPosition";//返回机器人位置
    public static final String UPDATE = "update";//编辑任务类型

    public static final String USE_MAP = "use_map";//应用地图
    public static final String REQUEST_MSG = "request_msg";//返回失败结果
    public static final String CONNECTING = "connecting";
    public static final String TEST_SENSOR_CALLBACK = "test_sensor_callback";

    public static final String ROBOT_HEALTHY = "robot_healthy";//机器人健康
    public static final String ROBOT_TASK_STATE = "robot_task_state";//机器人任务状态
    public static final String ROBOT_TASK_HISTORY = "robot_task_history";//机器人历史任务
    public static final String HISTORY_TIME = "history_time";

    public static final String SEND_MQTT_VIRTUAL = "send_mqtt_virtual";//返回虚拟墙数据
    public static final String MQTT_UPDATA_VIRTUAL = "mqtt_updata_virtual";//更新虚拟墙
    public static final String VIRTUAL_X = "virtual_x";//虚拟墙数据
    public static final String VIRTUAL_Y = "virtual_y";//虚拟墙数据
    //db Name
    public static final String GET_ALL_TASK_STATE = "get_all_task_state";//断连请求
    public static final String STATUS = "status";//机器人状态
    public static final String GET_SETTING = "get_setting";//设置信息
    public static final String SET_SETTING = "set_setting";//设置信息
    public static final String UPLOAD_MAP = "upload_map";//上传地图
    public static final String UPLOADMAPSYN = "uploadmapsyn";//上传地图成功
    public static final String MQTT_ADD_POINT = "mqtt_add_point";//添加点
    public static final String DELETE_POSITION = "delete_position";//删除点
    public static final String MAP_NAME_UUID = "map_name_uuid";//地图名字
    public static final String NEW_MAP_NAME = "new_map_name";//新地图名
    public static final String MQTT_RENAME_MAP = "mqtt_rename_map";//重命名地图
    public static final String DELETE_MAP = "delete_map";//删除地图
    public static final String DELETE_MAP_FAIL = "delete_map_fail";
    public static final String MAP_NAME = "map_name";//地图名字
    public static final String TASK_NAME = "task_Name";//任务名字
    public static final String POINT_NAME = "point_Name";//点的名字
    public static final String POINT_INDEX = "point_index";//点的名字
    public static final String POINT_X = "point_x";//点x坐标
    public static final String POINT_Y = "point_y";//点y坐标
    public static final String POINT_TYPE = "point_type";//点类型
    public static final String POINT_STATE = "point_state";//点执行状态
    public static final String POINT_TIME = "point_time";//点时间
    public static final String ANGLE = "angle";//角度
    public static final String SAVETASKQUEUE = "saveTaskQueue";//存储任务
    public static final String dbAlarmMapTaskName = "dbAlarmMapTaskName";
    public static final String dbAlarmTime = "dbAlarmTime";//时间
    public static final String dbAlarmCycle = "dbAlarmCycle";//周期
    public static final String dbAlarmIsRun = "dbAlarmIsRun";//周期任务是否执行
    public static final String TASK_TYPE = "task_type";//是否是定时任务
    public static final String POINT = "point";//是否是定时任务
    public static final String STARTMOVE = "startMove";//机器人移动
    public static final String GETMAPLIST = "getMapList";//请求地图列表
    public static final String DELETETASKQUEUE = "deleteTaskQueue";//删除任务队列
    public static final String RESET_ROBOT = "reset_robot";//重置设备
    public static final String STARTTASKQUEUE = "startTaskQueue";//开始任务队列
    public static final String GET_LOG_LIST = "get_log_list";//log列表
    public static final String MQTT_DOWNLOAD_LOG = "mqtt_download_log";//下载log
    public static final String COMPRESSED = "compressed";
    public static final String FILE_NAME = "file_name";
    public static final String response_ok = "ok";
    public static final String response_fail = "fail";
    public static final String ros_bag = "ros_bag";


    public static final String UPDATE_FILE_NAME = "update_file_name";//for ota

    //attributes
    public static final String taskStatus = "task_status";
    public static final String setting = "settings";
    public static final String tasklist = "task_list";
    public static final String maplist = "map_list";
    public static final String location = "location_info";
    public static final String history = "history";
    public static final String download_map_dump = "download_map_dump";
    public static final String upload_bag_result = "upload_bag_result";
    public static final String result = "result";
    public static final String upload_map_result = "upload_map_result";

    //telemetry
    public static final String robotStatus = "robot_status";
    public static final String device_error = "device_error";
    public static final String task_error = "task_error";
    public static final String gps_position = "gps_position";

    //RPC command type
    public static final String move = "move";
    public static final String usemap = "use_map";
    public static final String initialize = "initialize";
    public static final String sync_task_list = "sync_task_list";
    public static final String syncMap = "sync_map";
    public static final String syncSetting = "sync_settings";
    public static final String execTask = "execute_task";
    public static final String send_map_dump = "send_map_dump";
    public static final String upload_map = "upload_map";
    public static final String upload_log = "upload_log";
    public static final String reset = "reset";
    public static final String execute_task = "execute_task";
    public static final String ota = "ota";

    public static final String data = "data";
    public static final String dump_md5 = "dump_md5";
    public static final String dbName = "mqttDatabases";
    public static final String MapNameDeatabase = "MapNameDeatabase";
    public static final String MapNameUuid = "MapNameUuid";
    public static final String MapDumpMd5 = "MapDumpMd5";

}
