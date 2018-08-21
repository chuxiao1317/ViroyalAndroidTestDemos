package com.chuxiao.androidlistviewtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.chuxiao.androidlistviewtest.adapter.MyPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 12525 on 2018/4/12.
 */

public class ViewPagerActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MyPagerAdapter myPagerAdapter;
    public int[] list = new int[]{R.mipmap.ic_launcher,R.mipmap.f,R.mipmap.huazai3};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);

        viewPager = ((ViewPager) findViewById(R.id.viewpager));
//        List<String> list = new ArrayList<>();
//        for (int i = 0; i < 4; i++) {
//            list.add("这是第" + i + "页");
//        }
        myPagerAdapter = new MyPagerAdapter(this, list);

        viewPager.setAdapter(myPagerAdapter);
    }

    public void clickFragment(View view){
        startActivity(new Intent(ViewPagerActivity.this,Fragment1Activity.class));
    }
}
