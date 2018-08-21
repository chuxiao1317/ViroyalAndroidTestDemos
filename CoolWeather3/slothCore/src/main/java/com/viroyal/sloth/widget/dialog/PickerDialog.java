package com.viroyal.sloth.widget.dialog;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sloth.core.R;
import com.viroyal.sloth.widget.wheelview.MyArrayWheelAdapter;
import com.viroyal.sloth.widget.wheelview.MyWheelView;

import java.util.ArrayList;

/**
 * Created by shengpeng on 2015-10-19.
 */
public class PickerDialog extends DialogFragment {
    private final static String EXTRA_SHOW_BOTTOM = "show_bottom";
    private final static String EXTRA_MENUS = "menus";
    private final static String CHOSE = "chose";
    private final static String EXTRA_TITLE = "title";

    private Listener mListener;
    private Context context;
    private ArrayList<String> menus;
    private MyWheelView wheelView;
    public interface Listener {

        void onMenuItemSelected(String item);
    }
    public static PickerDialog newInstance(Context context,ArrayList<String> menus, String title, Listener listener) {
        PickerDialog f = new PickerDialog();
        Bundle b = new Bundle();
        f.setArguments(b);
        b.putStringArrayList(EXTRA_MENUS, menus);
        b.putString(EXTRA_TITLE, title);
        f.mListener = listener;
        f.context = context;
        return f;
    }
    public static PickerDialog newInstance(ArrayList<String> menus, String chose, Listener listener) {
        PickerDialog f = new PickerDialog();
        Bundle b = new Bundle();
        b.putStringArrayList(EXTRA_MENUS, menus);
        b.putString(CHOSE, chose);
        f.setArguments(b);
        f.mListener = listener;
        return f;
    }

    /**
     * 设置dialog的显示位置
     *
     * @param showBottom true 在底部显示 false 在中间显示
     */
    public PickerDialog setShowBottom(boolean showBottom) {
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
        //getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        menus = getArguments().getStringArrayList(EXTRA_MENUS);
//        assert menus != null;
        View view = inflater.inflate(R.layout.dialog_date_pick,
                ((ViewGroup) window.findViewById(android.R.id.content)), false);
        if (showBottom) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        TextView cancel = (TextView) view.findViewById(R.id.cancel);
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView ok = (TextView) view.findViewById(R.id.ok);
        wheelView = (MyWheelView) view.findViewById(R.id.day);
        title.setText(getArguments().getString(EXTRA_TITLE));

        MyArrayWheelAdapter adapter = new MyArrayWheelAdapter(context, menus.toArray());
        wheelView.setViewAdapter(adapter);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onMenuItemSelected(menus.get(wheelView.getCurrentItem()));
                dismiss();
            }
        });

        return view;
    }


}
