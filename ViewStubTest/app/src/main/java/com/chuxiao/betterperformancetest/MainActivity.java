package com.chuxiao.betterperformancetest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ViewStub mViewStub1;
    private ViewStub mViewStub2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewStub1 = findViewById(R.id.viewStub1);
        mViewStub2 = findViewById(R.id.viewStub2);
    }

    public void clickToVisible(View view) {
        View v = findViewById(R.id.viewStub1InflateId);
        if (v != null) {
            Toast.makeText(this, "此方法不能对延迟加载的控件再进一步操作", Toast.LENGTH_SHORT).show();
            return;
        }
        mViewStub1.setVisibility(View.VISIBLE);
    }

    public void clickToInflate(View view) {
        View v = findViewById(R.id.viewStub2InflateId);
        if (v != null) {
            Toast.makeText(this, "资源id已被改变，请不要对空的资源id进行inflate", Toast.LENGTH_SHORT).show();
            return;
        }
        View inflateView = mViewStub2.inflate();
        TextView textView = inflateView.findViewById(R.id.tv);
        textView.setText("文字内容已被改变");
    }
}
