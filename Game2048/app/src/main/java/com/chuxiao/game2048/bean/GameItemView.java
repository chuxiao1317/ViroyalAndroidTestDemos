package com.chuxiao.game2048.bean;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chuxiao.game2048.config.Config;

public class GameItemView extends FrameLayout {
    private int mCardShowNum;
    private TextView mTvNum;
    private LayoutParams mLayoutParams;

    public GameItemView(@NonNull Context context, int cardShowNum) {
        super(context);
        this.mCardShowNum = cardShowNum;
        initCardItem();
    }

    public View getItemTv() {
        return mTvNum;
    }

    public int getCardShowNum() {
        return mCardShowNum;
    }

    private void initCardItem() {
        setBackgroundColor(Color.GRAY);
        mTvNum = new TextView(getContext());
        setNum(mCardShowNum);
        // 修改5x5时字体太大
        int gameLines = Config.mSp.getInt(Config.keyGameLines, 4);
        if (gameLines == 4) {
            mTvNum.setTextSize(35);
        } else if (gameLines == 5) {
            mTvNum.setTextSize(25);
        } else {
            mTvNum.setTextSize(20);
        }
        TextPaint textPaint = mTvNum.getPaint();
        textPaint.setFakeBoldText(true);
        mTvNum.setGravity(Gravity.CENTER);

        mLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        mLayoutParams.setMargins(5, 5, 5, 5);
        addView(mTvNum, mLayoutParams);
    }

    public void setNum(int num) {
        this.mCardShowNum = num;
        if (num == 0) {
            mTvNum.setText("");
        } else {
            mTvNum.setText(String.valueOf(num));
        }
        // 设置背景颜色
        switch (num) {
            case 0:
                mTvNum.setBackgroundColor(0x00000000);
                break;
            case 2:
                mTvNum.setBackgroundColor(0xffeee5db);
                break;
            case 4:
                mTvNum.setBackgroundColor(0xffeee0ca);
                break;
            case 8:
                mTvNum.setBackgroundColor(0xfff2c17a);
                break;
            case 16:
                mTvNum.setBackgroundColor(0xfff59667);
                break;
            case 32:
                mTvNum.setBackgroundColor(0xfff68c6f);
                break;
            case 64:
                mTvNum.setBackgroundColor(0xfff66e3c);
                break;
            case 128:
                mTvNum.setBackgroundColor(0xffedcf74);
                break;
            case 256:
                mTvNum.setBackgroundColor(0xffedcc64);
                break;
            case 512:
                mTvNum.setBackgroundColor(0xffedc854);
                break;
            case 1024:
                mTvNum.setBackgroundColor(0xffedc54f);
                break;
            case 2048:
                mTvNum.setBackgroundColor(0xffedc32e);
                break;
            default:
                mTvNum.setBackgroundColor(0xff3c4a34);
                break;
        }
    }
}
