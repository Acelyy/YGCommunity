package com.yonggang.ygcommunity.wxapi;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.Entry.WXPayResponse;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;
import com.yonggang.ygcommunity.httpUtil.ProgressSubscriber;
import com.yonggang.ygcommunity.httpUtil.SubscriberOnNextListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;


public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";
    @BindView(R.id.pic_status)
    ImageView picStatus;
    @BindView(R.id.pic_text)
    TextView picText;

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat);
        ButterKnife.bind(this);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.registerApp(Constants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent();
        intent.setAction("payFinish");
        sendBroadcast(intent);
        finish();
        super.onDestroy();
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
        String extra = ((PayResp) resp).extData;
        Log.i("PayResp", extra);
        WXPayResponse res = JSON.parseObject(extra, WXPayResponse.class);

        switch (resp.errCode) {
            case 0:
//                sendMid(id);
//                Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
                queryOrder(res.getOrder_no(), res.getId());
                break;
            case -1:
                Toast.makeText(this, "支付失败", Toast.LENGTH_SHORT).show();
                break;
            case -2:
                Toast.makeText(this, "取消支付", Toast.LENGTH_SHORT).show();
                break;
        }

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

    /**
     * 查询订单支付状态
     *
     * @param id
     */
    private void queryOrder(final String orderId, final String id) {
        Subscriber subscriber = new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean data) {
                if (data) {
                    picStatus.setImageResource(R.mipmap.pic_pay_success);
                    picStatus.setOnClickListener(null);
                    picText.setText("支付成功！");
                } else {
                    picStatus.setImageResource(R.mipmap.pic_pay_fail);
                    picStatus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            queryOrder(orderId, id);
                        }
                    });
                    picText.setText("支付失败，请点击图标重新尝试！");
                }
            }
        };
        HttpUtil.getInstance().queryOrder(subscriber, orderId, id);
    }

    @OnClick(R.id.img_finish)
    public void onViewClicked() {
        finish();
    }
}