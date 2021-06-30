package com.retron.robotmqtt.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.retron.robotmqtt.KeepAliveService;

public class ServerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("robotmqttReceiver", "开启: " + intent.getAction());
        if ("com.android.mqtt.server.start".equals(intent.getAction())){
            Intent intentServer = new Intent(context, KeepAliveService.class);
            context.startService(intentServer);
        } else if ("com.android.mqtt.reconnect".equals(intent.getAction())) {
            Intent intentServer = new Intent(context, KeepAliveService.class);
            context.stopService(intentServer);
            context.startService(intentServer);
        }
    }
}
