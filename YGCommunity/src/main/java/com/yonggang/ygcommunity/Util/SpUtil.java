package com.yonggang.ygcommunity.Util;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;
import com.yonggang.ygcommunity.Entry.User;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by liyangyang on 2017/5/22.
 */

public class SpUtil {
    private static final String FIRST = "first";
    private static final String NIGHT_SHARED = "night_shared";
    private static final String USER_SHARED = "user_shared";
    private static final String GRID_SHARED = "grid_shared";

    /**
     * 获取是否第一次登陆
     *
     * @param context
     * @return
     */
    public static boolean getFirst(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FIRST, MODE_PRIVATE);
        return sp.getBoolean("first", true);
    }

    /**
     * 设置是否第一次
     *
     * @param context
     * @param is
     */
    public static void setFirst(Context context, boolean is) {
        SharedPreferences sp = context.getSharedPreferences(FIRST, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("first", is);
        editor.apply();
    }

    /**
     * 设置是否是夜间模式
     *
     * @param context
     * @param is_night
     */
    public static void setNightOrDay(Context context, boolean is_night) {
        SharedPreferences sp = context.getSharedPreferences(NIGHT_SHARED, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("is_night", is_night);
        editor.apply();
    }

    /**
     * 查看是否是夜间模式
     *
     * @param context
     * @return
     */
    public static boolean getNightOrDay(Context context) {
        SharedPreferences sp = context.getSharedPreferences(NIGHT_SHARED, MODE_PRIVATE);
        return sp.getBoolean("is_night", false);
    }

    /**
     * 缓存登录信息
     *
     * @param context
     * @param user
     */
    public static void saveUser(Context context, User user) {
        SharedPreferences sp = context.getSharedPreferences(USER_SHARED, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("user", user == null ? "" : JSON.toJSONString(user));
        editor.putLong("time", System.currentTimeMillis());
        editor.apply();
    }

    /**
     * 获取本地登录信息的时效性
     *
     * @param context
     * @param time
     * @return
     */
    public static boolean checkTime(Context context, long time) {
        SharedPreferences sp = context.getSharedPreferences(USER_SHARED, MODE_PRIVATE);
        long oldTime = sp.getLong("time", 0);
        return oldTime != 0 && time - oldTime < 7 * 24 * 60 * 60 * 1000;

    }

    /**
     * 获取登录信息
     *
     * @param context
     * @return
     */
    public static User getUser(Context context) {
        SharedPreferences sp = context.getSharedPreferences(USER_SHARED, MODE_PRIVATE);
        String user_string = sp.getString("user", null);
        if (user_string == null) {
            return null;
        } else {
            return JSON.parseObject(user_string, User.class);
        }
    }

    /**
     * 保存网格化账号密码
     *
     * @param context
     * @param user
     * @param pass
     */
    public static void saveGrid(Context context, String user, String pass, boolean is_remember) {
        SharedPreferences sp = context.getSharedPreferences(GRID_SHARED, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("name", user);
        editor.putString("pass", pass);
        editor.putBoolean("is_remember", is_remember);
        editor.apply();
    }

    /**
     * 读取网格化账号密码
     *
     * @param context
     * @return
     */
    public static Map<String, Object> getGrid(Context context) {
        Map<String, Object> result = new HashMap<>();
        SharedPreferences sp = context.getSharedPreferences(GRID_SHARED, MODE_PRIVATE);
        result.put("name", sp.getString("name", ""));
        result.put("pass", sp.getString("pass", ""));
        result.put("is_remember", sp.getBoolean("is_remember", false));
        return result;
    }

}
