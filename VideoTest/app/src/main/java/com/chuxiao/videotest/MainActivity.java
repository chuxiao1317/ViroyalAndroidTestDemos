package com.chuxiao.videotest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        videoView = findViewById(R.id.video_view);
        Button playVideoBtn = findViewById(R.id.play_video_btn);
        Button pauseVideoBtn = findViewById(R.id.pause_video_btn);
        Button replayVideoBtn = findViewById(R.id.replay_video_btn);
        playVideoBtn.setOnClickListener(this);
        pauseVideoBtn.setOnClickListener(this);
        replayVideoBtn.setOnClickListener(this);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            initVideoPath();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initVideoPath();
                } else {
                    Toast.makeText(this, "拒绝权限将无法使用程序", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    /**
     * 初始化视频播放器
     */
    private void initVideoPath() {
        File file = new File(Environment.getExternalStorageDirectory(), "歌词创作.mp4");
        videoView.setVideoPath(file.getPath());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_video_btn:
                if (!videoView.isPlaying()) {
                    //开始播放
                    videoView.start();
                }
                break;
            case R.id.pause_video_btn:
                if (videoView.isPlaying()) {
                    // 暂停播放
                    videoView.pause();
                }
                break;
            case R.id.replay_video_btn:
//                if (videoView.isPlaying()) {
                videoView.resume();
//                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoView != null) {
            // 释放掉ViewView占用资源
            videoView.suspend();
        }
    }
}
