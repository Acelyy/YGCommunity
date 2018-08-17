package com.yonggang.ygcommunity.DataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyangyang on 2017/7/31.
 */

public class DBDao {
    private DBHelper dbHelper;

    public DBDao(Context context) {
        dbHelper = new DBHelper(context);
    }

    /**
     * 根据url获取路径
     *
     * @param url
     * @return
     */
    public String getPicByUrl(String url) {
        String path = "";
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String sql = "select * from myTable where url=?";
        Cursor cursor = database.rawQuery(sql, new String[]{url});
        while (cursor.moveToNext()) {
            path = cursor.getString(2);
        }
        return path;
    }

    /**
     * 保存图片
     *
     * @param url
     * @param path
     */
    public void savePic(String url, String path) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String sql = "insert into myTable (url,path)values(?,?)";
        Object[] bandArgs = {url, path};
        database.execSQL(sql, bandArgs);
    }

    /**
     * 获取数据库的图片
     *
     * @return
     */
    public List<String> getPic() {
        List<String> list_path = new ArrayList<>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String sql = "select * from myTable";
        Cursor cursor = database.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            String path = cursor.getString(2);
            list_path.add(path);
        }
        return list_path;
    }

    /**
     * 删除所有本地订单
     */
    public void clearData() {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String delete_sql = "drop table myTable";
        database.execSQL(delete_sql);
        database.execSQL("create table myTable(_id integer PRiMARY KEY AUTOINCREMENT,url char,path char)");
    }

    /**
     * 关闭数据库
     */
    public void closeDb() {
        dbHelper.close();
    }
}
