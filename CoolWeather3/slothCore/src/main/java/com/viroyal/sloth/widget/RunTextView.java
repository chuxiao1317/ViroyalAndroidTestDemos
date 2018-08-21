package com.viroyal.sloth.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.sloth.core.R;

/**
 * Created by Brioal on 2016/5/28.
 */

public class RunTextView extends TextView {
    private int mDuration; //文字从出现到显示消失的时间
    private int mInterval; //文字停留在中间的时长切换的间隔
    private List<ADEnity> mTexts; //显示文字的数据源
    private int mY = 0; //文字的Y坐标
    private int mIndex = 0; //当前的数据下标
    private Paint mPaintBack; //绘制内容的画笔
    private Paint mPaintFront; //绘制前缀的画笔
    private boolean isMove = true; //文字是否移动
    private String TAG = "ADTextView";
    private boolean hasInit = false;

    public interface onClickLitener {
        public void onClick(ADEnity item);
    }

    private onClickLitener onClickLitener;

    public void setOnClickLitener(RunTextView.onClickLitener onClickLitener) {
        this.onClickLitener = onClickLitener;
    }

    public RunTextView(Context context) {
        this(context, null);
    }

    public RunTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (onClickLitener != null) {
                    //onClickLitener.onClick(mTexts.get(mIndex).getmUrl());
                    onClickLitener.onClick(mTexts.get(mIndex));
                }

                break;
        }
        return true;
    }

    //设置数据源
    public void setmTexts(List mTexts) {
        this.mTexts = mTexts;
    }

    //设置广告文字的停顿时间
    public void setmInterval(int mInterval) {
        this.mInterval = mInterval;
    }

    //设置文字从出现到消失的时长
    public void setmDuration(int mDuration) {
        this.mDuration = mDuration;
    }

    //设置前缀的文字颜色
    public void setFrontColor(int mFrontColor) {
        mPaintFront.setColor(mFrontColor);
    }

    //设置正文内容的颜色
    public void setBackColor(int mBackColor) {
        mPaintBack.setColor(mBackColor);
    }

    //初始化默认值
    private void init(Context context, AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.runtextview);
        int titleColor = typedArray.getColor(R.styleable.runtextview_titleColor, 0XFF999999);
        int textSize = typedArray.getDimensionPixelSize(R.styleable.runtextview_runTextSize, 30);
        int contentColor = typedArray.getColor(R.styleable.runtextview_contentColor, 0XFF303030);
        mDuration = 500;
        mInterval = 1000;
        mIndex = 0;
        mPaintFront = new Paint();
        mPaintFront.setAntiAlias(true);
        mPaintFront.setDither(true);
        mPaintFront.setTextSize(textSize);

        mPaintBack = new Paint();
        mPaintBack.setAntiAlias(true);
        mPaintBack.setDither(true);
        mPaintBack.setTextSize(textSize);

        mPaintFront.setColor(titleColor);
        mPaintBack.setColor(contentColor);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //Log.i(TAG, "onSizeChanged: " + h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mTexts != null) {
            //Log.i(TAG, "onDraw: " + mY);
            ADEnity model = mTexts.get(mIndex);
            String font = model.getmFront();
            String back = model.getmBack();
            //绘制前缀
            Rect indexBound = new Rect();
            mPaintFront.getTextBounds(font, 0, font.length(), indexBound);

            //绘制内容文字
            Rect contentBound = new Rect();
            mPaintBack.getTextBounds(back, 0, back.length(), contentBound);
            if (mY == 0 && hasInit == false) {
                mY = getMeasuredHeight() - indexBound.top;
                hasInit = true;
            }
            //移动到最上面
            if (mY == 0 - indexBound.bottom) {
                //Log.i(TAG, "onDraw: " + getMeasuredHeight());
                mY = getMeasuredHeight() - indexBound.top;
                mIndex++;
            }
            canvas.drawText(back, 0, back.length(), (indexBound.right - indexBound.left) + 20, mY, mPaintBack);
            canvas.drawText(font, 0, font.length(), 10, mY, mPaintFront);
            //移动到中间
            if (mY == getMeasuredHeight() / 2 - (indexBound.top + indexBound.bottom) / 2) {
                isMove = false;
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        postInvalidate();
                        isMove = true;
                    }
                }, mInterval);
            }
            mY -= 1;
            //循环使用数据
            if (mIndex == mTexts.size()) {
                mIndex = 0;
            }
            //如果是处于移动状态时的,则延迟绘制
            //计算公式为一个比例,一个时间间隔移动组件高度,则多少毫秒来移动1像素
            if (isMove) {
                postInvalidateDelayed(mDuration / getMeasuredHeight());
            }
        }

    }

    public static class ADEnity{
        private String mFront ; //前面的文字
        private String mBack ; //后面的文字
        private String mUrl ;//包含的链接

        public static ADEnity getADEnity(String mFront, String mBack,String mUrl){
            return new ADEnity(mFront,mBack,mUrl);
        }
        public ADEnity(String mFront, String mBack,String mUrl) {
            this.mFront = mFront;
            this.mBack = mBack;
            this.mUrl = mUrl;
        }

        public String getmUrl() {
            return mUrl;
        }

        public void setmUrl(String mUrl) {
            this.mUrl = mUrl;
        }

        public String getmFront() {
            return mFront;
        }

        public void setmFront(String mFront) {
            this.mFront = mFront;
        }

        public String getmBack() {
            return mBack;
        }

        public void setmBack(String mBack) {
            this.mBack = mBack;
        }
    }
}