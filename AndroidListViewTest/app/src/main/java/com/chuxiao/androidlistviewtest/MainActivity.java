package com.chuxiao.androidlistviewtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.chuxiao.androidlistviewtest.adapter.MyAdapter;
import com.chuxiao.androidlistviewtest.entity.Student;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String[] data = {
            "中国", "美国", "日本", "新加坡", "墨西哥", "印度", "法国", "德国", "英国", "意大利", "澳大利亚", "俄罗斯", "乌克兰", "伊拉克", "阿拉伯", "葡萄牙"
    };
    private ListView mListView;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //SimpleAapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, data);
        mListView = (ListView)findViewById(R.id.list_view);


        List<Student> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Student s = new Student("张三" + i, "男", i);
            list.add(s);
        }
        myAdapter = new MyAdapter(MainActivity.this, list);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this,"item的点击事件",Toast.LENGTH_SHORT).show();
            }
        });
        mListView.setAdapter(myAdapter);
    }

    public void click(View view){
        startActivity(new Intent(MainActivity.this,RecyclerViewActivity.class));
    }
}
