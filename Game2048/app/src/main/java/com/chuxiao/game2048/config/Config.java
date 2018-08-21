package com.chuxiao.game2048.config;

import android.app.Application;
import android.content.SharedPreferences;

public class Config extends Application {

    // SP对象
    public static SharedPreferences mSp;

    // Game Goal
    public static int mGameGoal;

    // GameView行列数
    public static int mGameLines;

    // Item宽高
    public int mItemSize = 0;

    // 记录分数
    public static int score = 0;
    public static String spHighestScore = "SP_HIGHEST_SCORE";
    public static String keyHighestScore = "KEY_HIGHEST_SCORE";
    public static String keyGameLines = "KEY_GAME_LINES";
    public static String keyGameGoal = "KEY_GAME_GOAL";

    @Override
    public void onCreate() {
        super.onCreate();
        mSp = getSharedPreferences(spHighestScore, 0);
        mGameLines = mSp.getInt(keyGameLines, 4);
        mGameGoal = mSp.getInt(keyGameGoal, 2048);
        mItemSize = 0;
    }
}
