package com.chuxiao.androidlistviewtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.chuxiao.androidlistviewtest.adapter.MyRecyAdapter;
import com.chuxiao.androidlistviewtest.entity.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 12525 on 2018/4/12.
 */

public class RecyclerViewActivity extends AppCompatActivity{

    private RecyclerView rv;
    private MyRecyAdapter adapter;
    private List<Student> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Student s = new Student("张三" + i, "男", i);
            list.add(s);
        }
        rv = ((RecyclerView) findViewById(R.id.rv));
        //RecyclerView必须添加Manager才能显示
//        rv.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.HORIZONTAL));
//        rv.addItemDecoration(new DividerItemDecoration(RecyclerViewActivity.this,DividerItemDecoration.VERTICAL));
        rv.setLayoutManager(new LinearLayoutManager(RecyclerViewActivity.this,LinearLayoutManager.HORIZONTAL,false));
//        rv.setLayoutManager(new GridLayoutManager());
        adapter = new MyRecyAdapter(RecyclerViewActivity.this, list);
        rv.setAdapter(adapter);
    }

    public void cli(View view){
        for (int i = 100; i < 200; i++) {
            Student s = new Student("张三" + i, "男", i);
            list.add(s);
        }
        adapter.notifyDataSetChanged();
    }

    public void clickViewPager(View view){
        startActivity(new Intent(RecyclerViewActivity.this,ViewPagerActivity.class));
    }
}
