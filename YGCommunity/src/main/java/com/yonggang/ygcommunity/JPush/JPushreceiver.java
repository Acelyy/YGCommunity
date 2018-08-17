package com.yonggang.ygcommunity.JPush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.yonggang.ygcommunity.Activity.MainActivity;
import com.yonggang.ygcommunity.Entry.Bbs;
import com.yonggang.ygcommunity.Entry.JPush_Entry;
import com.yonggang.ygcommunity.Entry.JPush_news;
import com.yonggang.ygcommunity.Entry.NewsItem;
import com.yonggang.ygcommunity.Entry.Notice;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by liyangyang on 2017/2/13.
 */

public class JPushreceiver extends BroadcastReceiver {
    private final String TAG = "JPushreceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "onReceive - " + intent.getAction());
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "收到了自定义消息。消息内容是：" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
            Intent intent_exit = new Intent(context, MainActivity.class);
            Bundle bundle_exit = new Bundle();
            bundle_exit.putString("JPush", "exit");
            intent_exit.putExtras(bundle_exit);
            intent_exit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent_exit);
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "收到了通知");
            // 在这里可以做些统计，或者做些其他工作
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "用户点击打开了通知");
            // 在这里可以自己写代码去定义用户点击后的行为
//            Intent i = new Intent(context, MainActivity.class);  //自定义打开的界面
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(i);
            String string = "Bundle{";
            for (String key : bundle.keySet()) {
                string += " " + key + " => " + bundle.get(key) + ";";
            }
            string += " }Bundle";
            Log.i(TAG, string);
            //获取Extra消息
            JPush_Entry entry = JSON.parseObject(bundle.getString("cn.jpush.android.EXTRA"), JPush_Entry.class);
            switch (entry.getNmsg()) {
                case 1:// 新闻
                    //判断类型，1为新闻，
                    Intent intent_news = new Intent(context, MainActivity.class);
                    Bundle bundle_news = new Bundle();
                    JPush_news news = JSON.parseObject(entry.getData(), JPush_news.class);
                    if (news.getCategory_type() == 0) { //新闻
                        bundle_news.putString("JPush", "news");
                    } else if (news.getCategory_type() == 1) { //图片
                        bundle_news.putString("JPush", "pic");
                    } else if (news.getCategory_type() == 2) { //视频
                        bundle_news.putString("JPush", "video");
                    }
                    bundle_news.putSerializable("newsItem", JSON.parseObject(entry.getData(), NewsItem.NewsBean.class));
                    intent_news.putExtras(bundle_news);
                    intent_news.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent_news);
                    break;
                case 2:// 社区活动
                    Intent intent_activity = new Intent(context, MainActivity.class);
                    Bundle bundle_activity = new Bundle();
                    if (entry.getType() == 1) {
                        bundle_activity.putString("JPush", "list");
                    } else if (entry.getType() == 0) {
                        bundle_activity.putString("JPush", "my");
                    }
                    intent_activity.putExtras(bundle_activity);
                    intent_activity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent_activity);
                    break;
                case 3:// 公告
                    Intent intent_notice = new Intent(context, MainActivity.class);
                    Bundle bundle_notice = new Bundle();
                    Notice.NoticeBean notice = JSON.parseObject(entry.getData(), Notice.NoticeBean.class);
                    bundle_notice.putString("JPush", "notice");
                    bundle_notice.putSerializable("notice", notice);
                    intent_notice.putExtras(bundle_notice);
                    intent_notice.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent_notice);
                    break;
                case 4:// 网上居委会
                    Intent intent_bbs = new Intent(context, MainActivity.class);
                    Bundle bundle_bbs = new Bundle();
                    Bbs.BbsBean bean = JSON.parseObject(entry.getData(), Bbs.BbsBean.class);
                    bundle_bbs.putString("JPush", "bbs");
                    bundle_bbs.putSerializable("bbs", bean);
                    intent_bbs.putExtras(bundle_bbs);
                    intent_bbs.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent_bbs);
            }
        } else {
            Log.d(TAG, "Unhandled intent - " + intent.getAction());
        }
    }
}
