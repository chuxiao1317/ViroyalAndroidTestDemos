package com.viroyal.sloth.widget.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by cxy_nj on 2016/8/15.
 */
public class ShowTotalRecyclerView extends RecyclerView{

    public ShowTotalRecyclerView(Context context) {
        super(context);
    }

    public ShowTotalRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShowTotalRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            return true;  //禁止recyclerview滑动
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
