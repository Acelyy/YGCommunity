package com.yonggang.ygcommunity.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by liyangyang on 2017/7/31.
 */

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "app.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table myTable(_id integer PRiMARY KEY AUTOINCREMENT,url char,path char)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
