package com.yonggang.liyangyang.iyonggang.HttpUtils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by liyangyang on 2017/7/10.
 */

public class SPUtil {
    /**
     * 设置地点
     *
     * @param place_id
     * @param context
     */
    public static void setPlace(String place_id, Context context) {
        SharedPreferences sp = context.getSharedPreferences("place", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("place_id", place_id);
        editor.commit();
    }

    /**
     * 获取地点信息
     *
     * @param context
     * @return
     */
    public static String getPlace(Context context) {
        SharedPreferences sp = context.getSharedPreferences("place", Context.MODE_PRIVATE);
        String place_id = sp.getString("place_id", "");
        return place_id;
    }
}
