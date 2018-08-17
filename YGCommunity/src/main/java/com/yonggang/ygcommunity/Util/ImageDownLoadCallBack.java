package com.yonggang.ygcommunity.Util;

import android.graphics.Bitmap;

import java.io.File;

/**
 * Created by liyangyang on 2017/7/31.
 */

public interface ImageDownLoadCallBack {
    void onDownLoadSuccess(File file);
    void onDownLoadSuccess(Bitmap bitmap);
    void onDownLoadFailed();
}
