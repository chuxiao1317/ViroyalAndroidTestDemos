package com.viroyal.sloth.widget.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sloth.core.R;

import java.util.ArrayList;

/**
 * Created by shengpeng on 2015-10-19.
 */
public class InputDialog extends DialogFragment {
    private final static int STRING = 0;
    private final static int INT = 1;
    private final static int DOUBLE = 2;
    private final static String EXTRA_SHOW_BOTTOM = "show_bottom";
    private final static String EXTRA_MENUS = "menus";
    private final static String CHOSE = "chose";
    TextView number_show;
    LinearLayout zero_d_layout;
    StringBuffer mNumber = new StringBuffer();
    int mLenght = 8;
    int mType = 0;
    private Listener mListener;

    //    public static InputDialog newInstance() {
//        InputDialog f = new InputDialog();
//        Bundle b = new Bundle();
//        f.setArguments(b);
//        return f;
//    }
    public static InputDialog newInstance(Listener listener, String number, int type) {
        InputDialog f = new InputDialog();
        Bundle b = new Bundle();
        f.setArguments(b);
        f.mListener = listener;
        f.mType = type;
        f.mNumber.append(number);
        return f;
    }

    /**
     * 设置dialog的显示位置
     *
     * @param showBottom true 在底部显示 false 在中间显示
     */
    public InputDialog setShowBottom(boolean showBottom) {
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
        View view = inflater.inflate(R.layout.dialog_input_layout,
                ((ViewGroup) window.findViewById(android.R.id.content)), false);
        if (showBottom) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        MyOnClickListener listener = new MyOnClickListener();
        Button one_num = (Button) view.findViewById(R.id.one_num);
        Button two_num = (Button) view.findViewById(R.id.two_num);
        Button three_num = (Button) view.findViewById(R.id.three_num);
        Button four_num = (Button) view.findViewById(R.id.four_num);
        Button five_num = (Button) view.findViewById(R.id.five_num);
        Button six_num = (Button) view.findViewById(R.id.six_num);
        Button seven_num = (Button) view.findViewById(R.id.seven_num);
        Button eight_num = (Button) view.findViewById(R.id.eight_num);
        Button nine_num = (Button) view.findViewById(R.id.nine_num);
        Button zero_num = (Button) view.findViewById(R.id.zero_num);
        Button del_num = (Button) view.findViewById(R.id.del_num);
        Button ok_num = (Button) view.findViewById(R.id.ok_num);
        Button zero_d = (Button) view.findViewById(R.id.zero_d);
        zero_d_layout = (LinearLayout) view.findViewById(R.id.zero_d_layout);
        number_show = (TextView) view.findViewById(R.id.number_show);
        if (mType == 5) {
            zero_d_layout.setVisibility(View.VISIBLE);
            zero_d.setText("-");
            mLenght = 32;
        }
        if (mType == 4) {
            zero_d_layout.setVisibility(View.GONE);
            mLenght = 32;
        } else if (mType == 1) {
            zero_d_layout.setVisibility(View.GONE);
            mLenght = 8;
        } else {
            zero_d_layout.setVisibility(View.VISIBLE);
            mLenght = 32;
        }
        number_show.setText(mNumber.toString());
        //@OnClick({R.id.one_num, R.id.two_num, R.id.three_num, R.id.four_num, R.id.five_num, R.id.six_num, R.id.seven_num, R.id.eight_num, R.id.nine_num, R.id.zero_num, R.id.del_num, R.id.ok_num})
        one_num.setOnClickListener(listener);
        two_num.setOnClickListener(listener);
        three_num.setOnClickListener(listener);
        four_num.setOnClickListener(listener);
        five_num.setOnClickListener(listener);
        six_num.setOnClickListener(listener);
        seven_num.setOnClickListener(listener);
        eight_num.setOnClickListener(listener);
        nine_num.setOnClickListener(listener);
        zero_num.setOnClickListener(listener);
        del_num.setOnClickListener(listener);
        ok_num.setOnClickListener(listener);
        zero_d.setOnClickListener(listener);
//        String chose = getArguments().getString(CHOSE);
//        MyAdapter myAdapter = new MyAdapter(menus);
//        myAdapter.setShowBottom(showBottom);
//        myAdapter.setChose(chose);
//        list.setAdapter(myAdapter);
        return view;
    }

    public int getmType() {
        return mType;
    }

    public int getmLenght() {
        return mLenght;
    }

    public void setmLenght(int mLenght) {
        this.mLenght = mLenght;
    }

