package com.yonggang.liyangyang.iyonggang;

import android.content.Context;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import java.io.File;


public class MyUtils {

    public static final String APP_NAME="iyonggang.apk";//名字

    /**
     * 获得存储文件
     *
     * @param
     * @param
     * @return
     */
    public static File getCacheFile(String name,Context context) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        Log.i("path",cachePath);
        return new File(cachePath + File.separator + name);
    }

    /**
     * 获取手机大小，px
     * @param context
     * @return
     */
    public static DisplayMetrics getPhoneMetrics(Context context) {// 获取手机分辨率
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(dm);
        return dm;
    }
}
