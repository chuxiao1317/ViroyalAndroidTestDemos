package com.chuxiao.providertest2;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String newId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addDataBtn = findViewById(R.id.add_data);
        addDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("content://com.chuxiao.providertest.provider/book");
                ContentValues values = new ContentValues();
                values.put("name", "人类简史");
                values.put("author", "赫拉利");
                values.put("pages", 1040);
                values.put("price", 22.85);
                Uri newUri = getContentResolver().insert(uri, values);
                if (newUri != null) {
                    newId = newUri.getPathSegments().get(1);
                }
            }
        });

        Button queryDataBtn = findViewById(R.id.query_data);
        queryDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("content://com.chuxiao.providertest.provider/book");
                Cursor cursor = getContentResolver().query(
                        uri,
                        null,
                        null,
                        null,
                        null
                );
                if (cursor != null) {
                    Toast.makeText(MainActivity.this, "请在AS查看日志", Toast.LENGTH_SHORT).show();
                    while (cursor.moveToNext()) {
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));
                        Log.d("MainActivity", "书名：" + name);
                        Log.d("MainActivity", "作何：" + author);
                        Log.d("MainActivity", "总页数：" + pages);
                        Log.d("MainActivity", "价格：" + price);
                    }
                    cursor.close();
                }
            }
        });

        Button updateDataBtn = findViewById(R.id.update_data);
        updateDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("content://com.chuxiao.providertest.provider/book/" + newId);
                ContentValues values = new ContentValues();
                values.put("name", "我的互联网方法论");
                values.put("author", "周鸿祎");
                values.put("pages", "1216");
                values.put("price", "24.05");
            }
        });

        Button deleteDataBtn = findViewById(R.id.delete_data);
        deleteDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("content://com.chuxiao.providertest.provider/book/" + newId);
                getContentResolver().delete(uri, null, null);
            }
        });
    }
}
