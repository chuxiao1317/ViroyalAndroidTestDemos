package com.chuxiao.androiduserdefineduitest.layout;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chuxiao.androiduserdefineduitest.R;

/**
 * Created by 12525 on 2018/4/26.
 */

public class TitleLayout extends LinearLayout {

    public TitleLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.title, this);
        Button titleBackBtn = findViewById(R.id.title_back_btn);
        Button titleEditBtn = findViewById(R.id.title_edit_btn);
        titleBackBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) getContext()).finish();
            }
        });
        titleEditBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "你点击了编辑按钮", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
