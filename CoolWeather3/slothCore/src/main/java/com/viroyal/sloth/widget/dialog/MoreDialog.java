package com.viroyal.sloth.widget.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sloth.core.R;

import java.util.List;

/**
 * Created by shengpeng on 2015-10-19.
 */
public class MoreDialog extends DialogFragment {

    private final static String EXTRA_TITLE = "title";
    private final static String EXTRA_MESSAGE = "message";
    private final static String EXTRA_POSITIVE_LABEL = "positive_label";
    private final static String EXTRA_SHOW_BOTTOM = "show_bottom";
    List<MoreItem> mItems;
    private Listener mListener;

    public interface Listener {

        boolean onItemClick(MoreItem item);

    }

    /**
     * @param title    Dialog标题
     * @param items    列表
     * @param listener 取消/确认按钮事件回调
     * @return
     */
    public static MoreDialog newInstance(String title, List<MoreItem> items, Listener listener) {
        MoreDialog f = new MoreDialog();
        Bundle b = new Bundle();
        b.putString(EXTRA_TITLE, title);
        f.setArguments(b);
        f.mListener = listener;
        f.mItems = items;
        return f;
    }

    /**
     * 设置dialog的显示位置
     *
     * @param showBottom true 在底部显示 false 在中间显示
     */
    public MoreDialog setShowBottom(boolean showBottom) {
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

        window.requestFeature(Window.FEATURE_NO_TITLE);
        int layoutId = R.layout.dialog_more_layout;
        View v = inflater.inflate(layoutId, ((ViewGroup) window.findViewById(android.R.id.content)), false);
        window.setBackgroundDrawableResource(android.R.color.transparent);
//        if (showBottom) {
//            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        }
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getDialog().setCanceledOnTouchOutside(true);

        TextView title = (TextView) v.findViewById(R.id.dialog_custom_title);

        if (getArguments().getString(EXTRA_TITLE) == null) {
            title.setVisibility(View.GONE);
        } else {
            title.setText(getArguments().getString(EXTRA_TITLE));
        }

        RecyclerView more = (RecyclerView) v.findViewById(R.id.more_list);
        more.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        ItemPositionAdapter adapter = new ItemPositionAdapter(mItems);
        more.setAdapter(adapter);

        adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                if (mListener != null) {
                    if (mListener.onItemClick(mItems.get(i))){
                        dismiss();
                    }
                }
            }
        });

        getDialog().setCanceledOnTouchOutside(true);
        return v;
    }
    class ItemPositionAdapter extends BaseItemDraggableAdapter<MoreItem> {
        public ItemPositionAdapter(List data) {
            super(R.layout.item_more, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, MoreItem item) {
            helper.setText(R.id.more_name, item.name);
            helper.setText(R.id.more_info, item.info);
            helper.setText(R.id.more_num, item.num);
        }
    }
}
