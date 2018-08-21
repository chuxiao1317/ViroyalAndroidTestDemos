package com.viroyal.sloth.widget.viewpager;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shengpeng on 2016-04-19.
 */

public class SimpleLoopPagerAdapter extends PagerAdapter {

    private List<View> listViews = new ArrayList<>();

    public SimpleLoopPagerAdapter() {
    }

    public void setListViews(List<View> views) {
        listViews = views;
    }

    @Override
    public int getCount() {
        return listViews != null ? listViews.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        try {
            container.addView(listViews.get(position));
        } catch (Exception e) {
            // TODO: handle exception
        }
        return listViews.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
    }
}