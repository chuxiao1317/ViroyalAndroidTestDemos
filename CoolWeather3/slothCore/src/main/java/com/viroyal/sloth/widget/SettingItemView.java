package com.viroyal.sloth.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sloth.core.R;
import com.viroyal.sloth.util.IdiUtils;

/**
 * Created by zaiyu on 2016/8/3.
 */
public class SettingItemView extends LinearLayout {
    private TextView mTitle;
    private TextView mContent;
    private ImageView mClickArrow;

    public SettingItemView(Context context) {
        super(context);
        initView(context, null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attributeSet) {
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.SettingItemView);

        int paddingMiddle = typedArray.getDimensionPixelSize(R.styleable.SettingItemView_paddingMiddle, 0);
        int titleTextSize = typedArray.getDimensionPixelSize(R.styleable.SettingItemView_titleTextSize, 0);
        int titleTextColor = typedArray.getColor(R.styleable.SettingItemView_titleTextColorRes, 0);
        int contentLengh = typedArray.getColor(R.styleable.SettingItemView_contentLengh, 20);
        int contentTextSize = typedArray.getDimensionPixelSize(R.styleable.SettingItemView_contentTextSize, 0);
        int contentTextColor = typedArray.getColor(R.styleable.SettingItemView_contentTextColorRes, 0);
        String contentText = typedArray.getString(R.styleable.SettingItemView_contentText);
        String titleText = typedArray.getString(R.styleable.SettingItemView_titleText);
        int arrowBackground = typedArray.getResourceId(R.styleable.SettingItemView_arrowBackgroundRes, 0);
        int divideBackground = typedArray.getResourceId(R.styleable.SettingItemView_divideBackgroundRes, 0);
        boolean showArrow = typedArray.getBoolean(R.styleable.SettingItemView_showArrow, false);
        boolean showDivide = typedArray.getBoolean(R.styleable.SettingItemView_showDivide, false);
        int insidePaddingLeft = typedArray.getDimensionPixelSize(R.styleable.SettingItemView_insidePaddingLeft, 0);
        int insidePaddingRight = typedArray.getDimensionPixelSize(R.styleable.SettingItemView_insidePaddingRight, 0);
//        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER);
        mTitle = new TextView(getContext());
        mContent = new TextView(getContext());
        mClickArrow = new ImageView(getContext());
        View divide = new View(getContext());
        divide.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 2));
        divide.setBackgroundColor(Color.BLACK);

