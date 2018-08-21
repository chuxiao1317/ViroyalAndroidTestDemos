package com.chuxiao.sharepreferencestest;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText nameRestore = findViewById(R.id.name_et);
        final EditText ageRestore = findViewById(R.id.age_et);
        final EditText marriedRestore = findViewById(R.id.married_et);
        Button saveDataButton = findViewById(R.id.save_data_btn);
        saveDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameInput = nameRestore.getText().toString();
                String ageInput = ageRestore.getText().toString();
                String marriedInput = marriedRestore.getText().toString();
                SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
//                editor.putString("name", "孙如血");
//                editor.putInt("age", 28);
//                editor.putBoolean("married", false);
                editor.putString("name", nameInput);
                editor.putString("age", ageInput);
                editor.putString("married", marriedInput);
                // 提交数据
                editor.apply();
            }
        });
        Button restoreDataButton = findViewById(R.id.restore_data_btn);
        restoreDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
//                String name = sharedPreferences.getString("name", "");
//                int age = sharedPreferences.getInt("age", 0);
//                boolean married = sharedPreferences.getBoolean("married", false);
//                nameRestore.setText("姓名：" + name);
//                ageRestore.setText("年龄：" + age);
//                marriedRestore.setText("婚否：" + married);
                String nameRecover = sharedPreferences.getString("name", "");
                String ageRecover = sharedPreferences.getString("age", "");
                String marriedRecover = sharedPreferences.getString("married", "");
                nameRestore.setText("姓名：" + nameRecover);
                ageRestore.setText("年龄：" + ageRecover);
                marriedRestore.setText("婚否：" + marriedRecover);
            }
        });
    }
}
