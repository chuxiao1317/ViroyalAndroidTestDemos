package com.chuxiao.androidwidgetstest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.chuxiao.androidwidgetstest.adapter.RecyclerAdapter_WaterfallStyle;
import com.chuxiao.androidwidgetstest.entity.Fruit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RecyclerView_WaterfallStyle_Activity extends AppCompatActivity {

    private List<Fruit> fruitList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_waterfall);
        initFruits();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle_view3);
//        RecyclerView必须添加Manager才能显示
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerAdapter_WaterfallStyle adapter = new RecyclerAdapter_WaterfallStyle(fruitList);
        recyclerView.setAdapter(adapter);
    }

    private void initFruits() {
        for (int i = 0; i < 5; i++) {
            fruitList.add(new Fruit(getRandomLengthName("苹果"), R.drawable.ic_launcher_background));
            fruitList.add(new Fruit(getRandomLengthName("橘子"), R.drawable.ic_launcher_background));
            fruitList.add(new Fruit(getRandomLengthName("芒果"), R.drawable.ic_launcher_background));
            fruitList.add(new Fruit(getRandomLengthName("葡萄"), R.drawable.ic_launcher_background));
            fruitList.add(new Fruit(getRandomLengthName("橙子"), R.drawable.ic_launcher_background));
        }
    }

    private String getRandomLengthName(String name) {
        Random random = new Random();
        int length = random.nextInt(20) + 1;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(name);
        }
        return builder.toString();
    }

    public void click(View view) {
        startActivity(new Intent(RecyclerView_WaterfallStyle_Activity.this, FragmentTestActivity.class));
    }
}
