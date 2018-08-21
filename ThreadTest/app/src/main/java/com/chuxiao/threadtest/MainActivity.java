package com.chuxiao.threadtest;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textView;

    public static final int UPDATE_TEXT = 1;

//    private Handler handler = new Handler() {
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case UPDATE_TEXT:
//                    //TODO:在这里进行UI操作
//                    textView.setText("你好！");
//                    break;
//                default:
//                    break;
//            }
//        }
//    };


    MyHandler myHandler = new MyHandler(MainActivity.this);

    private static class MyHandler extends Handler {
        private final WeakReference<MainActivity> mainActivityWeakReference;

        private MyHandler(MainActivity activity) {
            mainActivityWeakReference = new WeakReference<>(activity);
        }

        /**
         * 在主线程中运行
         * */
        @Override
        public void handleMessage(Message msg) {
            MainActivity mainActivity = mainActivityWeakReference.get();
            super.handleMessage(msg);
            if (mainActivity != null) {
                switch (msg.what) {
                    case UPDATE_TEXT:
                        //TODO:在这里进行UI操作
                        mainActivity.textView.setText("你好！");
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.text);
        Button changeTextBtn = findViewById(R.id.change_text_btn);
        changeTextBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_text_btn:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 在子线程中更新UI会导致程序崩溃
//                        textView.setText("你好！");
                        Message message = new Message();
                        message.what = UPDATE_TEXT;
                        // 发送message
//                        handler.sendMessage(message);
                        myHandler.sendMessage(message);
                    }
                }).start();
                break;
            default:
        }
    }
}
