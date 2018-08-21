package com.chuxiao.androidtest;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SecondActivity extends BaseActivity {

    /**
     * 启动该活动的最佳写法，其他活动启动此活动时只需调用此方法即可
     */
    public static void actionStart(Context context, String data1, String data2) {
        Intent intent = new Intent(context, SecondActivity.class);
        intent.putExtra("param1", data1);
        intent.putExtra("param2", data2);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("创建实例SecondActivity", "任务(返回栈)id是:" + getTaskId());
        setContentView(R.layout.activity_second);
        Button button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
//                Intent intent = new Intent(SecondActivity.this, MainActivity.class);
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse("http://www.baidu.com"));

//                Intent intent = new Intent(Intent.ACTION_DIAL);
//                intent.setData(Uri.parse("tel:10086"));

//                Intent intent = getIntent();
//                String data = intent.getStringExtra("extra_data");
//                Log.d("SecondActivity", data);

//                Intent intent = new Intent();
//                intent.putExtra("data_return", "返回数据");
//                setResult(RESULT_OK, intent);
//                finish();

                startActivity(intent);
            }
        });

    }

    @Override
    protected void onDestroy() {
        Toast.makeText(this, "SecondActivity onDestroy", Toast.LENGTH_SHORT).show();
        super.onDestroy();
        Log.d("SecondActivity", "onDestroy");
    }

    /**
     * Back键返回数据（防止用户不点击返回按钮而是Back键使得数据无法返回）
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("data_return", "返回数据");
        setResult(RESULT_OK, intent);
        finish();
    }
}
