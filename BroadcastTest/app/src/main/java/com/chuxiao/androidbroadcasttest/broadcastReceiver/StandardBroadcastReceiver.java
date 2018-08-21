package com.chuxiao.androidbroadcasttest.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class StandardBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "收到自定义广播",Toast.LENGTH_SHORT).show();
        abortBroadcast();
    }
}
