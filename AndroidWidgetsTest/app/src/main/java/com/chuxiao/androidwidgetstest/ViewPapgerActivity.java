package com.chuxiao.androidwidgetstest;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.chuxiao.androidwidgetstest.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPapgerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_papger);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        List<View> viewList = new ArrayList<>();
        viewList.add(getLayoutInflater().inflate(R.layout.view_pager_item_layout1, null, false));
        viewList.add(getLayoutInflater().inflate(R.layout.view_pager_item_layout2, null, false));
//        List<Integer> imgIdList = new ArrayList<>();
//        imgIdList.add(R.mipmap.f);
//        imgIdList.add(R.mipmap.huazai3);
//        imgIdList.add(R.mipmap.ic_launcher);
//        ViewPagerAdapter adapter = new ViewPagerAdapter(this,imgIdList);
        ViewPagerAdapter adapter = new ViewPagerAdapter(viewList);
        viewPager.setAdapter(adapter);
    }

    public void clickFragment(View view) {
        startActivity(new Intent(this, FragmentPagerActivity.class));
    }
}
