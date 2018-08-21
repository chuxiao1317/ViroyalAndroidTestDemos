package com.chuxiao.materialtest2;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

public class SixthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sixth);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.ic_done_anim_000);
        toolbar.setTitle("主标题");
        toolbar.setSubtitle("副标题");
        setSupportActionBar(toolbar);

        // 设置侧滑菜单的左上角点击事件
        DrawerLayout drawerLayout = findViewById(R.id.drawer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.abc_action_bar_home_description,
                R.string.abc_action_bar_home_description
        );
        drawerToggle.syncState();
        drawerLayout.addDrawerListener(drawerToggle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(
                menu.findItem(R.id.action_share)
        );
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/*");
        shareActionProvider.setShareIntent(intent);
        return super.onCreateOptionsMenu(menu);
    }
}
