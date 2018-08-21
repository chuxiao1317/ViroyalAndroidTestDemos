package com.chuxiao.androidbroadcasttest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

//    private NetworkChangeReceiver networkChangeReceiver;

    private LocalReceiver localReceiver;

    private LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //系统级广播不需要管理器
//        IntentFilter intentFilter = new IntentFilter();
        //网络状态发生变化的系统广播
//        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
//        networkChangeReceiver = new NetworkChangeReceiver();
//        //动态注册广播接收器
//        registerReceiver(networkChangeReceiver, intentFilter);

        // 本地广播管理器
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        Button button = findViewById(R.id.bt_send_broadcast);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 发送自定义标准广播
//                sendBroadcast(new Intent("com.chuxiao.androidbroadcasttest.MY_BROADCAST"));
                //发送自定义有序广播
//                sendOrderedBroadcast(new Intent("com.chuxiao.androidbroadcasttest.MY_BROADCAST"), null);
                //发送本地广播
                localBroadcastManager.sendBroadcast(new Intent("com.chuxiao.androidbroadcasttest.LOCAL_BROADCAST"));
            }
        });
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.chuxiao.androidbroadcasttest.LOCAL_BROADCAST");
        localReceiver = new LocalReceiver();
        //注册本地广播监听器
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 动态注册的广播接收器一定要取消注册
//        unregisterReceiver(networkChangeReceiver);
        // 注销本地广播管理器
        localBroadcastManager.unregisterReceiver(localReceiver);
    }

//    /**
//     * 系统级广播接收器
//     */
//    class NetworkChangeReceiver extends BroadcastReceiver {
//        /**
//         * 每当网络状态发生改变时，onReceive方法都会得到执行
//         */
//        @Override
//        public void onReceive(Context context, Intent intent) {
////            Toast.makeText(context, "网络状态发生改变", Toast.LENGTH_SHORT).show();
//            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//            if (networkInfo != null && networkInfo.isAvailable()) {
//                Toast.makeText(context, "网络可用", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(context, "网络不可用", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }


    /**
     * 本地广播接收器
     */
    class LocalReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "接收到本地广播", Toast.LENGTH_SHORT).show();
        }
    }
}
