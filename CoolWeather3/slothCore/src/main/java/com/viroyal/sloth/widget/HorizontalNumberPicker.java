package com.viroyal.sloth.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sloth.core.R;
import com.viroyal.sloth.util.KeyBoardUtils;

/**
 * Created by yu.zai on 2016/5/6.
 */
public class HorizontalNumberPicker extends LinearLayout {
    TextView mIncreaseTextView;
    TextView mDecreaseTextView;
    EditText mInputText;
    Context mContext;
    private int mValue = 0;
    private Integer mMinValue = null;
    private Integer mMaxValue = null;
    private ValueChangeCallback mValueChangeCallback = null;

    public interface ValueChangeCallback {
        public void onChange(int value);
    }

    public HorizontalNumberPicker(Context context) {
        super(context);
        ininView(context);
    }

    public HorizontalNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        ininView(context);
    }

    public HorizontalNumberPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ininView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HorizontalNumberPicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        ininView(context);
    }

    private void ininView(Context context) {
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.horizontal_number_picker, this, true);

        OnClickListener onClickListener = new OnClickListener() {
            public void onClick(View v) {
                hideSoftInput();
                mInputText.clearFocus();
                if (v.getId() == R.id.numberpicker_increase) {
                    valueChange(1, false);
                } else {
                    valueChange(-1, false);
                }
            }
        };

        OnLongClickListener onLongClickListener = new OnLongClickListener() {
            public boolean onLongClick(View v) {
                hideSoftInput();
                mInputText.clearFocus();
                if (v.getId() == R.id.numberpicker_increase) {
                    valueChange(1, true);
                } else {
                    valueChange(-1, true);
                }
                return true;
            }
        };

        // increment button
        mIncreaseTextView = (TextView) findViewById(R.id.numberpicker_increase);
        mIncreaseTextView.setOnClickListener(onClickListener);
//        mIncreaseTextView.setOnLongClickListener(onLongClickListener);

        // decrement button
        mDecreaseTextView = (TextView) findViewById(R.id.numberpicker_decrease);
        mDecreaseTextView.setOnClickListener(onClickListener);
//        mDecreaseTextView.setOnLongClickListener(onLongClickListener);

        mInputText = (EditText) findViewById(R.id.numberpicker_input);
        mInputText.addTextChangedListener(mTextWatcher);
    }

    private void valueChange(int i, boolean longPerss) {
        int temp = mValue + i * (longPerss ? 10 : 1);
        if (mMinValue != null) {
            if (temp < mMinValue) {
                return;
            }
        }
        if (mMaxValue != null) {
            if (temp > mMaxValue) {
                return;
            }
        }
        mValue = temp;
        mInputText.setText(mValue + "");
    }

    /**
     * Hides the soft input if it is active for the input text.
     */
    private void hideSoftInput() {
        KeyBoardUtils.closeKeybord(mInputText, mContext);
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (TextUtils.isEmpty(s.toString()) && mValueChangeCallback != null) {
                mValueChangeCallback.onChange(Integer.parseInt(s.toString()));
            }
        }
    };

    public int getValue() {
        return mValue;
    }

    public void setValue(int mValue) {
        this.mValue = mValue;
        mInputText.setText(mValue + "");
    }

    public Integer getMinValue() {
        return mMinValue;
    }

    public void setMinValue(Integer mMinValue) {
        this.mMinValue = mMinValue;
    }

    public int getMaxValue() {
        return mMaxValue;
    }

    public void setMaxValue(Integer mMaxValue) {
        this.mMaxValue = mMaxValue;
    }
}
