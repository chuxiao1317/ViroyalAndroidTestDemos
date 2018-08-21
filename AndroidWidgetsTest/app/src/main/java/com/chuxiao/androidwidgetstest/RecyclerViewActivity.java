package com.chuxiao.androidwidgetstest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chuxiao.androidwidgetstest.adapter.RecyclerAdapter;
import com.chuxiao.androidwidgetstest.entity.Fruit;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewActivity extends AppCompatActivity {

    private List<Fruit> fruitList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        initFruits();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
//        RecyclerView必须添加Manager才能显示
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //让每个item横向布局
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerAdapter adapter = new RecyclerAdapter(fruitList);
        recyclerView.setAdapter(adapter);
    }

    private void initFruits() {
        for (int i = 0; i < 5; i++) {
            fruitList.add(new Fruit("苹果", R.drawable.ic_launcher_background));
            fruitList.add(new Fruit("橘子", R.drawable.ic_launcher_background));
            fruitList.add(new Fruit("芒果", R.drawable.ic_launcher_background));
            fruitList.add(new Fruit("葡萄", R.drawable.ic_launcher_background));
            fruitList.add(new Fruit("橙子", R.drawable.ic_launcher_background));
            fruitList.add(new Fruit("酸梅", R.drawable.ic_launcher_background));
        }
    }

    public void click(View view) {
        startActivity(new Intent(RecyclerViewActivity.this, RecyclerView_WaterfallStyle_Activity.class));
    }
}
