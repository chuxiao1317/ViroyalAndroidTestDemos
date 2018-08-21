package com.chuxiao.servicetest;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * 总会有人忘记开启线程，或者忘记调用stopSelf自动停止服务，因此Android专门提供IntentService自动在子线程中运行服务，自动停止服务
 */
public class MyIntentService extends IntentService {
//    /**
//     * Creates an IntentService.  Invoked by your subclass's constructor.
//     *
//     * @param name Used to name the worker thread, important only for debugging.
//     */
//    public MyIntentService(String name) {
//        //
//        super(name);
//    }

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // 此方法已在子线程中运行，此处乃证实
//        Toast.makeText(this, "子线程id：" + Thread.currentThread().getId(), Toast.LENGTH_SHORT).show();
        Log.d("MyIntentService", "子线程id：" + Thread.currentThread().getId());
//        Handler mHandler = new Handler(getMainLooper());
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(MyIntentService.this, "子线程id：" + Thread.currentThread().getId(), Toast.LENGTH_SHORT).show();
//            }
//        });
        Handler mHandler = new Handler(getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MyIntentService.this, "服务已在子线程启动,请在控制台查看线程id", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //IntentService特性：服务运行结束自动停止
        Toast.makeText(this, "服务已自动销毁：", Toast.LENGTH_SHORT).show();
    }
}
