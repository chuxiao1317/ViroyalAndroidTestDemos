package com.chuxiao.scrollviewtest.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.chuxiao.scrollviewtest.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 12525 on 2018/6/7.
 */

public class DragViewGroup extends FrameLayout {

    private ViewDragHelper mViewDragHelper;
//    @Bind(R.id.menu_view)
    public View mMenuView;
//    @Bind(R.id.main_view)
    public View mMainView;
    private int mWidth;

    public DragViewGroup(@NonNull Context context) {
        super(context);
        initView();
    }

    public DragViewGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DragViewGroup(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mViewDragHelper = ViewDragHelper.create(this, callback);
    }

    /**
     * 自定义ViewGroup
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
//        View view = View.inflate(getContext(),R.id.main_view,this);
//        View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_second, this, false);
//        ButterKnife.bind(this, view);
        mMenuView = getChildAt(0);
        mMainView = getChildAt(1);
//        mMainView = view.findViewById(R.id.main_view);
//        mMenuView = view.findViewById(R.id.menu_view);
    }

    /**
     * 当指定元素大小发生变化的时候调用
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = mMenuView.getMeasuredWidth();
    }

    /**
     * 拦截触摸事件，好在onTouchEvent方法中通过processTouchEvent将事件传递给mViewDragHelper处理
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 将触摸事件传递给ViewDragHelper
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        // 何时开始检测触摸事件
        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            // 触摸mMainMenu时开始检测,只有此控件可被拖动
            return mMainView == child;
        }

        //处理水平滑动
        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            return left;
        }

        // 处理垂直滑动
        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            return 0;
        }

        // 拖动结束后调用
        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            // 手指抬起后缓慢移动到指定位置
            if (mMainView.getLeft() < 300) {
                // 关闭菜单，相当于Scroller的startScroller方法
                mViewDragHelper.smoothSlideViewTo(mMainView, 0, 0);
                // 重绘相当于startScroll里面的invalidate
                ViewCompat.postInvalidateOnAnimation(DragViewGroup.this);
            } else {
                // 打开菜单
                mViewDragHelper.smoothSlideViewTo(mMainView, 300, 0);
                // 重绘相当于startScroll里面的invalidate
                ViewCompat.postInvalidateOnAnimation(DragViewGroup.this);
            }
        }
    };

    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
}
