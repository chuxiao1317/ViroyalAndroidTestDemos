package com.chuxiao.coolweather3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.chuxiao.coolweather3.network.Api;
import com.viroyal.sloth.util.Slog;

import org.kymjs.kjframe.KJDB;
import org.litepal.LitePalApplication;

public class App extends LitePalApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        boolean isLog = true;
//        KJDB.create(
//                this,
//                "cool_weather.db",
//                isLog,
//                1,
//                new KJDB.DbUpdateListener() {
//                    @Override
//                    public void onCreate(Context context, SQLiteDatabase sqLiteDatabase) {
//
//                    }
//
//                    @Override
//                    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
//                        Slog.d(KJDB.DbUpdateListener.class.getSimpleName(), "onUpgrade, old=" + oldVersion + ", new=" + newVersion);
//                    }
//                }
//        );
        Api.getInstance().init(this);
    }
}
