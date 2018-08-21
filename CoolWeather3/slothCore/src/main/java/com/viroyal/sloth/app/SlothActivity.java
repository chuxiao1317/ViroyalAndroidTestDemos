package com.viroyal.sloth.app;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.hwangjr.rxbus.RxBus;
import com.viroyal.sloth.util.DateStyle;
import com.viroyal.sloth.util.DateUtils;
import com.viroyal.sloth.util.Slog;
import com.viroyal.sloth.util.ToastUtils;
import com.viroyal.sloth.widget.dialog.ProgressDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LiGang on 2016/4/22.
 */
@SuppressLint("Registered")
public class SlothActivity extends AppCompatActivity {

    protected final String TAG = getClass().getSimpleName();

    public static final String BROADCAST_ACTION = "com.viroyal.permission.capture_complete";
    public static final String REGISTER_BROADCAST_ACTION = "com.viroyal.permission.capture";
    //    public LocalBroadcastManager mLocalBroadcastManager;
    private String mCaptureId;
    boolean mResume = false;

    Intent mIntentToStart = null;

    private List<Object> mRxBusList;

    protected ProgressDialog mProgressDlg;
    boolean mNeedToDismissProgressDlg = false;
    boolean mNeedToShowProgressDlg = false;
    MyBroadcastReceiver mMyBroadcastReceiver = new MyBroadcastReceiver();
    /**
     * 用来管理Subscribe的生命周期
     * 在Activity中用到的subscriptin一定要加到这个里面，在onDestroy的时候会
     * 释放掉。
     */
    private CompositeSubscription mCompositeSubscription
            = new CompositeSubscription();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Slog.state(TAG, "---------onCreate");
        super.onCreate(savedInstanceState);
        //Activity管理
        ActivityStackManager.getInstance().addActivity(this);
//        mLocalBroadcastManager =  LocalBroadcastManager.getInstance(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Slog.state(TAG, "---------onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mResume = true;
        startActivity();
        checkProgress();
        Slog.state(TAG, "---------onResume");
        mMyBroadcastReceiver.register(this);
    }


    @Override
    protected void onPause() {
        mMyBroadcastReceiver.unRegister(this);
        super.onPause();
        mResume = false;
        Slog.state(TAG, "---------onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Slog.state(TAG, "---------onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Slog.state(TAG, "---------onRestart");
    }


    @Override
    protected void onDestroy() {
        Slog.state(TAG, "---------onDestroy ");
        unregisterRxBus();
        super.onDestroy();
        ActivityStackManager.getInstance().removeActivity(this);
        mCompositeSubscription.unsubscribe();
    }

    public void startActivitySloth(Intent intent) {
        if (intent != null) {
            if (mResume) {
                startActivity(intent);
            } else {
                mIntentToStart = intent;
            }
        }
    }

    private void startActivity() {
        if (mIntentToStart != null && mResume) {
            startActivity(mIntentToStart);
            mIntentToStart = null;
        }
    }

    public void addRxSubscription(Subscription sub) {
        if (sub != null) {
            mCompositeSubscription.add(sub);
        }
    }

    public void removeRxSubscription(Subscription sub) {
        mCompositeSubscription.remove(sub);
    }

    public void registerRxBus(Object o) {
        if (mRxBusList == null) {
            mRxBusList = new ArrayList<>();
        }
        RxBus.get().register(o);
        mRxBusList.add(o);
    }

    public void unregisterRxBus(Object o) {
        if (mRxBusList != null && mRxBusList.contains(o)) {
            RxBus.get().unregister(o);
            mRxBusList.remove(o);
        }
    }

    public void unregisterRxBus() {
        if (mRxBusList != null && mRxBusList.size() > 0) {
            for (Object o : mRxBusList) {
                RxBus.get().unregister(o);
            }
            mRxBusList.clear();
        }
    }

    public void showProgress() {
        if (mProgressDlg == null) {
            mProgressDlg = ProgressDialog.newInstance();
        }
        if (mResume) {
            mProgressDlg.show(getFragmentManager(), "");
            mNeedToShowProgressDlg = false;
        } else {
            mNeedToShowProgressDlg = true;
        }
    }

    private void checkProgress() {
        if (mProgressDlg != null && mResume) {
            if (mNeedToShowProgressDlg) {
                mProgressDlg.show(getFragmentManager(), "");
                mNeedToShowProgressDlg = false;
            }
            if (mNeedToDismissProgressDlg) {
                mProgressDlg.dismiss();
                mNeedToDismissProgressDlg = false;
            }
        }
    }

    public void dismissProgress() {
        if (mProgressDlg != null) {
            if (mResume) {
                mProgressDlg.dismiss();
                mNeedToDismissProgressDlg = false;
            } else {
                mNeedToDismissProgressDlg = true;
            }
        }
    }

    public void showToast(String msg) {
        ToastUtils.showToast(this, msg);
    }

    public void showToastLong(String msg) {
        ToastUtils.showToastLong(this, msg);
    }

    public void handleToast(int code, String msg) {

    }

    //截图
    private void capture() {
        viewSaveToImage(getRootView());
    }

    //截图并保存
    private void viewSaveToImage(View view) {
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        view.setDrawingCacheBackgroundColor(Color.WHITE);

        // 把一个View转换成图片
        Bitmap cachebmp = loadBitmapFromView(view);

        // 添加水印
        Bitmap bitmap = Bitmap.createBitmap(cachebmp);

        FileOutputStream fos;
        String time = DateUtils.format(new Date(), "yyyy-MM-dd-HH-mm-ss");
        long timestamp = System.currentTimeMillis();
        String path = "/sdcard/capture/";
        String fileName = "capture-" + time + "-" + timestamp + ".png";
        String ret = "";
        String msg = "";
        try {
            // 判断手机设备是否有SD卡
            boolean isHasSDCard = Environment.getExternalStorageState().equals(
                    android.os.Environment.MEDIA_MOUNTED);
            if (isHasSDCard) {
                // SD卡根目录

                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                fos = new FileOutputStream(path + fileName);
//                fos = new FileOutputStream(file);
            } else {
                msg = "创建文件失败!";
                throw new Exception("创建文件失败!");
            }
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);

            fos.flush();
            fos.close();
        } catch (Exception e) {
            ret = "0";
            e.printStackTrace();
        }

        Slog.d(TAG, BROADCAST_ACTION);
        Intent intent = new Intent();
        intent.setAction(BROADCAST_ACTION);
        intent.putExtra("path", path + fileName);
        intent.putExtra("pkgName", getPackageName());
        intent.putExtra("ret", ret);
        intent.putExtra("msg", msg);
        intent.putExtra("id", mCaptureId);
//        mLocalBroadcastManager.sendBroadcast(intent);
        sendBroadcast(intent);
        view.destroyDrawingCache();
    }

    //获取根view
    private View getRootView() {
        return ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
    }

    private Bitmap loadBitmapFromView(View v) {
        int w = v.getWidth();
        int h = v.getHeight();

        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);

        c.drawColor(Color.WHITE);
        /** 如果不设置canvas画布为白色，则生成透明 */

        v.layout(0, 0, w, h);
        v.draw(c);

        return bmp;
    }

    class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Slog.d(TAG, REGISTER_BROADCAST_ACTION);
            mCaptureId = intent.getStringExtra("id");
            capture();
        }

        public void register(Context context) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(REGISTER_BROADCAST_ACTION);
            registerReceiver(this, filter);
//            mLocalBroadcastManager.registerReceiver(mMyBroadcastReceiver, filter);

        }

        public void unRegister(Context context) {
            unregisterReceiver(this);
//            mLocalBroadcastManager.unregisterReceiver(mMyBroadcastReceiver);
        }
    }
}
