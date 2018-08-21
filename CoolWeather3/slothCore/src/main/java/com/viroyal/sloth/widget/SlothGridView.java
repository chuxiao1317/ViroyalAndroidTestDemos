package com.viroyal.sloth.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

import com.sloth.core.R;

/**
 * Created by qjj on 2016/8/19.
 */
public class SlothGridView extends GridView {

    private boolean showLine;

    public SlothGridView(Context context) {
        super(context);
    }

    public SlothGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlothGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO 自动生成的构造函数存根
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO 自动生成的方法存根
        int expandSpec = View.MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    private void initProperty(Context context, AttributeSet attributeSet) {
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.gridview);
        showLine = typedArray.getBoolean(R.styleable.gridview_showline, false);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (showLine) {
            View localView1 = getChildAt(0);
            int column = getWidth() / localView1.getWidth();//计算出一共有多少列，假设有3列
            int childCount = getChildCount();//子view的总数
            Paint localPaint;//画笔
            localPaint = new Paint();
            localPaint.setStyle(Paint.Style.STROKE);
            localPaint.setColor(getContext().getResources().getColor(R.color.c5));//设置画笔的颜色
            for (int i = 0; i < childCount; i++) {//遍历子view
                View cellView = getChildAt(i);//获取子view
                if (i < 3) {//第一行
                    canvas.drawLine(cellView.getLeft(), cellView.getTop(), cellView.getRight(), cellView.getTop(), localPaint);
                }
                if (i % column == 0) {//第一列
                    canvas.drawLine(cellView.getLeft(), cellView.getTop(), cellView.getLeft(), cellView.getBottom(), localPaint);
                }
                if ((i + 1) % column == 0) {//第三列
                    //画子view底部横线
                    canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(), cellView.getBottom(), localPaint);
                    canvas.drawLine(cellView.getRight(), cellView.getTop(), cellView.getRight(), cellView.getBottom(), localPaint);
                } else if ((i + 1) > (childCount - (childCount % column))) {//如果view是最后一行
                    //画子view的右边竖线
                    canvas.drawLine(cellView.getRight(), cellView.getTop(), cellView.getRight(), cellView.getBottom(), localPaint);
                    canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(), cellView.getBottom(), localPaint);
                } else {//如果view不是最后一行
                    //画子view的右边竖线
                    canvas.drawLine(cellView.getRight(), cellView.getTop(), cellView.getRight(), cellView.getBottom(), localPaint);
                    //画子view的底部横线
                    canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(), cellView.getBottom(), localPaint);
                }
            }
        }
    }
}
