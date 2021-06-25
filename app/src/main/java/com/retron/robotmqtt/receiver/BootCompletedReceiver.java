package com.retron.robotmqtt.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.retron.robotmqtt.KeepAliveService;

public class BootCompletedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intentServer = new Intent(context, KeepAliveService.class);
        context.startService(intentServer);
    }
}
