package com.chuxiao.materialtest2;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class ThirdActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
    }

    /**
     * 跳转界面，设置过渡动画
     */
    public void explodeIntent(View view) {
        intent = new Intent(this, FourthActivity.class);
        intent.putExtra("flag", 0);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    public void slideIntent(View view) {
        intent = new Intent(this, FourthActivity.class);
        intent.putExtra("flag", 1);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    public void fadeIntent(View view) {
        intent = new Intent(this, FourthActivity.class);
        intent.putExtra("flag", 2);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    public void shareIntent(View view) {
        View fabButton = findViewById(R.id.bt_fab);
        intent = new Intent(this, FourthActivity.class);
        intent.putExtra("flag", 3);
        startActivity(intent,
                ActivityOptions.makeSceneTransitionAnimation(
                        this,
                        // 创建多个共享元素
                        Pair.create(view, "share"),
                        Pair.create(fabButton, "fabButton")).toBundle());
    }
}
