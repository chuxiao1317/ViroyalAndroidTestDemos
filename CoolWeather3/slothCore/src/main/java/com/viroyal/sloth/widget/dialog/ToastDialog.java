package com.viroyal.sloth.widget.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.sloth.core.R;
/**
 * Created by qjj on 2017/5/12.
 */
public class ToastDialog  extends DialogFragment {

    private final static String EXTRA_TITLE = "title";
    private final static String EXTRA_MESSAGE = "message";
    private final static String EXTRA_POSITIVE_LABEL = "positive_label";
    private final static String EXTRA_NEGATIVE_LABEL = "negative_label";
    private final static String EXTRA_SHOW_IMG = "show_img";

    /**
     * @param msg      Dialog 内容
     * @param showImg 取消/确认按钮事件回调
     * @return
     */
    public static ToastDialog newInstance(String msg, boolean showImg) {
        ToastDialog f = new ToastDialog();
        Bundle b = new Bundle();
        b.putString(EXTRA_MESSAGE, msg);
        b.putBoolean(EXTRA_SHOW_IMG, showImg);
        f.setArguments(b);
        return f;
    }

    /**
     * 设置dialog的显示位置
     *
     * @param showImg true 正确 false 错误
     */
    public ToastDialog setShowBottom(boolean showImg) {
        getArguments().putBoolean(EXTRA_SHOW_IMG, showImg);
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        boolean showImg = getArguments().getBoolean(EXTRA_SHOW_IMG, false);
        Window window = getDialog().getWindow();


        window.requestFeature(Window.FEATURE_NO_TITLE);
        int layoutId = R.layout.dialog_toast_layout;
        View v = inflater.inflate(layoutId, ((ViewGroup) window.findViewById(android.R.id.content)), false);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        getDialog().setCanceledOnTouchOutside(true);

        TextView messageView = (TextView) v.findViewById(R.id.dialog_toast_content);
        if (getArguments().getString(EXTRA_MESSAGE) == null) {
            messageView.setVisibility(View.GONE);
        } else {
            messageView.setVisibility(View.VISIBLE);
            messageView.setText(getArguments().getString(EXTRA_MESSAGE));
        }
        ImageView img = (ImageView) v.findViewById(R.id.dialog_toast_img);

        if (showImg) {
            img.setImageResource(R.drawable.ic_success);
        } else {
            img.setImageResource(R.drawable.ic_error);
        }

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastDialog.this.dismiss();
            }
        });
        return v;
    }
}