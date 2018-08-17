package com.yonggang.ygcommunity.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yonggang.ygcommunity.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class WeChatPay extends AppCompatActivity {

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_we_chat_pay);
        ButterKnife.bind(this);
        api = WXAPIFactory.createWXAPI(this, "wxd930ea5d5a258f4f");
    }

    @OnClick(R.id.pay)
    public void onClick() {
        pay();
    }

    private void pay() {
        PayReq request = new PayReq();

        request.appId = "wxd930ea5d5a258f4f";

        request.partnerId = "1900000109";

        request.prepayId = "1101000000140415649af9fc314aa427";

        request.packageValue = "Sign=WXPay";

        request.nonceStr = "1101000000140429eb40476f8896f4c9";

        request.timeStamp = "1398746574";

        request.sign = "7FFECB600D7157C5AA49810D2D8F28BC2811827B";

        api.sendReq(request);


    }
}
