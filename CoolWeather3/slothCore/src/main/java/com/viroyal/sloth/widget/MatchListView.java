package com.viroyal.sloth.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by qjj on 2016/7/27.
 */
public class MatchListView extends ListView{
    public MatchListView(Context context) {
        super(context);
    }

    public MatchListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MatchListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO 自动生成的方法存根
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
