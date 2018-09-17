package com.yonggang.ygcommunity.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;
import com.yonggang.ygcommunity.httpUtil.ProgressSubscriber;
import com.yonggang.ygcommunity.httpUtil.SubscriberOnNextListener;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.registerApp(Constants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i(TAG, "get");
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp resp) {
        Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
//        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setMessage(String.valueOf(resp.errCode));
//            builder.show();
//        }
        String id = ((PayResp) resp).extData;
        Log.i(TAG, id);

        switch (resp.errCode) {
            case 0:
                sendMid(id);
                Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
                break;
            case -1:
                Toast.makeText(this, "支付失败", Toast.LENGTH_SHORT).show();
                break;
            case -2:
                Toast.makeText(this, "取消支付", Toast.LENGTH_SHORT).show();
                break;
        }
        Intent intent = new Intent();
        intent.setAction("payFinish");
        sendBroadcast(intent);
        finish();
    }

    /**
     *
     */
    private void sendMid(String id) {
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String data) {
                Log.i("sendMid", data);
            }
        };
        HttpUtil.getInstance().sendMid(new ProgressSubscriber<String>(onNextListener, this), id);
    }
}