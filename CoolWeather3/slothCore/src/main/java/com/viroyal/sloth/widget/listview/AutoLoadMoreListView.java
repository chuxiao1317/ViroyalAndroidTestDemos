package com.viroyal.sloth.widget.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sloth.core.R;


/**
 * 目前很多App的ListView都有在分页显示时自动加载更多内容的功能，既：
 * 1 每次只显示一页内容；
 * 2 用户滚动到底部的时候，则会出现一行“正在加载”的提示并开始加载；
 * 3 如果全部内容都加载完成了，则滚动到底部不会出现提示。
 *
 * AutoLoadMoreListView在标准ListView的基础上，进行了简单的封装来实现这个功能；
 * 1 “正在加载”的提示是一个footer，默认实现是文字加旋转动画；
 * 2 可以指定footer的高度，背景颜色，文字等属性，也可以替换为自定义的layout；
 * 3 footer是否显示由调用者决定；
 * 4 当滚动到底部的时候，通过回调通知应用执行加载，加载完成后要告知加载完成；
 */
public class AutoLoadMoreListView extends ListView implements AbsListView.OnScrollListener {
    private static final String TAG = AutoLoadMoreListView.class.getSimpleName();

    private View mLoadMoreView;
    //控制moreView的显示，是否显示是由调用者来决定的
    private boolean mShowLoadMore;

    private AutoLoadMoreListener mLoadMoreListener;
    //是否正在执行加载更多的动作，防止重复调用
    private boolean mDoingLoadMore;
    //滚动到底部的标记，防止scroll一直在底部（比如没网络导致本次数据加载更多失败）
    //导致不断去load，只有先向上滚动，再到底部的时候才能触发
    private boolean mLastOverBottom = false;

    public AutoLoadMoreListView(Context context) {
        super(context);
        init(null, 0);
    }

    public AutoLoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public AutoLoadMoreListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        mLoadMoreView = LayoutInflater.from(getContext()).inflate(R.layout.simple_auto_load_more, null);
        setOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        boolean overBottom = (firstVisibleItem + visibleItemCount) >= totalItemCount;
        //检查是否要通知App去做加载更多
        if (mShowLoadMore && (!mDoingLoadMore)
                && mLoadMoreListener != null
                && overBottom
                && !mLastOverBottom) {
            mDoingLoadMore = true;
            mLoadMoreListener.onLoadMoreBegin(this);
        }
        mLastOverBottom = overBottom;

        if (mShowLoadMore && this.getFooterViewsCount() == 0) {
            if (firstVisibleItem + visibleItemCount <= totalItemCount - 1) {
                this.addFooterView(mLoadMoreView);
            }
        }
    }

    /**
     * 设置是否显示“加载更多”footer
     *
     * @param show
     */
    public void setShowLoadMore(boolean show) {
        if (show == mShowLoadMore) {
            return;
        }

        mShowLoadMore = show;

        if (mShowLoadMore) {
            this.addFooterView(mLoadMoreView);
        } else {
            this.removeFooterView(mLoadMoreView);
        }
    }

    public void hideShowLoadMore() {
        this.removeFooterView(mLoadMoreView);
    }

    public void setLoadMoreListener(AutoLoadMoreListener listener) {
        mLoadMoreListener = listener;
    }

    /**
     * App设置是否加载更多动作已经完成
     * 只有本次加载完成了，当滚动到底部的时候，才能再次发起加载
     */
    public void loadMoreComplete() {
        mDoingLoadMore = false;
    }

    /**
     * 设置默认的加载更多footer的属性
     *
     * @param height  高度 px
     * @param bgColor 背景颜色
     * @param textSize 文字大小 sp
     * @param textColor 文字颜色
     */
    public void setupDefaultLoadMoreView(int height, int bgColor, int textSize, int textColor) {
        mLoadMoreView.setBackgroundColor(bgColor);
        View v = mLoadMoreView.findViewById(R.id.auto_load_more_container);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) v
                .getLayoutParams();
        lp.height = height;
        v.setLayoutParams(lp);

        TextView textView = (TextView) mLoadMoreView.findViewById(R.id.auto_load_more_text);
        textView.setTextSize(textSize);
        textView.setTextColor(textColor);
    }

    /**
     * 设置自定义的加载更多footer
     * @param view
     */
    public void setCustomLoadMoreView(View view) {
        mLoadMoreView = view;
    }

    /**
     * 在android4.2.2的机器上遇到一个问题，调用addFooterView的时候报1
     * ListDataBindingAdapter cannot be cast to android.widget.HeaderViewListAdapter
     * 在网上查找后得知，在Api18以下的机器，Wrapping做得不太好，可以用如下方法来规避。
     *
     * @param adapter 适配器
     */
    @Override
    public void setAdapter(ListAdapter adapter) {
        this.addFooterView(mLoadMoreView);
        super.setAdapter(adapter);
        this.removeFooterView(mLoadMoreView);
    }


}
