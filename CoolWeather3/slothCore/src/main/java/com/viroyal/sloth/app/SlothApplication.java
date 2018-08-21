// (c)2016 Flipboard Inc, All Rights Reserved.

package com.viroyal.sloth.app;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.squareup.leakcanary.RefWatcher;
import com.viroyal.sloth.network.HttpManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;


public class SlothApplication extends MultiDexApplication {
    private static SlothApplication INSTANCE;
    private RefWatcher refWatcher;

    public static SlothApplication getInstance() {
        return INSTANCE;
    }

    public static RefWatcher getRefWatcher(Context context) {
        SlothApplication application = (SlothApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        INSTANCE = this;
        //初始化fresco
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = HttpManager.getFrescoHttpClient(HttpLoggingInterceptor.Level.BODY); // build on your own
                ImagePipelineConfig config = OkHttpImagePipelineConfigFactory
                        .newBuilder(INSTANCE, okHttpClient)
                        .build();
                Fresco.initialize(INSTANCE, config);
                //内存泄漏检测
                //refWatcher = LeakCanary.install(this);
            }
        }).start();
    }
}
