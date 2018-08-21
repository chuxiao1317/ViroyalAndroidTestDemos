package com.chuxiao.materialtest2;

import android.content.Intent;
import android.graphics.Outline;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.Window;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

//        Toolbar toolbar = findViewById(R.id.toolbar);
////        toolbar.setLogo(R.drawable.ic_done_anim_000);
////        toolbar.setTitle("主标题");
////        toolbar.setSubtitle("副标题");
//        setSupportActionBar(toolbar);

        View tvRect = findViewById(R.id.tv_rect);
        View tvCircle = findViewById(R.id.tv_circle);
        // 获取Outline
        ViewOutlineProvider tvRectOutline = new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                // 修改outline为圆角矩形
                outline.setRoundRect(
                        0,
                        0,
                        view.getWidth(),
                        view.getHeight(),
                        30
                );
            }
        };
        ViewOutlineProvider tvCircleOutline = new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                // 修改outline为圆形(根据外界矩阵画椭圆)
                outline.setOval(
                        0,
                        0,
                        view.getWidth(),
                        view.getHeight()
                );
            }
        };
        // 重新设置形状
        tvRect.setOutlineProvider(tvRectOutline);
        tvCircle.setOutlineProvider(tvCircleOutline);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        menuItem.setIntent(createShareForecastIntent());
        return true;
    }

    private Intent createShareForecastIntent() {
        return ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText("内容")
                .getIntent();
    }

    public void eventToThirdActivity(View view) {
        startActivity(new Intent(this, ThirdActivity.class));
    }
}
