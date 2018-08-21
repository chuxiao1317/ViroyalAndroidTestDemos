package com.chuxiao.game2048.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chuxiao.game2048.R;
import com.chuxiao.game2048.config.Config;
import com.chuxiao.game2048.view.GameView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Activity的引用
    private MainActivity mMainActivity;
    // 记录分数的TextView
    private TextView mTvScore;
    // 历史最高分数
    private TextView mTvHighestScore;
    // 目标分数
    private TextView mTvGoal;
    private int mGoal;
    // 游戏面板
    private GameView mGameView;

    public MainActivity() {
        mMainActivity = this;
    }

    /**
     * 获取当前Activity的引用
     */
    public MainActivity getMainActivity() {
        return mMainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mGameView = new GameView(this);

        FrameLayout frameLayout = findViewById(R.id.game_panel);
        RelativeLayout relativeLayout = findViewById(R.id.rl_game_panel);
        // GameView居中
        relativeLayout.addView(mGameView);
    }

    private void initView() {
        mTvScore = findViewById(R.id.tv_score);
        mTvGoal = findViewById(R.id.tv_goal);
        mTvHighestScore = findViewById(R.id.tv_record);
        Button mBtnRestart = findViewById(R.id.btn_restart);
        Button mBtnOptions = findViewById(R.id.btn_option);
        Button mBtnRevert = findViewById(R.id.btn_revert);

        mBtnRevert.setOnClickListener(this);
        mBtnOptions.setOnClickListener(this);
        mBtnRestart.setOnClickListener(this);

        int mHighestScore = Config.mSp.getInt(Config.keyHighestScore, 0);
        mGoal = Config.mSp.getInt(Config.keyGameGoal, 2048);

        mTvHighestScore.setText(String.valueOf(mHighestScore));
        mTvGoal.setText(String.valueOf(mGoal));
        mTvScore.setText("0");

        setScore(0, 0);
    }

    /**
     * 点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_restart:
                mGameView.startGame();
                setScore(0, 0);
                break;
            case R.id.btn_revert:
                mGameView.revertLastSwipe();
                break;
            case R.id.btn_option:
                startActivityForResult(new Intent(this, ConfigActivity.class),
                        0);
                break;
        }
    }

    // 回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mGoal = Config.mSp.getInt(Config.keyGameGoal, 2048);
            mTvGoal.setText(String.valueOf(mGoal));
            getHighestScore();
        }
    }

    /**
     * 获取最高纪录
     */
    private void getHighestScore() {
        int score = Config.mSp.getInt(Config.keyHighestScore, 0);
        setScore(score, 1);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * 修改得分
     */
    public void setScore(int score, int flag) {
        switch (flag) {
            case 0:
                mTvScore.setText(String.valueOf(score));
                break;
            case 1:
                mTvHighestScore.setText(String.valueOf(score));
                break;
        }
    }

    /**
     * 修改目标分数
     */
    public void setGoal(int goal) {
        mTvGoal.setText(String.valueOf(goal));
    }
}
