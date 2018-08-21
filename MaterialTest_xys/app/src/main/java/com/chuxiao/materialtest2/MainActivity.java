package com.chuxiao.materialtest2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.ic_done_anim_000);
        toolbar.setTitle("主标题");
        toolbar.setSubtitle("副标题");
        setSupportActionBar(toolbar);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);
        // 创建Palette对象
        new Palette.Builder(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(@NonNull Palette palette) {
                Log.d("getVibrantSwatch", palette.getVibrantSwatch() + "");
                Log.d("getDarkVibrantSwatch", palette.getDarkVibrantSwatch() + "");
                Log.d("getLightVibrantSwatch", palette.getLightVibrantSwatch() + "");
                Log.d("getMutedSwatch", palette.getMutedSwatch() + "");
                Log.d("getDarkMutedSwatch", palette.getDarkMutedSwatch() + "");
                Log.d("getLightMutedSwatch", palette.getLightMutedSwatch() + "");
                // 注意此处并不是每一个swatch都能获取到（可能为null），所以实际项目中应加以判断或设置默认值
                // 即getXXXSwatch（int defaultColor）
                Palette.Swatch vibrantSwatch = palette.getMutedSwatch();

                // 设置ActionBar颜色
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(vibrantSwatch.getRgb()));

                // 设置系统状态栏颜色
                Window window = getWindow();
                window.setStatusBarColor(vibrantSwatch.getRgb());
            }
        });
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_map) {
            openLocationInMap();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openLocationInMap() {
        String addressString = "1600 Ampitheatre Parkway, CA";
        Uri geoLocation = Uri.parse("geo:0,0?q=" + addressString);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d(MainActivity.class.getSimpleName(), "Couldn't call " + geoLocation.toString()
                    + ", no receiving apps installed!");
        }
    }

    /**
     * 活动跳转
     */
    public void eventToSecondActivity(View view) {
        startActivity(new Intent(this, SecondActivity.class));
    }
}
