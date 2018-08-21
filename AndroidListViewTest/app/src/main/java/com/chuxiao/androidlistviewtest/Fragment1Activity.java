package com.chuxiao.androidlistviewtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.chuxiao.androidlistviewtest.fragment.FirstFragment;
import com.chuxiao.androidlistviewtest.fragment.SecondFragment;

public class Fragment1Activity extends AppCompatActivity {

    private FirstFragment firstFragment;
    private SecondFragment secondFragment;
    private String status;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment1);

        //方式1
//        firstFragment = ((FirstFragment) getSupportFragmentManager().findFragmentById(R.id.first));
//        secondFragment = ((SecondFragment) getSupportFragmentManager().findFragmentById(R.id.second));


//        getSupportFragmentManager().findFragmentByTag("aaa")

        //方式二
        FirstFragment firstFragment = new FirstFragment();
        firstFragment.setParmas("aaa");
        getSupportFragmentManager().beginTransaction().replace(R.id.fl1, firstFragment).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.fl2, new SecondFragment()).commit();


        //activity与Fragment通信   //setArgument和getArgument
        //Fragment与Activity通信（传值）   接口回调

        firstFragment.setFragmentInface(new FirstFragment.FragmentInface() {
            @Override
            public void result(String type) {
                status = type;
                Log.d("result", type);
            }
        });
    }


    public void getStatus(String status) {
        Log.d("getStatus", status);
    }


    public void clik2(View view) {
        startActivity(new Intent(Fragment1Activity.this, FragmentViewPagerActivity.class));
    }
}
