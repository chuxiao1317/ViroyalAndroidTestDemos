package com.chuxiao.providertest;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chuxiao.providertest.database.MyDatabaseHelper;

public class MainActivity extends AppCompatActivity {

    private MyDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //设置准备创建的数据库的参数
        databaseHelper = new MyDatabaseHelper(this, "BookStore.db", null, 2);
        /**
         * 建库
         * */
        Button createDatabaseButton = findViewById(R.id.create_database_btn);
        createDatabaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建或打开数据库
                databaseHelper.getWritableDatabase();
            }
        });
        /**
         * 添加数据
         * */
        Button addDataButton = findViewById(R.id.add_data_btn);
        addDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 打开数据库
                SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                // 开始组装第一条数据
                values.put("name", "达芬奇密码");
                values.put("author", "Dan Brown");
                values.put("pages", 454);
                values.put("price", 16.96);
                // 插入数据
                sqLiteDatabase.insert("Book", null, values);
                values.clear();
                // 组装第二条数据
                values.put("name", "人性的弱点");
                values.put("author", "卡耐基");
                values.put("pages", 600);
                values.put("price", 21.99);
                // 插入数据
                sqLiteDatabase.insert("Book", null, values);
            }
        });
        /**
         * 更改数据
         * */
        Button updateDataButton = findViewById(R.id.update_data_btn);
        updateDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 打开数据库
                SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("price", 10.99);
                // 更改数据
                sqLiteDatabase.update("Book", values, "name = ?", new String[]{"达芬奇密码"});
            }
        });
        /**
         * 删除数据
         * */
        Button deleteDataButton = findViewById(R.id.delete_data_btn);
        deleteDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开数据库
                SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
                // 删除总页数大于500的书籍
                sqLiteDatabase.delete("Book", "pages > ?", new String[]{"500"});
            }
        });
        /**
         * 查询数据
         * */
        Button queryDataButton = findViewById(R.id.query_data_btn);
        queryDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "请在控制台查看打印信息", Toast.LENGTH_SHORT).show();
                SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
                // 查询表中所有数据
                Cursor cursor = sqLiteDatabase.query("Book", null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        // 遍历Cursor对象，取出数据并打印
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));
                        Log.d("Book表中数据", "书名：" + name + "，作者：" + author + "，总页数：" + pages + "，价格：" + price);
                    } while (cursor.moveToNext());
                }
                // 关闭cursor
                cursor.close();
            }
        });
    }
}
