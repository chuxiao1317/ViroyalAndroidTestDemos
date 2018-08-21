package com.viroyal.sloth.widget.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sloth.core.R;

import java.util.List;

/**
 * Created by qjj on 2016/10/10.
 */
public class MenuPickerDialog extends DialogFragment {

    private final static String EXTRA_TITLE = "title";
    private final static String EXTRA_MESSAGE = "message";
    private final static String EXTRA_POSITIVE_LABEL = "positive_label";
    private final static String EXTRA_NEGATIVE_LABEL = "negative_label";
    private final static String EXTRA_SHOW_BOTTOM = "show_bottom";

    private Listener mListener;
    private RecyclerView mMenulist;
    private BaseQuickAdapter mAdapter;
    private boolean mMany;

    public interface Listener {

        boolean onYes();

        boolean onNo();

    }

    /**
     * @param title    Dialog标题
     * @param items    显示数据
     * @param isMany   true-多选  false-单选
     * @param no       取消按钮文本
     * @param yes      确认按钮文本
     * @param listener 取消/确认按钮事件回调
     * @return
     */
    public static MenuPickerDialog newInstance(String title, List<SelectItem> items, boolean isMany,
                                               String yes, String no,
                                               Listener listener) {
        MenuPickerDialog f = new MenuPickerDialog();
        Bundle b = new Bundle();
        b.putString(EXTRA_TITLE, title);
        //b.putString(EXTRA_MESSAGE, msg);
        b.putString(EXTRA_POSITIVE_LABEL, yes);
        b.putString(EXTRA_NEGATIVE_LABEL, no);
        f.setArguments(b);
        f.mListener = listener;
        f.mItems = items;
        f.mMany = isMany;
        return f;
    }

    /**
     * 设置dialog的显示位置
     *
     * @param showBottom true 在底部显示 false 在中间显示
     */
    public MenuPickerDialog setShowBottom(boolean showBottom) {
        getArguments().putBoolean(EXTRA_SHOW_BOTTOM, showBottom);
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        final View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_menu_picker_layout, null);
        if (v == null) {
            return null;
        }

        TextView title = (TextView) v.findViewById(R.id.dialog_custom_title);

        if (getArguments().getString(EXTRA_TITLE) == null) {
            title.setVisibility(View.GONE);
        } else {
            title.setText(getArguments().getString(EXTRA_TITLE));
        }
        mMenulist = (RecyclerView) v.findViewById(R.id.menu_check_list);

        mMenulist.setLayoutManager(new LinearLayoutManager(getActivity()));
        initAdapter();
        mMenulist.setAdapter(mAdapter);

        TextView no = (TextView) v.findViewById(R.id.dialog_custom_no);
        TextView yes = (TextView) v.findViewById(R.id.dialog_custom_yes);
        if (getArguments().getString(EXTRA_NEGATIVE_LABEL) == null) {
            no.setText(getActivity().getString(android.R.string.no));
        } else {
            no.setText(getArguments().getString(EXTRA_NEGATIVE_LABEL));
        }
        if (getArguments().getString(EXTRA_POSITIVE_LABEL) == null) {
            yes.setText(getActivity().getString(android.R.string.yes));
        } else {
            yes.setText(getArguments().getString(EXTRA_POSITIVE_LABEL));
        }
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean dismiss = mListener.onNo();
                if (!dismiss) {
                    MenuPickerDialog.this.dismiss();
                }
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean dismiss = mListener.onYes();
                if (!dismiss) {
                    MenuPickerDialog.this.dismiss();
                }
            }
        });
        getDialog().setCanceledOnTouchOutside(false);

        return v;
    }

    private List<SelectItem> mItems;//测试数据集合
    private int mOldPosition = -1;//上一次点击的位置

    private void initAdapter() {
        //初始化adapter
        mAdapter = new BaseQuickAdapter<SelectItem>(R.layout.menu_list_item, mItems) {
            @Override
            protected void convert(final BaseViewHolder baseViewHolder, final SelectItem item) {
                baseViewHolder.setText(R.id.menu_item_tv, item.getName());
                baseViewHolder.setVisible(R.id.menu_item_check_img, item.isChecked());
                baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        item.setIsChecked(!item.isChecked());//设置item状态
                        if (!mMany){
                            int position = baseViewHolder.getAdapterPosition();
                            if (position == mOldPosition) {
                                mOldPosition = -1;
                            } else {
                                if (mOldPosition != -1) {
                                    mItems.get(mOldPosition).setIsChecked(false);
                                }
                                mOldPosition = position;
                            }
                        }
                        notifyDataSetChanged();////刷新recyclerview
                    }
                });
            }
        };
    }
}
