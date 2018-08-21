package com.chuxiao.fragmentbestpractice_news;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.chuxiao.fragmentbestpractice_news.fragment.NewsContentFragment;

public class NewsContentActivity extends AppCompatActivity {

    public static void actionStart(Context context, String newsTitile, String newsContent) {
        Intent intent = new Intent(context, NewsContentActivity.class);
        intent.putExtra("news_title", newsTitile);
        intent.putExtra("news_content", newsContent);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_content);
        String newsTitle = getIntent().getStringExtra("news_title");
        String newsContent = getIntent().getStringExtra("news_content");
        NewsContentFragment newsContentFragment = (NewsContentFragment) getSupportFragmentManager().findFragmentById(R.id.news_content_fragment);
        //刷新newsContentFragment界面
        newsContentFragment.refresh(newsTitle, newsContent);
    }
}
