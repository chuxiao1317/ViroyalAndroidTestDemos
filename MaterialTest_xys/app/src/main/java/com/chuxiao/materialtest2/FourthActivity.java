package com.chuxiao.materialtest2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.View;
import android.view.Window;

public class FourthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置Activity过渡动画
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        int flag = getIntent().getExtras().getInt("flag");
        // 设置不同的动画效果
        switch (flag) {
            case 0:
                getWindow().setEnterTransition(new Explode());
                break;
            case 1:
                getWindow().setEnterTransition(new Slide());
                break;
            case 2:
                getWindow().setEnterTransition(new Fade());
//                getWindow().setExitTransition(new Fade());
                break;
        }
        setContentView(R.layout.activity_fourth);
    }

    public void intentToFifthActivity(View view) {
        startActivity(new Intent(this, FifthActivity.class));
    }
}
