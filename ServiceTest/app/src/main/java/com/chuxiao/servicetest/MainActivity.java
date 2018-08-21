package com.chuxiao.servicetest;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private MyService.DownloadBinder downloadBinder;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder = (MyService.DownloadBinder) service;
            downloadBinder.startDownload();
            downloadBinder.getProgress();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startServiceBtn = findViewById(R.id.start_service_btn);
        Button stopServiceBtn = findViewById(R.id.stop_service_btn);
        Button bindServiceBtn = findViewById(R.id.bind_service_btn);
        Button unbindServiceBtn = findViewById(R.id.unbind_service_btn);

        startServiceBtn.setOnClickListener(this);
        stopServiceBtn.setOnClickListener(this);
        bindServiceBtn.setOnClickListener(this);
        unbindServiceBtn.setOnClickListener(this);

        Button startIntentServiceBtn = findViewById(R.id.start_intent_service_btn);
        startIntentServiceBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_service_btn:
                // 启动服务
                startService(new Intent(this, MyService.class));
                break;
            case R.id.stop_service_btn:
                // 停止服务
                stopService(new Intent(this, MyService.class));
                break;
            case R.id.bind_service_btn:
                // 绑定服务
                bindService(new Intent(this, MyService.class), connection, BIND_AUTO_CREATE);
                break;
            case R.id.unbind_service_btn:
                // 解绑服务
                unbindService(connection);
                break;
            case R.id.start_intent_service_btn:
                Toast.makeText(this, "主线程id：" + Thread.currentThread().getId(), Toast.LENGTH_SHORT).show();
                Log.d("MainActivity", "主线程id：" + Thread.currentThread().getId());
                // 用IntentService启动服务（已默认开启子线程）
                startService(new Intent(this, MyIntentService.class));
                break;
            default:
                break;
        }
    }
}
