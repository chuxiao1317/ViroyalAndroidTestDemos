package com.chuxiao.sqlitetest.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by 12525 on 2018/4/27.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String CREAT_BOOK_TABLE = "create table Book ("
            + "id integer primary key autoincrement, "
            + "author text, "
            + "price real, "
            + "pages integer, "
            + "name text)";

    public static final String CREAT_CATEGORY_TABLE = "create table Category ("
            + "id integer primary key autoincrement, "
            + "category_name text, "
            + "category_code integer)";

    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    /**
     * 如果数据库已存在，则不会进入此方法
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 执行sql语句建表
        db.execSQL(CREAT_BOOK_TABLE);
        db.execSQL(CREAT_CATEGORY_TABLE);
        Toast.makeText(mContext, "建库成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 删除已经存在的表
        db.execSQL("drop table if exists Book");
        db.execSQL("drop table if exists Category");
        // 重新建表
        onCreate(db);
    }
}
