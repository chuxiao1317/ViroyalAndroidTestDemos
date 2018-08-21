package com.chuxiao.androidtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ThirdActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("创建实例ThirdActivity", "任务(返回栈)id是:" + getTaskId());
        setContentView(R.layout.activity_third);
        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 *测试调用ActivityController里面的方法，随时随地可快速退出应用
                 * */
                ActivityController.finishAll();
            }
        });
    }
}
