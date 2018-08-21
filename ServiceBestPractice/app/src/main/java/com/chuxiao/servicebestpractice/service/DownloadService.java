package com.chuxiao.servicebestpractice.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.chuxiao.servicebestpractice.DownloadTask;
import com.chuxiao.servicebestpractice.MainActivity;
import com.chuxiao.servicebestpractice.R;
import com.chuxiao.servicebestpractice.interfaces.DownloadListener;

import java.io.File;

public class DownloadService extends Service {

    private DownloadTask downloadTask;

    private String  downloadUrl;

    public DownloadService() {
    }

    private DownloadListener listener = new DownloadListener() {
        @Override
        public void onProgress(int progress) {
            getNotificationManager().notify(1, getNotification("正在下载...", progress));
        }

        @Override
        public void onSuccess() {
            downloadTask = null;
            // 下载成功时前台服务通知关闭，并创建一个下载成功的通知
            stopForeground(true);
            // 通知下载成功，但不显示进度
            getNotificationManager().notify(1, getNotification("下载成功", -1));
            Toast.makeText(DownloadService.this, "下载成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed() {
            downloadTask = null;
            // 下载失败时将前台服务通知关闭，并创建一个下载失败的通知
            stopForeground(true);
            // 通知下载失败，无进度
            getNotificationManager().notify(1, getNotification("下载失败", -1));
            Toast.makeText(DownloadService.this, "下载失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPaused() {
            downloadTask = null;
            Toast.makeText(DownloadService.this, "暂停下载", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCanceled() {
            downloadTask = null;
            // 关闭前台服务通知
            stopForeground(true);
            Toast.makeText(DownloadService.this, "取消下载", Toast.LENGTH_SHORT).show();
        }
    };

    private DownloadBinder mBinder = new DownloadBinder();

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }

    public class DownloadBinder extends Binder {

        public void startDownload(String url) {
            if (downloadTask == null) {
                downloadUrl = url;
                downloadTask = new DownloadTask(listener);
                // 开始下载
                downloadTask.execute(downloadUrl);
                // 显示前台服务通知
                startForeground(1, getNotification("正在下载...", 0));
                Toast.makeText(DownloadService.this, "正在下载...", Toast.LENGTH_SHORT).show();
            }
        }

        public void pauseDownload() {
            if (downloadTask != null) {
                downloadTask.pauseDownload();
            }
        }

        public void cancelDownload() {
            if (downloadTask != null) {
                downloadTask.cancelDownload();
            }
            if (downloadUrl != null) {
                // 取消下载需将已下载文件删除,并将通知关闭
                String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
                String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                File file = new File(directory + fileName);
                if (file.exists()) {
                    file.delete();
                }
                // 关闭通知
                getNotificationManager().cancel(1);
                // 关闭前台服务通知
                stopForeground(true);
                Toast.makeText(DownloadService.this, "取消下载", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    private Notification getNotification(String title, int progress) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentIntent(pendingIntent);
        builder.setContentTitle(title);
        if (progress >= 0) {
            // 当progress大于或等于0时才需要显示下载进度
            builder.setContentText(progress + "%");
            // 显示进度条，参数1为最大进度，参数2为当前进度(参数1、2皆为参数传入)，参数3为是否使用模糊进度条
            builder.setProgress(100, progress, false);
        }
        return builder.build();
    }

}
