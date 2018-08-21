package com.chuxiao.materialtest2;

import android.animation.Animator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

public class FifthActivity extends AppCompatActivity {

    private boolean mIsCheck;
    private static final int[] STATE_CHECKED = new int[]{
            android.R.attr.state_checked
    };
    private static final int[] STATE_UNCHECKED = new int[]{};
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifth);

        mImageView = findViewById(R.id.image);
//        Drawable mDrawable = getResources().getDrawable(R.drawable.state_change_animator);

        final View oval = findViewById(R.id.oval);
        oval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animator animator = ViewAnimationUtils.createCircularReveal(
                        oval,
                        oval.getWidth() / 2,// 圆心X坐标
                        oval.getHeight() / 2,// 圆心纵坐标
                        0,// 起始半径
                        oval.getWidth()// 结束半径
                );
                // 两头慢中间快的插值器
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.setDuration(3000);
                animator.start();
            }
        });

        final View rect = findViewById(R.id.rect);
        rect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animator animator = ViewAnimationUtils.createCircularReveal(
                        rect,
                        0,
                        0,
                        0,
                        // 根据矩形宽高求对角线长度，作为动画圆的结束半径（此方法为求直角三角形的斜边长）
                        (float) Math.hypot(rect.getWidth(), rect.getHeight())
                );
                // 由慢变快的插值器
                animator.setInterpolator(new AccelerateInterpolator());
                animator.setDuration(3000);
                animator.start();
            }
        });
    }

    public void btnStateChangeAnimator(View view) {
        if (mIsCheck) {
            mImageView.setImageState(STATE_UNCHECKED, true);
            mIsCheck = false;
        } else {
            mImageView.setImageState(STATE_CHECKED, true);
            mIsCheck = true;
        }
    }

    public void intentToSexthActivity(View view) {
        startActivity(new Intent(this, SixthActivity.class));
    }
}
