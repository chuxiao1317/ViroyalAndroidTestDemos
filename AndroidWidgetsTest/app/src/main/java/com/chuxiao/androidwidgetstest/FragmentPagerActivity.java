package com.chuxiao.androidwidgetstest;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.chuxiao.androidwidgetstest.adapter.MyFragmentPagerAdapter;
import com.chuxiao.androidwidgetstest.fragment.LeftFragment;
import com.chuxiao.androidwidgetstest.fragment.RightFragment;

import java.util.ArrayList;
import java.util.List;

public class FragmentPagerActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_pager);
        viewPager = (ViewPager) findViewById(R.id.vp);
        LeftFragment leftFragment = new LeftFragment();
        leftFragment.setType("fragmentPager");
        fragments.add(leftFragment);
        fragments.add(new RightFragment());
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //滑动
            }

            @Override
            public void onPageSelected(int position) {
                //选择当前页
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //滚动
                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    //滚动
                } else if (state == ViewPager.SCROLL_STATE_IDLE) {
                    //静止
                } else if (state == ViewPager.SCROLL_STATE_SETTLING) {
                    //viewpager解决了最后一个位置的过程
                }
            }
        });
        viewPager.setAdapter(adapter);
    }

    public void click1(View view) {
        //平滑滚动过去
        viewPager.setCurrentItem(0, true);
    }

    public void click2(View view) {
        // 闪现过去
        viewPager.setCurrentItem(1, false);
    }
}
