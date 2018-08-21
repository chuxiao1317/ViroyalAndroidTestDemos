package com.chuxiao.androidwidgetstest;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.chuxiao.androidwidgetstest.fragment.AnotherRightFragment;

public class FragmentTestActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_test);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        // 手动添加碎片
//        replaceFragment(new RightFragment());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                // 切换碎片
                replaceFragment(new AnotherRightFragment());
                break;
            default:
                break;
        }
    }

    /**
     * 切换碎片
     */
    private void replaceFragment(Fragment fragment) {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        // 开启事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 向容器中添加或替换碎片
        transaction.replace(R.id.right_layout, fragment);
        //模拟返回栈，会从替换之后的Fragment返回到上一个Fragment
        transaction.addToBackStack(null);
        // 提交事务
        transaction.commit();
    }

    public void click(View view) {
        startActivity(new Intent(this, ViewPapgerActivity.class));
    }
}
