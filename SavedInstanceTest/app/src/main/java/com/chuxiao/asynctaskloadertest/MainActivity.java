package com.chuxiao.asynctaskloadertest;

import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private static final int SEARCH_LOADER = 22;

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String LIFECYCLE_CALLBACKS_TEXT_KEY = "callbacks";

    private static final String ON_CREATE = "onCreate";
    private static final String ON_START = "onStart";
    private static final String ON_RESUME = "onResume";
    private static final String ON_PAUSE = "onPause";
    private static final String ON_STOP = "onStop";
    private static final String ON_RESTART = "onRestart";
    private static final String ON_DESTROY = "onDestroy";
    private static final String ON_SAVE_INSTANCE_STATE = "onSaveInstanceState";

    private TextView mLifecycleDisplay;

    private static final List<String> mLifecycleCallbacks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLifecycleDisplay = (TextView) findViewById(R.id.tv_lifecycle_events_display);

        // 如果有缓存，则显示在UI上
        if (savedInstanceState != null) {
            if (savedInstanceState//如果给定的键被包含在这个捆绑包的映射中，则返回true。
                    .containsKey((LIFECYCLE_CALLBACKS_TEXT_KEY))
                    ) {
                String callbacks = savedInstanceState.getString(LIFECYCLE_CALLBACKS_TEXT_KEY);
                mLifecycleDisplay.setText(callbacks);
            }
        }

        for (String callback : mLifecycleCallbacks) {
            mLifecycleDisplay.append(callback + "\n");
        }
        mLifecycleCallbacks.clear();

        // TODO (1) Use logAndAppend within onCreate
        logAndAppend(ON_CREATE);
    }

    // TODO (2) Override onStart, call super.onStart, and call logAndAppend with ON_START
    @Override
    protected void onStart() {
        super.onStart();
        logAndAppend(ON_START);
    }

    // TODO (3) Override onResume, call super.onResume, and call logAndAppend with ON_RESUME
    @Override
    protected void onResume() {
        super.onResume();
        logAndAppend(ON_RESUME);
        Toast.makeText(this, "请尝试旋转手机", Toast.LENGTH_SHORT).show();
    }

    // TODO (4) Override onPause, call super.onPause, and call logAndAppend with ON_PAUSE
    @Override
    protected void onPause() {
        super.onPause();
        logAndAppend(ON_PAUSE);
    }

    // TODO (5) Override onStop, call super.onStop, and call logAndAppend with ON_STOP
    @Override
    protected void onStop() {
        super.onStop();
        mLifecycleCallbacks.add(ON_STOP);
        logAndAppend(ON_STOP);
    }

    // TODO (6) Override onRestart, call super.onRestart, and call logAndAppend with ON_RESTART
    @Override
    protected void onRestart() {
        logAndAppend(ON_RESTART);
        super.onRestart();
    }

    // TODO (7) Override onDestroy, call super.onDestroy, and call logAndAppend with ON_DESTROY
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLifecycleCallbacks.add(ON_DESTROY);
        logAndAppend(ON_DESTROY);
    }

    /**
     * 缓存
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        logAndAppend(ON_SAVE_INSTANCE_STATE);
        String textViewContents = mLifecycleDisplay.getText().toString();
        outState.putString(LIFECYCLE_CALLBACKS_TEXT_KEY, textViewContents);
    }


    /**
     * Logs to the console and appends the lifecycle method name to the TextView so that you can
     * view the series of method callbacks that are called both from the app and from within
     * Android Studio's Logcat.
     *
     * @param lifecycleEvent The name of the event to be logged.
     */
    private void logAndAppend(String lifecycleEvent) {
        Log.d(TAG, "Lifecycle Event: " + lifecycleEvent);

        mLifecycleDisplay.append(lifecycleEvent + "\n");
    }

    /**
     * This method resets the contents of the TextView to its default text of "Lifecycle callbacks"
     *
     * @param view The View that was clicked. In this case, it is the Button from our layout.
     */
    public void resetLifecycleDisplay(View view) {
        mLifecycleDisplay.setText("Lifecycle callbacks:\n");
    }

    private void makeSearchQuery() {

    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
