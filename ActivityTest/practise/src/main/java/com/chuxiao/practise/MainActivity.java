package com.chuxiao.practise;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;

import org.kymjs.kjframe.KJDB;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RxBus.get().register(this);

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.d("Environment", Environment.getDataDirectory().getAbsolutePath());
        }
        //    /data/data/包名/database
        //内存     /data/data/包名/文件名
        //KJDB    //        getDatabasePath()
        //SharePreference    /data/data/包名/shared_prefs

        SharedPreferences sp = getSharedPreferences("sss", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("result", "asd");
        edit.commit();


        String result = sp.getString("result", "-1");
        Log.d("result", result);
    }

    public void click(View view) {
        startActivity(new Intent(MainActivity.this, ScondActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(this);
    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = @Tag("tag")
    )
    public void callback(String result) {
        Log.d("callback", result);
    }
}
