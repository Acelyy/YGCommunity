package com.yonggang.liyangyang.iyonggang.HttpUtils;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import rx.Subscriber;

/**
 * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理
 * Created by liyangyang on 17/4/10.
 */
public class ProgressSubscriber<T> extends Subscriber<T> implements ProgressCancelListener {

    private SubscriberOnNextListener mSubscriberOnNextListener;
    private ProgressDialogHandler mProgressDialogHandler;

    private Context context;
    private boolean is_show;
    private String txt_msg;

    public ProgressSubscriber(SubscriberOnNextListener mSubscriberOnNextListener,
                              Context context) {
        this.mSubscriberOnNextListener = mSubscriberOnNextListener;
        this.context = context;
        this.is_show = false;
        mProgressDialogHandler = new ProgressDialogHandler(context, this, true);
    }

    public ProgressSubscriber(SubscriberOnNextListener mSubscriberOnNextListener,
                              Context context,
                              String txt_msg) {
        this.mSubscriberOnNextListener = mSubscriberOnNextListener;
        this.context = context;
        this.is_show = true;
        this.txt_msg = txt_msg;
        mProgressDialogHandler = new ProgressDialogHandler(context, this, true);
    }

    private void showProgressDialog() {
        if (mProgressDialogHandler != null) {
            Message msg = mProgressDialogHandler.obtainMessage();
            msg.what = ProgressDialogHandler.SHOW_PROGRESS_DIALOG;
            Bundle bundle = new Bundle();
            bundle.putString("msg", txt_msg);
            msg.setData(bundle);
            msg.sendToTarget();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            mProgressDialogHandler = null;
        }
    }

    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onStart() {
        if (is_show) {
            showProgressDialog();
        }
    }

    /**
     * 完成，隐藏ProgressDialog
     */
    @Override
    public void onCompleted() {
        if (is_show) {
            dismissProgressDialog();
        }
    }

    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     *
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        if (e instanceof SocketTimeoutException) {
            Toast.makeText(context, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else if (e instanceof ConnectException) {
            Toast.makeText(context, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        Log.e("error", e.toString());
        dismissProgressDialog();

    }

    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param t 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(T t) {
        if (mSubscriberOnNextListener != null) {
            mSubscriberOnNextListener.onNext(t);
        }
    }

    /**
     * 取消ProgressDialog的时候，取消对observable的订阅，同时也取消了http请求
     */
    @Override
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }
}