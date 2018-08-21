package com.viroyal.sloth.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sloth.core.R;

import java.util.List;

/**
 * Created by zaiyu on 2016/8/19.
 *
 * 封装分页加载，将加载更多的接口与下拉刷新接口放一起
 */
public class QAdapter<T> extends BaseQuickAdapter<T> implements BaseQuickAdapter.RequestLoadMoreListener {
    public Integer mSinceId = 0;
    public Integer total = 1;//默认有数据
    public Integer account = 0;//默认有数据
    private IQuickAdapter iQuickAdapter;

    private Activity context = null;
    private RecyclerView recyclerView = null;
    private View footView = null;

    private int mPageSizeDefault = 5;

    public interface IQuickAdapter<T> {
        void convert(BaseViewHolder baseViewHolder, T t);

        void onLoadMoreRequested(boolean finish);
    }

    public QAdapter(int layoutResId, List<T> data, IQuickAdapter iQuickAdapter) {
        super(layoutResId, data);
        init(iQuickAdapter);
    }

    public QAdapter(List<T> data, IQuickAdapter iQuickAdapter) {
        super(data);
        init(iQuickAdapter);
    }

    public QAdapter(View contentView, List<T> data, IQuickAdapter iQuickAdapter) {
        super(contentView, data);
        init(iQuickAdapter);
    }

    public void setContextRecyclerView(Activity context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
        footView = context.getLayoutInflater().inflate(R.layout.not_loading, null, false);
    }

    private void init(IQuickAdapter iQuickAdapter) {
        this.iQuickAdapter = iQuickAdapter;
        setOnLoadMoreListener(this);
        openLoadMore(mPageSizeDefault, true);
        account = getData().size();
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, T t) {
        if (iQuickAdapter != null) {
            iQuickAdapter.convert(baseViewHolder, t);
        }
    }

    @Override
    public void onLoadMoreRequested() {
        if (iQuickAdapter != null) {
            if (account >= total) {
                if (recyclerView != null && context != null) {
                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            notifyDataChangedAfterLoadMore(false);
                            if (footView != null) {
                                addFooterView(footView);
                            }
                        }
                    });
                } else {
                    iQuickAdapter.onLoadMoreRequested(true);
                }
            } else {
                iQuickAdapter.onLoadMoreRequested(false);
            }
        }
    }

    public void append(boolean refresh, List<T> t, Integer sinceId, Integer total) {
        setTotal(total);
        setSinceId(sinceId);
        if (refresh) {
            setData(t);
        } else {
            notifyDataChangedAfterLoadMore(t, true);
        }
        account = getData().size();
    }


    private void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getTotal() {
        return total;
    }

    private void setData(List<T> t) {
        if (footView != null) {
            removeFooterView(footView);
        }
        setNewData(t);
    }

    private void setSinceId(Integer sinceId) {
        this.mSinceId = sinceId;
    }

    public Integer getSinceId() {
        return mSinceId;
    }
}
