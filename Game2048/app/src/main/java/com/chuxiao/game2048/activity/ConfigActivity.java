package com.chuxiao.game2048.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.chuxiao.game2048.R;
import com.chuxiao.game2048.config.Config;

public class ConfigActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnGameLines;

    private Button mBtnGoal;

    private String[] mGameLinesList;

    private String[] mGameGoalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        initView();
    }

    private void initView() {
        mBtnGameLines = findViewById(R.id.btn_game_lines);
        mBtnGoal = findViewById(R.id.btn_goal);
        Button mBtnBack = findViewById(R.id.btn_back);
        Button mBtnDone = findViewById(R.id.btn_done);

        mBtnGameLines.setText(String.valueOf(Config.mSp.getInt(Config.keyGameLines, 4)));
        mBtnGoal.setText(String.valueOf(Config.mSp.getInt(Config.keyGameGoal, 2048)));

        mBtnGameLines.setOnClickListener(this);
        mBtnGoal.setOnClickListener(this);
        mBtnBack.setOnClickListener(this);
        mBtnDone.setOnClickListener(this);

        mGameLinesList = new String[]{"4", "5", "6"};
        mGameGoalList = new String[]{"1024", "2048", "4096"};
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_game_lines:
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
                mBuilder.setTitle("选择行列数");
                mBuilder.setItems(mGameLinesList,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mBtnGameLines.setText(mGameLinesList[which]);
                            }
                        });
                mBuilder.create().show();
                break;
            case R.id.btn_goal:
                mBuilder = new AlertDialog.Builder(this);
                mBuilder.setTitle("选择目标分数");
                mBuilder.setItems(mGameGoalList,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mBtnGoal.setText(mGameGoalList[which]);
                            }
                        });
                mBuilder.create().show();
                break;
            case R.id.btn_back:
                this.finish();
                break;
            case R.id.btn_done:
                saveConfig();
                setResult(RESULT_OK);
                this.finish();
                break;
        }
    }

    /**
     * 保存配置
     */
    private void saveConfig() {
        SharedPreferences.Editor editor = Config.mSp.edit();
        editor.putInt(Config.keyGameLines,
                Integer.parseInt(mBtnGoal.getText().toString()));
        editor.putInt(Config.keyGameGoal,
                Integer.parseInt(mBtnGoal.getText().toString()));
        editor.commit();
    }
}
