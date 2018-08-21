package com.viroyal.sloth.widget.dialog;

/**
 * Created by cxy on 2016/7/27.
 * 单选/多选list测试item
 */
public class SelectItem {
    private int id;
    private String name;
    private boolean isChecked;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}
