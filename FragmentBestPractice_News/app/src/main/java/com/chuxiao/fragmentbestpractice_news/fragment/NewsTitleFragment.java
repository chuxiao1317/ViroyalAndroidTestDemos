package com.chuxiao.fragmentbestpractice_news.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chuxiao.fragmentbestpractice_news.NewsContentActivity;
import com.chuxiao.fragmentbestpractice_news.R;
import com.chuxiao.fragmentbestpractice_news.entity.News;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by 12525 on 2018/4/25.
 */

public class NewsTitleFragment extends Fragment {

    private boolean isTwoPane;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_title_fragment, container, false);
        RecyclerView newsTitleRecyclerView = (RecyclerView) view.findViewById(R.id.news_title_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        newsTitleRecyclerView.setLayoutManager(layoutManager);
        NewsAdapter adapter = new NewsAdapter(getNews());
        newsTitleRecyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //能找到news_content_layout布局时，为双页模式,找不到news_content_layout布局时，为单页模式
        isTwoPane = getActivity().findViewById(R.id.news_content_layout) != null;
    }

    /**
     * 设置静态数据
     */
    private List<News> getNews() {
        List<News> newsList = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            newsList.add(new News("这里是新闻标题" + i, getRandomLengthContent("这里是新闻内容" + i + "。")));
        }
        Log.d("list大小", newsList.size() + "");
        return newsList;
    }

    /**
     * 设置随机新闻内容
     */
    private String getRandomLengthContent(String content) {
        Random random = new Random();
        int length = random.nextInt(20) + 1;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(content);
        }
        return builder.toString();
    }

    private class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

        private List<News> mNewsList;

        public NewsAdapter(List<News> newsList) {
            this.mNewsList = newsList;
        }

        @Override
        public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_title_item, parent, false);
            final ViewHolder holder = new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    News news = mNewsList.get(holder.getAdapterPosition());
                    if (isTwoPane) {
                        // 如果是双页模式，则刷新NewsContentFragment里面的内容
                        NewsContentFragment newsContentFragment = (NewsContentFragment) getFragmentManager().findFragmentById(R.id.news_content_fragment);
                        newsContentFragment.refresh(news.getTitle(), news.getContent());
                    } else {
                        // 如果是单页模式，则直接启动NewsContentActivity
                        NewsContentActivity.actionStart(getActivity(), news.getTitle(), news.getContent());
                    }
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(NewsAdapter.ViewHolder holder, int position) {
            News news = mNewsList.get(position);
            holder.newsTitleText.setText(news.getTitle());
        }

        @Override
        public int getItemCount() {
            return mNewsList == null ? 0 : mNewsList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView newsTitleText;

            public ViewHolder(View itemView) {
                super(itemView);
                newsTitleText = (TextView) itemView.findViewById(R.id.news_title);
            }
        }
    }
}
