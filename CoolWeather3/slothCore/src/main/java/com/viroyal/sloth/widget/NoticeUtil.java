package com.viroyal.sloth.widget;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.sloth.core.R;

/**
 * Created by qjj on 2017/2/17.
 */
public class NoticeUtil {

    /** Notification构造器 */
    NotificationCompat.Builder mBuilder;
    /** Notification的ID */
    int notifyId = 100;
    Context mContext;

    /** Notification管理 */
    public NotificationManager mNotificationManager;
    Intent mIntent;//点击启动的activity
    String mTitle;
    String mMsg;
    String mTicker;
    int mSmallIcon;
    public static void showNotice(Context context, Intent intent, String title, String msg, String ticker, int icon) {
        NoticeUtil n = new NoticeUtil();
        n.mNotificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        n.mContext = context;
        n.mIntent = intent;
        n.mTitle = title;
        n.mTicker = ticker;
        n.mMsg = msg;
        n.mSmallIcon = icon;
        n.showIntentActivityNotify();
    }
    public PendingIntent getDefalutIntent(int flags){
        PendingIntent pendingIntent= PendingIntent.getActivity(mContext, 1, new Intent(), flags);
        return pendingIntent;
    }
    public void initView(){
        mBuilder = new NotificationCompat.Builder(mContext);
        mBuilder.setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL))
//				.setNumber(number)//显示数量
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示
                .setPriority(Notification.PRIORITY_DEFAULT)//设置该通知优先级
//				.setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
                //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                .setSmallIcon(mSmallIcon);
    }

    /** 初始化通知栏 */
    private void initNotify(){
        mBuilder = new NotificationCompat.Builder(mContext);
        mBuilder.setContentTitle(mTitle)
                .setContentText(mMsg)
                .setTicker(mTicker)//通知首次出现在通知栏，带上升动画效果的
                .setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL))
//				.setNumber(number)//显示数量
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示
                .setPriority(Notification.PRIORITY_DEFAULT)//设置该通知优先级
//				.setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
                //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                .setSmallIcon(mSmallIcon);
    }
    /** 显示通知栏点击跳转到指定Activity */
    public void showIntentActivityNotify(){
        // Notification.FLAG_ONGOING_EVENT --设置常驻 Flag;Notification.FLAG_AUTO_CANCEL 通知栏上点击此通知后自动清除此通知
//		notification.flags = Notification.FLAG_AUTO_CANCEL; //在通知栏上点击此通知后自动清除此通知
//        mBuilder.setAutoCancel(true)//点击后让通知将消失
//                .setContentTitle("测试标题")
//                .setContentText("点击跳转")
//                .setTicker("点我");
//        //点击的意图ACTION是跳转到Intent
//        mIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0,mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        mBuilder.setContentIntent(pendingIntent);
//        mNotificationManager.notify(notifyId, mBuilder.build());

        //点击的意图ACTION是跳转到Intent
        mBuilder = new NotificationCompat.Builder(mContext);
        mBuilder.setAutoCancel(true)//点击后让通知将消失
                .setContentTitle(mTitle)
                .setContentText(mMsg)
                .setTicker(mTicker)
                .setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL))
//				.setNumber(number)//显示数量
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示
                .setPriority(Notification.PRIORITY_DEFAULT)//设置该通知优先级
//				.setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
                //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                .setSmallIcon(mSmallIcon);

        mIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0,mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        mNotificationManager.notify(notifyId, mBuilder.build());
    }

    /** 显示通知栏 */
    public void showNotify(){
        mBuilder.setContentTitle(mTitle)
                .setContentText(mMsg)
                .setTicker(mTicker);//通知首次出现在通知栏，带上升动画效果的
        mNotificationManager.notify(notifyId, mBuilder.build());
//		mNotification.notify(getResources().getString(R.string.app_name), notiId, mBuilder.build());
    }
}
