package com.chuxiao.scrollviewtest.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Scroller;

import com.chuxiao.scrollviewtest.R;

import butterknife.Bind;

public class DragView extends View {

    @Bind(R.id.scroll_bt)
    Button button;

    // 用于记录触摸点坐标
    private int firstX;
    private int firstY;
    private int firstRawX;
    private int firstRawY;

    private Scroller mScroller;

    public DragView(Context context) {
        super(context);
        mScroller = new Scroller(context);
    }

    public DragView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
//        View layout = LayoutInflater.from(context).inflate(R.layout.activity_main, null, false);
//        button = layout.findViewById(R.id.scroll_bt);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 获取触摸点坐标（视图坐标）
        int x = (int) event.getX();
        int y = (int) event.getY();

        // 获取触摸点的绝对坐标
        int rawX = (int) event.getRawX();
        int rawY = (int) event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 记录触摸点坐标
                firstX = x;
                firstY = y;
                firstRawX = rawX;
                firstRawY = rawY;
                break;
            case MotionEvent.ACTION_MOVE:
                // 计算偏移量
                int offsetX = x - firstX;
                int offsetY = y - firstY;
                int offsetRawX = rawX - firstRawX;
                int offsetRawY = rawY - firstRawY;

                /**
                 * 移动
                 * */
                // 方法一：在当前left、top、right、bottom基础上加上偏移量
//                layout(getLeft() + offsetX,
//                        getTop() + offsetY,
//                        getRight() + offsetX,
//                        getBottom() + offsetY);
                // 使用绝对坐标系
//                layout(getLeft() + offsetRawX,
//                        getTop() + offsetRawY,
//                        getRight() + offsetRawX,
//                        getBottom() + offsetRawY);
//                // 在使用绝对坐标移动时，每次执行完移动操作都要重新设置初始坐标，否则不管此时View在何处，系统都将以屏幕左上角为原点
//                firstRawX = rawX;
//                firstRawY = rawY;
                //方法二：同时对left、right或者top、bottom进行偏移
//                offsetLeftAndRight(offsetX);
//                offsetTopAndBottom(offsetY);
                //方法三：修改布局的位置参数
//                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
//                layoutParams.leftMargin = getLeft() + offsetX;
//                layoutParams.topMargin = getTop() + offsetY;
//                setLayoutParams(layoutParams);
                // 方法四：scrollTo（指定位置（x,y））和scrollBy（偏移量）：移动cotent的位置，如textView移动的是字体，而不是整个textView
                //scrollBy：可以理解为是手机屏幕相对于视图在移动，故需将值设为相反数才能达到想要的效果
                ((View) getParent()).scrollBy(-offsetX, -offsetY);
                //scrollTo：同理scrollBy,此处存在bug，未完成
//                ((View) getParent()).scrollTo(-offsetRawX, -offsetRawY);
////                Toast.makeText(getContext(), "两个偏移量的相等关系：" + (offsetRawX == offsetX), Toast.LENGTH_SHORT).show();
//                // 在使用绝对坐标移动时，每次执行完移动操作都要重新设置初始坐标，否则不管此时View在何处，系统都将以屏幕左上角为原点
//                firstRawX = rawX;
//                firstRawY = rawY;
                break;
            case MotionEvent.ACTION_UP:
//                // 方法五：Scroller：手指离开时，执行滑动过程，执行此处需要将方法四纵的scrollBy注释打开
//                View viewGroup = (View) getParent();
//                // startScroll只是个计算器，负责提供平滑移动的动画属性，真正滑动的过程是computeScroll中的scrollTo
//                mScroller.startScroll(
//                        viewGroup.getScrollX(),// 原点横坐标
//                        viewGroup.getScrollY(),// 原点纵坐标
//                        -viewGroup.getScrollX(),// 偏移量
//                        -viewGroup.getScrollY()// 偏移量
//                );
//                invalidate();
                break;
        }
        return true;
    }

    public void scroll() {
        // 方法五：Scroller：点击按钮执行平滑移动
        View viewGroup = (View) getParent();
        // startScroll只是个计算器，负责提供平滑移动的动画属性，真正滑动的过程是computeScroll中的scrollTo
        mScroller.startScroll(
                viewGroup.getScrollX(),// 原点横坐标
                viewGroup.getScrollY(),// 原点纵坐标
                -200,// 偏移量
                -200// 偏移量
        );
        invalidate();
    }

    /**
     * 方法五：Scroller平滑移动，需重写此方法实现模拟滑动（真正的滑动动画过程）
     */
    @Override
    public void computeScroll() {
        super.computeScroll();
        // 判断Scroller是否执行完毕
        if (mScroller.computeScrollOffset()) {
            ((View) getParent()).scrollTo(mScroller.getCurrX(),// startScroll中的偏移量X
                    mScroller.getCurrY());// startScroll中的偏移量Y
            // 通过重绘来不断调用computeScroll
            invalidate();
        }
    }
}
