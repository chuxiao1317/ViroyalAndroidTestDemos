package com.chuxiao.scrollviewtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.chuxiao.scrollviewtest.view.DragView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void scroll(View view) {
        DragView dragView = findViewById(R.id.drag_view);
        dragView.scroll();
    }

    public void intent(View view) {
        startActivity(new Intent(MainActivity.this, SecondActivity.class));
    }
}
