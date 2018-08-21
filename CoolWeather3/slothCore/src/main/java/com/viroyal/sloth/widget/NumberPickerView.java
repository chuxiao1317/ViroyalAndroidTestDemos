package com.viroyal.sloth.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.InputFilter;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sloth.core.R;
import com.viroyal.sloth.util.KeyBoardUtils;

/**
 * Created by qjj on 2016/7/25.
 */
public class NumberPickerView extends LinearLayout {

    private float txSize = 20;
    private Paint paint;
    private int txColor;
    private int txBg;
    private float buttonWH;
    private int minnum = 0;
    private int addBackground, subtractBackground;
    private int maxlength = 9;

    private EditText numberText;
    private TextView addBt, subtractBt;
    public NumberPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initProperty(context, attrs);
        initView();
    }

    public NumberPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initProperty(context, attrs);
        initView();
    }

    public NumberPickerView(Context context) {
        super(context);
        initView();
    }

    private void initProperty(Context context, AttributeSet attributeSet) {
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.numberPicker);

        txSize = typedArray.getDimension(R.styleable.numberPicker_txSize, 20);
        txColor = typedArray.getColor(R.styleable.numberPicker_txColor, Color.BLACK);
        buttonWH = typedArray.getDimension(R.styleable.numberPicker_buttonWH, 20);
        txBg = typedArray.getResourceId(R.styleable.numberPicker_textBackground, Color.WHITE);
        addBackground = typedArray.getResourceId(R.styleable.numberPicker_addImg, Color.WHITE);
        subtractBackground = typedArray.getResourceId(R.styleable.numberPicker_subtractImg, Color.WHITE);
        minnum = typedArray.getInt(R.styleable.numberPicker_minnum, 0);
        paint = new Paint();
        paint.setAntiAlias(true);
        typedArray.recycle();
    }

    private void initView() {

        numberText = new EditText(getContext());
        addBt = new TextView(getContext());
        subtractBt = new TextView(getContext());

        LayoutParams bp = new LayoutParams(0, 0);

        bp.height = (int) buttonWH;
        bp.width = (int) buttonWH;
        addBt.setLayoutParams(bp);
        subtractBt.setLayoutParams(bp);

        addBt.setTextSize(txSize);
        subtractBt.setTextSize(txSize);

        addBt.setBackgroundResource(addBackground);
        addBt.setGravity(Gravity.CENTER);

        subtractBt.setBackgroundResource(subtractBackground);
        subtractBt.setGravity(Gravity.CENTER);

        LayoutParams p = new LayoutParams(0, 0);

        p.height = bp.height*2;
        p.width = ViewGroup.LayoutParams.WRAP_CONTENT;

        p.setMargins(10, 0, 10, 0);
        numberText.setLayoutParams(p);
        numberText.setMinEms(2);
        numberText.setLines(1);
        numberText.setSingleLine(true);
        numberText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxlength)});
        numberText.setRawInputType(InputType.TYPE_CLASS_NUMBER);

        numberText.setTextSize(txSize);
        numberText.setTextColor(txColor);
        numberText.setBackgroundResource(txBg);
        numberText.setGravity(Gravity.CENTER );

        addView(subtractBt);
        addView(numberText);
        addView(addBt);

        numberText.setText(minnum + "");

        addBt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardUtils.closeKeybord(numberText, getContext());
                numberText.clearFocus();
                int num = getNumber();
                numberText.setText((++num)+"");
            }
        });
        subtractBt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardUtils.closeKeybord(numberText, getContext());
                numberText.clearFocus();
                int num = getNumber();
                if (num>minnum)
                {
                    numberText.setText((--num)+"");
                }

            }
        });
    }
    @Override
    public void draw(Canvas canvas) {

        LayoutParams bp = new LayoutParams(0,0);

        bp.height = getHeight();
        bp.width = getHeight();


        addBt.setLayoutParams(bp);
        subtractBt.setLayoutParams(bp);

        super.draw(canvas);
    }

    public int getNumber() {
        String value = numberText.getText().toString();
        if (null!=value && value.length()>0)
            return Integer.parseInt(value);
        else
            return 0;
    }
}
