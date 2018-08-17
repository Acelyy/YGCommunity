package com.yonggang.liyangyang.iyonggang;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Process;

import java.io.File;


public class DownLoadRunnable implements Runnable {
    private Context context;
    private String url;
    private Handler handler;
    private int state;
    private String title;

    public DownLoadRunnable(Context context, String url, String title, int state, Handler handler) {
        this.context = context;
        this.url = url;
        this.state = state;
        this.handler = handler;
        this.title = title;
    }

    @Override
    public void run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        startDownload();
    }

    /**
     * 通过DownloadManager开始下载
     *
     * @return
     */
    public long startDownload() {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        long requestId = downloadManager.enqueue(CreateRequest(url, title));
        queryDownloadProgress(requestId, downloadManager);
        return requestId;
    }

    /**
     * 设置Request参数
     *
     * @param url
     * @return
     */
    public DownloadManager.Request CreateRequest(String url, String title) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle(title);// 文件下载时显示的标题
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);// 下载完成后，notification会自动消失
        File file = MyUtils.getCacheFile(MyUtils.APP_NAME,context);
        if (file != null && file.exists()) {
            file.delete();//删除掉存在的apk
        }
        request.setDestinationUri(Uri.fromFile(file));//指定apk缓存路径
        return request;
    }

    /**
     * 查询下载进度
     *
     * @param requestId DownloadManager.COLUMN_TOTAL_SIZE_BYTES  下载文件的大小（总字节数）
     *                  DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR 当前下载文件的字节数
     */
    public void queryDownloadProgress(long requestId, DownloadManager downloadManager) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(requestId);
        try {
            boolean isGoging = true;
            while (isGoging) {
                Cursor cursor = downloadManager.query(query);
                if (cursor != null && cursor.moveToFirst()) {
                    int state = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    switch (state) {
                        case DownloadManager.STATUS_SUCCESSFUL://下载成功
                            isGoging = false;
                            handler.obtainMessage(downloadManager.STATUS_SUCCESSFUL).sendToTarget();//发送到主线程，更新ui
                            break;
                        case DownloadManager.STATUS_FAILED://下载失败
                            isGoging = false;
                            handler.obtainMessage(downloadManager.STATUS_FAILED).sendToTarget();//发送到主线程，更新ui
                            break;

                        case DownloadManager.STATUS_RUNNING://下载中
                            /**
                             * 计算下载下载率；
                             */
                            int totalSize = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                            int currentSize = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                            int progress = (int) (((float) currentSize) / ((float) totalSize) * 100);
                            handler.obtainMessage(downloadManager.STATUS_RUNNING, progress).sendToTarget();//发送到主线程，更新ui
                            break;

                        case DownloadManager.STATUS_PAUSED://下载停止
                            handler.obtainMessage(DownloadManager.STATUS_PAUSED).sendToTarget();
                            break;

                        case DownloadManager.STATUS_PENDING://准备下载
                            handler.obtainMessage(DownloadManager.STATUS_PENDING).sendToTarget();
                            break;
                    }
                }
                if (cursor != null) {
                    cursor.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
