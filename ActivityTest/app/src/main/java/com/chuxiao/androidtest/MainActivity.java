package com.chuxiao.androidtest;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.d("MainAcrivity", this.toString());
        Log.d("创建实例MainActivity", "任务(返回栈)id是:" + getTaskId());
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              finish();
                // 测试启动模式
//                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);

//                String data = "活动间传递数据";
//                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
//                intent.putExtra("extra_data", data);
                // 请求码只要是一个唯一值就行，如：1
//                startActivityForResult(intent, 1);

//                Intent intent = new Intent("com.chuxiao.androidtest.ACTION_START");
//                intent.addCategory("com.chuxiao.androidtest.MY_CATEGORY");

//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setAction("android.intent.action.VIEW");
//                Intent intent = new Intent(Intent.ACTION_DIAL);
//                intent.setData(Uri.parse("tel:10086"));
//                intent.setData(Uri.parse("http://www.baidu.com"));
                startActivity(intent);

//                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
//                startActivity(intent);

                /**
                 * 启动活动最简便的方法，见SecondActivity
                 * */
//                SecondActivity.actionStart(MainActivity.this, "data1", "data2");
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(this, "MainActivity onRestart", Toast.LENGTH_SHORT).show();
        Log.d("MainActivity", "onRestart");
    }

    /**
     * 得到返回数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String returnedData = data.getStringExtra("data_return");
                    Log.d("MainActivity", returnedData);
                }
                break;
            default:
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * 菜单
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_item:
                // Toast弹框
                Toast.makeText(this, "你点击了add", Toast.LENGTH_SHORT).show();
                break;
            case R.id.remove_item:
                Toast.makeText(this, "你点击了remove", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
    }
}
