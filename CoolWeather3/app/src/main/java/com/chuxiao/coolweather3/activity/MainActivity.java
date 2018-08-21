package com.chuxiao.coolweather3.activity;

import android.os.Bundle;

import com.chuxiao.coolweather3.R;
import com.chuxiao.coolweather3.fragment.ChooseAreaFragment;
import com.viroyal.sloth.app.SlothActivity;

import static com.chuxiao.coolweather3.fragment.ChooseAreaFragment.LEVEL_CITY;
import static com.chuxiao.coolweather3.fragment.ChooseAreaFragment.LEVEL_COUNTY;
import static com.chuxiao.coolweather3.fragment.ChooseAreaFragment.LEVEL_PROVINCE;
import static com.chuxiao.coolweather3.fragment.ChooseAreaFragment.currentLevel;

public class MainActivity extends SlothActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 返回
     */
    @Override
    public void onBackPressed() {
        if (currentLevel == LEVEL_PROVINCE) {
            finish();
        }
        ChooseAreaFragment fragment =
                (ChooseAreaFragment) getSupportFragmentManager().findFragmentById(
                        R.id.choose_area_fragment);
        fragment.backPreviousLevel();
    }
}
