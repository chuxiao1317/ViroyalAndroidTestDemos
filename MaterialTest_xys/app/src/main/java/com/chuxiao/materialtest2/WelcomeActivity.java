package com.chuxiao.materialtest2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

public class WelcomeActivity extends AppCompatActivity {

    private ImageView welcomeImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        welcomeImg = findViewById(R.id.img_welcome);
        AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(500);
        welcomeImg.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                welcomeImg.setBackgroundResource(R.drawable.welcome);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                skip();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void skip() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}
