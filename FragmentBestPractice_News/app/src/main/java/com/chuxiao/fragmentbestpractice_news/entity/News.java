package com.chuxiao.fragmentbestpractice_news.entity;

/**
 * Created by 12525 on 2018/4/24.
 */

public class News {

    private String title;

    private String content;

    public News(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
