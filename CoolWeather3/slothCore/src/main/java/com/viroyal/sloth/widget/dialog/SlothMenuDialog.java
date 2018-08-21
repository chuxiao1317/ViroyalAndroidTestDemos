package com.viroyal.sloth.widget.dialog;

import android.app.DialogFragment;
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

import java.util.ArrayList;

/**
 * Created by shengpeng on 2015-10-19.
 */
public class SlothMenuDialog extends DialogFragment {
    private final static String EXTRA_SHOW_BOTTOM = "show_bottom";
    private final static String EXTRA_MENUS = "menus";
    private final static String CHOSE = "chose";

    private Listener mListener;

    public interface Listener {

        void cancle();
        void ok();
        void onMenuItemSelected(int position);
    }

    public static SlothMenuDialog newInstance(ArrayList<String> menus, String chose, Listener listener) {
        SlothMenuDialog f = new SlothMenuDialog();
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
    public SlothMenuDialog setShowBottom(boolean showBottom) {
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
        final ArrayList<String> menus = getArguments().getStringArrayList(EXTRA_MENUS);
        assert menus != null;
        View view = inflater.inflate(R.layout.dialog_sloth_menu_layout,
                ((ViewGroup) window.findViewById(android.R.id.content)), false);
        if (showBottom) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        ListView list = (ListView) view.findViewById(R.id.dialog_menu_list);

        TextView okbtn = (TextView) view.findViewById(R.id.dialog_ok);
        TextView canclebtn = (TextView) view.findViewById(R.id.dialog_cancle);
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SlothMenuDialog.this.dismiss();
                mListener.ok();
            }
        });
        canclebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SlothMenuDialog.this.dismiss();
                mListener.cancle();
            }
        });
        String chose = getArguments().getString(CHOSE);
        MyAdapter myAdapter = new MyAdapter(menus);
        myAdapter.setShowBottom(showBottom);
        myAdapter.setChose(chose);
        list.setAdapter(myAdapter);
        return view;
    }

    private class MyAdapter extends BaseAdapter {

        final ArrayList<String> menus;
        String chose = null;
        boolean showBottom = false;
        public MyAdapter(ArrayList<String> menus) {
            this.menus = menus;
        }

        public void setChose(String c) {
            chose = c;
        }

        public void setShowBottom(boolean showBottom) {
            this.showBottom = showBottom;
        }

        @Override
        public int getCount() {
            return (menus == null) ? 0 : menus.size();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final View view;
            if (menus != null) {
                if (convertView == null) {
                    LayoutInflater inflater = LayoutInflater.from(getActivity());
                    convertView = inflater.inflate(R.layout.dialog_menu_item_layout, null);
                }
                TextView title = (TextView) convertView.findViewById(R.id.dialog_menu_list_txt);
                if (showBottom) {
                    title.setBackgroundResource(R.drawable.listview_bg_middle);
                } else {
                    if (getCount() == 1) {
                        title.setBackgroundResource(R.drawable.listview_bg);
                    } else if (position == 0) {
                        title.setBackgroundResource(R.drawable.listview_bg_top);
                    } else if (position == getCount() - 1) {
                        title.setBackgroundResource(R.drawable.listview_bg_bottom);
                    } else {
                        title.setBackgroundResource(R.drawable.listview_bg_middle);
                    }
                }
                title.setText(menus.get(position));
//                if (position == getCount() - 1) {
//                    title.setTextColor(getResources().getColor(R.color.cancel_color));
//                } else
                if (chose != null && chose.equals(menus.get(position))) {
                    title.setTextColor(getResources().getColor(R.color.important_operation_color));
                } else {
                    title.setTextColor(getResources().getColor(R.color.text_color_property));
                }
                title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chose = menus.get(position);
                        mListener.onMenuItemSelected(position);
                        notifyDataSetChanged();
                    }
                });
            }
            view = convertView;
            return view;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            if (menus != null && position < menus.size()) {
                return menus.get(position);
            }
            return null;
        }
    }

}
