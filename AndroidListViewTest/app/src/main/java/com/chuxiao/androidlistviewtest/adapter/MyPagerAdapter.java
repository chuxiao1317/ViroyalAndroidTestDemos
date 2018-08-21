package com.chuxiao.androidlistviewtest.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chuxiao.androidlistviewtest.R;

import java.util.ArrayList;
import java.util.List;

public class MyPagerAdapter extends PagerAdapter {
    public List<ImageView> cache = new ArrayList<>();
    private int[] list;
    private Context mContext;

    public MyPagerAdapter(Context context, int[] list) {
        this.mContext = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.length;
    }

    //判断object是否和view相等，如果相等ViewPager就显示view
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object == view;
    }

    //初始化当前显示的View
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = null;
        if (cache.size() > 0) {
            imageView = cache.get(0);
            cache.remove(0);
        } else {
            imageView = new ImageView(mContext);
        }
        imageView.setBackgroundResource(list[position]);
        //图片自动拉伸匹配
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//        LayoutInflater.from(mContext).inflate(R.layout)
        container.addView(imageView);
        return imageView;
    }

    //View销毁的时候调用
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
        container.removeView((View) object);
        cache.add((ImageView) object);
    }
}
