package com.viroyal.sloth.widget.listview;

/**
 * Created by LiGang on 2016/4/30.
 */
public interface AutoLoadMoreListener {
    //通知App Listview已经滚动到底部，开始加载下一页数据
    void onLoadMoreBegin(final AutoLoadMoreListView listView);
}