    public interface Listener {
        void onNumberSelected(String item);

        void ok(String item);
    }

    //@OnClick({R.id.one_num, R.id.two_num, R.id.three_num, R.id.four_num, R.id.five_num, R.id.six_num, R.id.seven_num, R.id.eight_num, R.id.nine_num, R.id.zero_num, R.id.del_num, R.id.ok_num})

    class MyOnClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            {
                if (v.getId() == R.id.ok_num) {
                    mListener.ok(mNumber.toString());
                    dismiss();
                } else {
                    if (mNumber.length() < mLenght) {
                        if (v.getId() == R.id.one_num) {
                            mNumber.append(1);
                        }
                        if (v.getId() == R.id.two_num) {
                            mNumber.append(2);
                        }
                        if (v.getId() == R.id.three_num) {
                            mNumber.append(3);
                        }
                        if (v.getId() == R.id.four_num) {
                            mNumber.append(4);
                        }
                        if (v.getId() == R.id.five_num) {
                            mNumber.append(5);
                        }
                        if (v.getId() == R.id.six_num) {
                            mNumber.append(6);
                        }
                        if (v.getId() == R.id.seven_num) {
                            mNumber.append(7);
                        }
                        if (v.getId() == R.id.eight_num) {
                            mNumber.append(8);
                        }
                        if (v.getId() == R.id.nine_num) {
                            mNumber.append(9);
                        }
                        if (v.getId() == R.id.zero_num) {
                            mNumber.append(0);
                        }
                        if (mType != 1) {
                            if (v.getId() == R.id.zero_d) {
                                if (mType == 2) {
                                    if (mNumber.indexOf(".") > -1) {

                                    } else {
                                        if (mNumber.length() > 0) {
                                            mNumber.append(".");
                                        }
                                    }
                                } else if (mType == 5){
                                    mNumber.append("-");
                                } else {
                                    if (mNumber.length() > 0) {
                                        mNumber.append(".");
                                    }
                                }
                            }
                        }

                    }
                    if (v.getId() == R.id.del_num) {
                        if (mNumber.length() > 0) {
                            mNumber.deleteCharAt(mNumber.length() - 1);
                        }
                    }

                    mListener.onNumberSelected(mNumber.toString());
                    number_show.setText(mNumber.toString());
                }
            }
        }
    }
//    private class MyAdapter extends BaseAdapter {
//
//        final ArrayList<String> menus;
//        String chose = null;
//        boolean showBottom = false;
//        public MyAdapter(ArrayList<String> menus) {
//            this.menus = menus;
//        }
//
//        public void setChose(String c) {
//            chose = c;
//        }
//
//        public void setShowBottom(boolean showBottom) {
//            this.showBottom = showBottom;
//        }
//
//        @Override
//        public int getCount() {
//            return (menus == null) ? 0 : menus.size();
//        }
//
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//            final View view;
//            if (menus != null) {
//                if (convertView == null) {
//                    LayoutInflater inflater = LayoutInflater.from(getActivity());
//                    convertView = inflater.inflate(R.layout.dialog_menu_item_layout, null);
//                }
//                TextView title = (TextView) convertView.findViewById(R.id.dialog_menu_list_txt);
//                if (showBottom) {
//                    title.setBackgroundResource(R.drawable.listview_bg_middle);
//                } else {
//                    if (getCount() == 1) {
//                        title.setBackgroundResource(R.drawable.listview_bg);
//                    } else if (position == 0) {
//                        title.setBackgroundResource(R.drawable.listview_bg_top);
//                    } else if (position == getCount() - 1) {
//                        title.setBackgroundResource(R.drawable.listview_bg_bottom);
//                    } else {
//                        title.setBackgroundResource(R.drawable.listview_bg_middle);
//                    }
//                }
//                title.setText(menus.get(position));
//                if (position == getCount() - 1) {
//                    title.setTextColor(getResources().getColor(R.color.cancel_color));
//                } else if (chose != null && chose.equals(menus.get(position))) {
//                    title.setTextColor(getResources().getColor(R.color.important_operation_color));
//                } else {
//                    title.setTextColor(getResources().getColor(R.color.text_color_property));
//                }
//                title.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        InputDialog.this.dismiss();
//                        mListener.onMenuItemSelected(menus.get(position));
//                    }
//                });
//            }
//            view = convertView;
//            return view;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public Object getItem(int position) {
//            if (menus != null && position < menus.size()) {
//                return menus.get(position);
//            }
//            return null;
//        }
//    }

}
