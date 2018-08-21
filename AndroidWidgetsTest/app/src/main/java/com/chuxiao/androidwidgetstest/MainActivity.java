package com.chuxiao.androidwidgetstest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.chuxiao.androidwidgetstest.adapter.ListViewAdapter;
import com.chuxiao.androidwidgetstest.entity.Fruit;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Fruit> fruitList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFruit();
        ListViewAdapter adapter = new ListViewAdapter(MainActivity.this, R.layout.fruit_item, fruitList);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "整个Item的点击事件:", Toast.LENGTH_SHORT).show();
            }
        });
        listView.setAdapter(adapter);

    }

    private void initFruit() {
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
        startActivity(new Intent(MainActivity.this, RecyclerViewActivity.class));
    }
}
