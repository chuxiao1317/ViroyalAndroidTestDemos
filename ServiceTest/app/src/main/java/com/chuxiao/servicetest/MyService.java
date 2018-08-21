package com.chuxiao.servicetest;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class MyService extends Service {

    private DownloadBinder mBinder = new DownloadBinder();

    class DownloadBinder extends Binder {
        public void startDownload() {
            Toast.makeText(MyService.this, "开始下载", Toast.LENGTH_SHORT).show();
        }

        public int getProgress() {
            Toast.makeText(MyService.this, "正在下载", Toast.LENGTH_SHORT).show();
            return 0;
        }
    }

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "服务已创建", Toast.LENGTH_SHORT).show();
        /**
         * 开始写前台服务代码
         */
        Intent intent = new Intent(this, MainActivity.class);
        // 用于实现下拉状态栏通知的点击跳转
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(this, "")//Builder(Context)构造方法在API26中已被弃用
                .setContentTitle("这是通知标题")
                .setContentText("这是通知内容")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentIntent(pendingIntent)
                .build();
        // 以运行前台服务的形式调用通知
        startForeground(1, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "服务已启动", Toast.LENGTH_SHORT).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //TODO:处理具体的逻辑
                //子线程开启的服务会一直处于运行状态，可通过此方法让它执行完毕之后自动停止
//                stopSelf();
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "服务已销毁", Toast.LENGTH_SHORT).show();
    }
}
