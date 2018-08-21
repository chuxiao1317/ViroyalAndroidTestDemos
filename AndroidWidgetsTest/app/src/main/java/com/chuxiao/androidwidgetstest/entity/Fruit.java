package com.chuxiao.androidwidgetstest.entity;

/**
 * Created by 12525 on 2018/4/13.
 */

public class Fruit {
    private String name;

    private int imageId;

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public Fruit() {
    }

    public Fruit(String name, int imageId) {

        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