//        LinearLayout linearLayout = new LinearLayout(getContext());
//        linearLayout.setGravity(Gravity.CENTER_VERTICAL);
//        LayoutParams l = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//        l.weight = 1;
//        linearLayout.setLayoutParams(l);
//        linearLayout.setPadding(insidePaddingLeft, 0, insidePaddingRight, 0);
//        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
//        LayoutParams bp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//        mTitle.setLayoutParams(bp);
//        LayoutParams lc = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//        lc.weight = 1;
//        lc.setMargins(10, 0, 0, 0);
//        mContent.setLayoutParams(lc);
//        LayoutParams l1 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//        l1.leftMargin = paddingMiddle;
//        mClickArrow.setLayoutParams(l1);
//
//        mTitle.setTextColor(titleTextColor);
//        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
//        mTitle.setText(titleText);
//        mTitle.setMaxLines(1);
//
//        mContent.setTextColor(contentTextColor);
//        mContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, contentTextSize);
//        mContent.setText(contentText);
//        mContent.setMaxEms(contentLengh);
//        mContent.setSingleLine(true);
//        mContent.setMaxLines(1);
//        mContent.setEllipsize(TextUtils.TruncateAt.END);
//        mContent.setGravity(Gravity.RIGHT);
//        mTitle.setSingleLine(true);
//
//
//        if (showArrow) {
//            mClickArrow.setVisibility(VISIBLE);
//            if (arrowBackground != 0) {
//                mClickArrow.setBackgroundResource(arrowBackground);
//            } else {
//                mClickArrow.setVisibility(GONE);
//            }
//        } else {
//            mClickArrow.setVisibility(GONE);
//        }
//
//        if (showDivide) {
//            divide.setVisibility(VISIBLE);
//            if (divideBackground != 0) {
//                divide.setBackgroundResource(divideBackground);
//            } else {
//                divide.setVisibility(GONE);
//            }
//        } else {
//            divide.setVisibility(GONE);
//        }
//
//        typedArray.recycle();
//        linearLayout.addView(mTitle);
//        linearLayout.addView(mContent);
//        linearLayout.addView(mClickArrow);
//        addView(linearLayout);

        RelativeLayout relativeLayout = new RelativeLayout(getContext());
        RelativeLayout.LayoutParams l = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        relativeLayout.setLayoutParams(l);
        relativeLayout.setPadding(insidePaddingLeft, 0, insidePaddingRight, 0);
        relativeLayout.setGravity(Gravity.CENTER);
        RelativeLayout.LayoutParams l1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        l1.leftMargin = paddingMiddle;
        l1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        l1.addRule(RelativeLayout.CENTER_VERTICAL);
        mClickArrow.setLayoutParams(l1);
        mClickArrow.setId(IdiUtils.generateViewId());
        relativeLayout.addView(mClickArrow);

        RelativeLayout.LayoutParams lc = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lc.setMargins(10, 0, 0, 0);
        l1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lc.addRule(RelativeLayout.CENTER_VERTICAL);
        lc.addRule(RelativeLayout.LEFT_OF, mClickArrow.getId());
        mContent.setLayoutParams(lc);
        mContent.setId(IdiUtils.generateViewId());
        relativeLayout.addView(mContent);


        RelativeLayout.LayoutParams bp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        bp.addRule(RelativeLayout.CENTER_VERTICAL);
        bp.addRule(RelativeLayout.LEFT_OF, mContent.getId());
        mTitle.setLayoutParams(bp);
        mTitle.setId(IdiUtils.generateViewId());
        relativeLayout.addView(mTitle);


        mTitle.setTextColor(titleTextColor);
        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
        mTitle.setText(titleText);
        mTitle.setMaxLines(1);
        mTitle.setGravity(Gravity.LEFT);
        mTitle.setSingleLine(true);

        mContent.setTextColor(contentTextColor);
        mContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, contentTextSize);
        mContent.setText(contentText);
        mContent.setMaxEms(contentLengh);
        mContent.setSingleLine(true);
        mContent.setMaxLines(1);
        mContent.setEllipsize(TextUtils.TruncateAt.END);
        mContent.setGravity(Gravity.RIGHT);


        if (showArrow) {
            mClickArrow.setVisibility(VISIBLE);
            if (arrowBackground != 0) {
                mClickArrow.setBackgroundResource(arrowBackground);
            } else {
                mClickArrow.setVisibility(GONE);
            }
        } else {
            mClickArrow.setVisibility(GONE);
        }

        if (showDivide) {
            divide.setVisibility(VISIBLE);
            if (divideBackground != 0) {
                divide.setBackgroundResource(divideBackground);
            } else {
                divide.setVisibility(GONE);
            }
        } else {
            divide.setVisibility(GONE);
        }

        typedArray.recycle();
        addView(relativeLayout);
        addView(divide);
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }

    public String getTitle() {
        return mTitle.getText().toString();
    }

    public void setTitleText(String title) {
        setTitle(title);
    }

    public String getTitleText() {
        return getTitle();
    }

    public void setContent(String content) {
        mContent.setText(content);
    }

    public ImageView getRightImgView() {
        return mClickArrow;
    }

    public void setArrowVisibility(int visibility) {
        mClickArrow.setVisibility(visibility);
    }

    public String getContent() {
        return mContent.getText().toString();
    }

    public String getContentText() {
        return getContent();
    }

    public void setContentText(String content) {
        setContent(content);
    }

    public void setContentTextColor(@ColorInt int color) {
        mContent.setTextColor(color);
    }
}
