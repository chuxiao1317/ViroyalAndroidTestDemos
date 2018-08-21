package com.chuxiao.activitylifecycletest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        Toast.makeText(MainActivity.this, TAG + "onCreate", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_main);
        // 当活动onStop后，activity有可能因为内存原因被系统回收，此时用户若返回此活动，就会调用onCreate方法，此处可恢复之前的临时数据
        if (savedInstanceState != null) {
            String tempData = savedInstanceState.getString("data_key");
            Log.d(TAG, tempData);
            Toast.makeText(MainActivity.this, tempData, Toast.LENGTH_SHORT).show();
        } else {
            // 刚启动应用的时候可收到此提示，因为此时刚调用过onCreate()方法
            Toast.makeText(MainActivity.this, "并没有收到保存数据", Toast.LENGTH_SHORT).show();
        }
        // Button用于启动活动
        Button startNormalActivity = findViewById(R.id.start_normal_activity);
        Button startDialogActivity = findViewById(R.id.start_dialog_activity);
        // 启动normalActivity
        startNormalActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NormalActivity.class);
                startActivity(intent);
            }
        });
        //启动DialogActivity
        startDialogActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DialogActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 保存临时数据以免活动跳转丢失
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String tempData = "你刚刚保存的数据";
        outState.putString("data_key", tempData);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(MainActivity.this, TAG + "onStart", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(MainActivity.this, TAG + "onResume", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(MainActivity.this, TAG + "onPause", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(MainActivity.this, TAG + "onStop", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        Toast.makeText(MainActivity.this, TAG + "onDestroy", Toast.LENGTH_SHORT).show();
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(MainActivity.this, TAG + "onRestart", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onRestart");
    }
}
