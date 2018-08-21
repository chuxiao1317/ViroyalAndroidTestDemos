package com.chuxiao.androidtest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * 用于测试知晓当前处于哪一个活动
 */
public class BaseActivity extends AppCompatActivity {

    /**
     * 重写onCreate方法，此后每个活动都会加入到活动管理器中，从而使得程序中任何地方只要调用ActivityController.finishAll()方法就可以立刻退出程序
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("BaseActivity告知，此时所在活动：", getClass().getSimpleName());
        // 将活动添加到活动管理器中
        ActivityController.addActivity(this);
    }

    /**
     * 重写onDestroy方法，
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 从活动管理器中移除一个将要销毁的活动，因为即将销毁，故可以直接移除，无需通过Activity管理器管理
        ActivityController.removeActivity(this);
    }
}
