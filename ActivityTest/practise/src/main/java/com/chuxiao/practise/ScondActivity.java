package com.chuxiao.practise;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.hwangjr.rxbus.RxBus;

public class ScondActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        RxBus.get().register(this);

        textView = ((TextView) findViewById(R.id.tv));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送消息
                RxBus.get().post("tag","ssss");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(this);
    }
}
