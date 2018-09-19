package com.yonggang.ygcommunity.Activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.Entry.Alipay;
import com.yonggang.ygcommunity.Entry.Expense;
import com.yonggang.ygcommunity.Entry.WXPayResponse;
import com.yonggang.ygcommunity.Entry.WechatPay;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.View.RadioGroup;
import com.yonggang.ygcommunity.YGApplication;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;
import com.yonggang.ygcommunity.httpUtil.ProgressSubscriber;
import com.yonggang.ygcommunity.httpUtil.SubscriberOnNextListener;
import com.yonggang.ygcommunity.wxapi.Constants;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayActivity extends BaseActivity {
    private static final int SDK_PAY_FLAG = 1;

    @BindView(R.id.img_type)
    ImageView imgType;
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.txt_sum)
    TextView txtSum;
    @BindView(R.id.pay_type)
    RadioGroup payType;

    private YGApplication app;

    private IWXAPI api;

    private Expense bean;

    private int pay_type = -1;

    private String id;// 欠费记录的id

    private PayFinish pay;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
//                        sendMid();
                        Toast.makeText(PayActivity.this, "支付成功,+" + msg.getData().getString("score") + "积分", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(PayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();

        pay = new PayFinish();
        IntentFilter filter = new IntentFilter();
        filter.addAction("payFinish");
        registerReceiver(pay, filter);

        id = getIntent().getExtras().getString("id");
        bean = (Expense) getIntent().getExtras().getSerializable("account");
        //初始化界面
        txtName.setText(bean.getTab_name());
        txtSum.setText(getIntent().getExtras().getString("sum"));
        switch (bean.getType()) {
            case 0:
                imgType.setImageResource(R.mipmap.server_water);
                break;
            case 1:
                imgType.setImageResource(R.mipmap.server_electric);
                break;
        }
        payType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                pay_type = checkedId;
                Log.i("checkedId", checkedId + "");
            }
        });

        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, true);
        api.registerApp(Constants.APP_ID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(pay);
    }

    @OnClick({R.id.img_finish, R.id.btn_complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_finish:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("支付尚未完成，是否返回？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .create().show();
                break;
            case R.id.btn_complete:
                switch (pay_type) {
                    case R.id.option_alipay:
                        alipay();
                        break;
                    case R.id.option_wechat:
                        wxpay();
                        break;
                    default:
                        Toast.makeText(app, "请至少选择一种支付方式", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
        }
    }

    /**
     * 支付宝支付
     */
    private void alipay() {
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<Alipay>() {
            @Override
            public void onNext(final Alipay data) {
                Log.i("alipay", data.toString());

                Runnable payRunnable = new Runnable() {

                    @Override
                    public void run() {
                        PayTask alipay = new PayTask(PayActivity.this);
                        Map<String, String> result = alipay.payV2(data.getResponse(), true);
                        Log.i("msp", result.toString());
                        Message msg = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putString("score", data.getScore());
                        msg.what = SDK_PAY_FLAG;
                        msg.obj = result;
                        msg.setData(bundle);
                        mHandler.sendMessage(msg);
                    }
                };
                Thread payThread = new Thread(payRunnable);
                payThread.start();
            }
        };
        HttpUtil.getInstance().alipay(new ProgressSubscriber<String>(onNextListener, this, "支付中"), app.getUser().getUser_id(), bean.getId(), bean.getType());
    }

    /**
     * 微信支付
     */
    private void wxpay() {
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<WechatPay>() {
            @Override
            public void onNext(WechatPay data) {
                Log.i("wxpay", JSON.toJSONString(data));

                WXPayResponse res = new WXPayResponse();
                res.setId(id);
                res.setOrder_no(data.getOut_trade_no());

                PayReq req = new PayReq();

                req.extData = JSON.toJSONString(res);// optional

                req.appId = data.getResponse().getAppid();

                req.partnerId = data.getResponse().getPartnerid();

                req.prepayId = data.getResponse().getPrepayid();

                req.packageValue = data.getResponse().getPackageValue();

                req.nonceStr = data.getResponse().getNonce_str();

                req.timeStamp = data.getResponse().getTimestamp();

                req.sign = data.getResponse().getSign();

                api.sendReq(req);
            }
        };
        HttpUtil.getInstance().wxpay(new ProgressSubscriber<WechatPay>(onNextListener, this, "支付中"), app.getUser().getUser_id(), bean.getId(), bean.getType());
    }

    /**
     *
     */
    private void sendMid() {
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String data) {
                Log.i("sendMid", data);
            }
        };
        HttpUtil.getInstance().sendMid(new ProgressSubscriber<String>(onNextListener, this), id);
    }

    /**
     * 支付宝支付返回值类
     */
    private class PayResult {
        private String resultStatus;
        private String result;
        private String memo;

        public PayResult(Map<String, String> rawResult) {
            if (rawResult == null) {
                return;
            }

            for (String key : rawResult.keySet()) {
                if (TextUtils.equals(key, "resultStatus")) {
                    resultStatus = rawResult.get(key);
                } else if (TextUtils.equals(key, "result")) {
                    result = rawResult.get(key);
                } else if (TextUtils.equals(key, "memo")) {
                    memo = rawResult.get(key);
                }
            }
        }

        @Override
        public String toString() {
            return JSON.toJSONString(this);
        }

        /**
         * @return the resultStatus
         */
        public String getResultStatus() {
            return resultStatus;
        }

        /**
         * @return the memo
         */
        public String getMemo() {
            return memo;
        }

        /**
         * @return the result
         */
        public String getResult() {
            return result;
        }
    }

    class PayFinish extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    }
}
