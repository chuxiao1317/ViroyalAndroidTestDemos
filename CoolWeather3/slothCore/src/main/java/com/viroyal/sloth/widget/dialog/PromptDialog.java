package com.viroyal.sloth.widget.dialog;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.sloth.core.R;

/**
 * Created by yu.zai on 2015/12/10.
 */
public class PromptDialog extends DialogFragment {
    private final static String EXTRA_SHOW_BOTTOM = "show_bottom";
    private final static String EXTRA_TITLE = "title";
    private final static String EXTRA_MESSAGE = "message";
    private final static String EXTRA_POSITIVE_LABEL = "positive_label";
    private Listener mListener;

    public interface Listener {
        void onYes();
    }

    public static PromptDialog newInstance(String title,String msg, String yes,
                                          Listener listener) {
        PromptDialog f = new PromptDialog();
        Bundle b = new Bundle();
        b.putString(EXTRA_TITLE, title);
        b.putString(EXTRA_MESSAGE, msg);
        b.putString(EXTRA_POSITIVE_LABEL, yes);
        f.setArguments(b);
        f.mListener = listener;
        return f;
    }

    /**
     * 设置dialog的显示位置
     *
     * @param showBottom true 在底部显示 false 在中间显示
     */
    public PromptDialog setShowBottom(boolean showBottom) {
        getArguments().putBoolean(EXTRA_SHOW_BOTTOM, showBottom);
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        boolean showBottom = getArguments().getBoolean(EXTRA_SHOW_BOTTOM, false);
        Window window = getDialog().getWindow();
        if (showBottom) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            layoutParams.dimAmount = 0.5f;
            window.setGravity(Gravity.BOTTOM);
            window.setAttributes(layoutParams);
        }
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        View v = inflater.inflate(R.layout.prompt_dialog_layout,
                ((ViewGroup) window.findViewById(android.R.id.content)), false);
        if (showBottom) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        TextView title = (TextView)v.findViewById(R.id.dialog_custom_title);
        title.setText(getArguments().getString(EXTRA_TITLE));
        TextView messageView = (TextView)v.findViewById(R.id.dialog_custom_content);
        if (getArguments().getString(EXTRA_MESSAGE) == null ) {
            messageView.setVisibility(View.GONE);
        } else {
            messageView.setText(getArguments().getString(EXTRA_MESSAGE));
        }
        TextView yes = (TextView)v.findViewById(R.id.dialog_custom_yes);
        yes.setText(getArguments().getString(EXTRA_POSITIVE_LABEL));

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PromptDialog.this.dismiss();
            }
        });
        this.getDialog().setCanceledOnTouchOutside(false);
        return v;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        mListener.onYes();
    }
}
