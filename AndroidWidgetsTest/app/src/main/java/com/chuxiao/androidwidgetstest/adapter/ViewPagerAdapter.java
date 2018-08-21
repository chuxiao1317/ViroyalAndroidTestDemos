package com.chuxiao.androidwidgetstest.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {

    private List<ImageView> tempImgList = new ArrayList<>();
    private List<Integer> mPagerContentIdList;
    private List<View> viewList;
//    private Context mContext;

//    public ViewPagerAdapter(Context context, List<Integer> list) {
//        this.mPagerContentIdList = list;
//        this.mContext = context;
//    }

    public ViewPagerAdapter(/*Context context,*/ List<View> list) {
        this.viewList = list;
//        this.mContext = context;
    }

//    @Override
//    public int getCount() {
//        return mPagerContentIdList == null ? 0 : mPapgerContentIdList.size();
//    }

    @Override
    public int getCount() {
        return viewList == null ? 0 : viewList.size();
    }

    /**
     * 判断object是否和view相等，如果相等ViewPager就显示view
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        // 判断是否属于同一对象
        return view == object;
    }

    /**
     * 初始化当前显示View
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(viewList.get(position));
        return viewList.get(position);
    }

//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        ImageView imageView;
//        if (tempImgList.size() > 0) {
//            imageView = tempImgList.get(0);
//            tempImgList.remove(0);
//        } else {
//            imageView = new ImageView(mContext);
//        }
//        imageView.setImageResource(mPapgerContentIdList.get(position));
//        //图片自动拉伸匹配
//        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//        container.addView(imageView);
//        return imageView;
//    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
        container.removeView((View) object);
//        tempImgList.add((ImageView) object);
    }
}
