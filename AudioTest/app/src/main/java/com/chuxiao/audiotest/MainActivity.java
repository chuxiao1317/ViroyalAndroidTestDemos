package com.chuxiao.audiotest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button playAudioBtn = findViewById(R.id.play_audio_btn);
        Button pauseAudioBtn = findViewById(R.id.pause_audio_btn);
        Button stopAudioBtn = findViewById(R.id.stop_audio_btn);
        playAudioBtn.setOnClickListener(this);
        pauseAudioBtn.setOnClickListener(this);
        stopAudioBtn.setOnClickListener(this);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            //初始化音频播放器
            initMediaPlayer();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initMediaPlayer();
                } else {
                    Toast.makeText(this, "拒绝权限将无法使用程序", Toast.LENGTH_SHORT).show();
                    // 关闭程序
                    finish();
                }
                break;
            default:
        }
    }

    /**
     * 初始化音频播放器
     */
    private void initMediaPlayer() {
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "群星 - CCTV新闻联播片头.mp3");
            // 指定音频文件路径
            mediaPlayer.setDataSource(file.getPath());
            // 让MediaPlayer进入准备状态
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_audio_btn:
                if (!mediaPlayer.isPlaying()) {
                    //开始播放
                    mediaPlayer.start();
                }
                break;
            case R.id.pause_audio_btn:
                if (mediaPlayer.isPlaying()) {
                    //暂停播放
                    mediaPlayer.pause();
                }
                break;
            case R.id.stop_audio_btn:
                if (mediaPlayer.isPlaying()) {
                    //停止播放，将MediaPlayer对象重置到初始状态
                    mediaPlayer.reset();
                    //此时用户如果再次点击播放按钮，则会从头开始播放
                    initMediaPlayer();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            //停止播放音频，此方法后MediaPlayer无法再次播放音频
            mediaPlayer.stop();
            // 释放掉与MediaPlayer对象相关的资源
            mediaPlayer.release();
        }
    }
}
