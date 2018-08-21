package com.chuxiao.norificationtest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button sendNoticeBtn = findViewById(R.id.send_notice_btn);
        sendNoticeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_notice_btn:
//                Intent intent = new Intent(this, NotificationActivity.class);
//                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
                // 获取通知管理器
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                Notification notification = new NotificationCompat.Builder(this, "")//Builder(Context)构造方法在API26中已被弃用
                        .setContentTitle("内容标题")
                        .setContentText("内容正文")
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
//                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)//点击后自动取消
                        .setSound(Uri.fromFile(new File("/system/media/audio/ringtones/Luna.ogg")))
                        .setVibrate(new long[]{0, 500, 0, 0})
                        .setLights(Color.GREEN,1000,1000)
//                        .setDefaults(NotificationCompat.DEFAULT_ALL)
//                        .setStyle(new NotificationCompat.BigTextStyle().bigText("很长的通知很长的通知很长的通知" +
//                                "很长的通知很长的通知很长的通知很长的通知很长的通知很长的通知很长的通知"))
                        .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.big_image)))
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .build();
                if (manager != null) {
                    manager.notify(1, notification);
                }
                break;
            default:
                break;
        }
    }
}
