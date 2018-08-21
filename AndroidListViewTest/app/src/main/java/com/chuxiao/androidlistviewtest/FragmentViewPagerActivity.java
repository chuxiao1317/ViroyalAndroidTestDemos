package com.chuxiao.androidlistviewtest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.chuxiao.androidlistviewtest.adapter.MyFragmentPagerAdapter;
import com.chuxiao.androidlistviewtest.fragment.FirstFragment;
import com.chuxiao.androidlistviewtest.fragment.SecondFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 12525 on 2018/4/12.
 */

public class FragmentViewPagerActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private List<Fragment> fragments = new ArrayList<>();
    private MyFragmentPagerAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_viewpager);

        viewPager = ((ViewPager) findViewById(R.id.vp));
        FirstFragment firstFragent = new FirstFragment();
        firstFragent.setParmas("viewpagerFragment");
        fragments.add(firstFragent);
        fragments.add(new SecondFragment());
        adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //滑动
            }

            @Override
            public void onPageSelected(int position) {
                //选择的当前页
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //滚动
                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    //滑动

                } else if (state == ViewPager.SCROLL_STATE_IDLE) {
                    //静止

                } else if (state == ViewPager.SCROLL_STATE_SETTLING) {

                }
            }
        });
        viewPager.setAdapter(adapter);
    }


    public void clickkk1(View view) {
        viewPager.setCurrentItem(0,true);//疾走过去
    }

    public void clickkk2(View view) {
        viewPager.setCurrentItem(1,false);//闪现过去
    }
}
