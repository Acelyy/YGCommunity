package com.alipay.sdk.pay.demo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.pay.demo.util.OrderInfoUtil2_0;

import java.util.Map;

/**
 *  重要说明:
 *  
 *  这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
 *  真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
 *  防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险； 
 */
public class PayDemoActivity extends FragmentActivity {
	
	/** 支付宝支付业务：入参app_id */
	public static final String APPID = "2017072807929448";

	/** 商户私钥，pkcs8格式 */
	/** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
	/** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
	/** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
	/** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
	/** 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1 */
	public static final String RSA2_PRIVATE = "MIIEowIBAAKCAQEAuyQLFDFfMSCEaXw8OLPqwy0xbA6X4KB1ZoEPDJvLK37zSFwOkr/C/o4DaOhhBQD/CUKq/gEJkwx+j6+daN+dvN8s0zu4PYwhB82aeG6uHq1BqbGgNLL6auIF3/lOG1kFCAq8HTXQCNzlw/fknDRGTec/X2M75tHKtM5qwVbzzKdBXapLUXB7cFksxGMTgPSUY/3kbOvHPZkBtcYU3OS4gHpbhYelDtETRwIx+izi3UvoomfvBftfWJyagPpHuJGMwd6RfjSM2OKANEy7EpwB3/G0joKv9FZXCTUEGLu6xVz8mcKMlVnxVWR5qPTdUFWAiYS0nYRUy8yFnXKfDBpELQIDAQABAoIBAB8P5n7diw1edlwD8QMAahEnaTKkYXU5bQMsnNDc8yYvFjaGgSy+g6E49TvuhlwMREjHNhafGXW89yzmW1xS8Z2t9YTCPtyIa9sdt2wSAvi4jVObVhopMwOWrJ72hI5pgjpJfdvjWLp/3+tdNCbxTlIT9iLhF8pCPEEymempgyIOwE2MAb8fjrIWcLtradEa9yzWi3l+F78ADfJuXWhAYh72rY6WJZhfcJW9GXOBCpQL5ZGYPKMzP30fazmE/cSZ1/Dr8L0k4asvK1xAc3rFZv1RO8Ga5Go6autOOiNFKavOCt4Gu9DgbiInXCP5rzTG5UPzjNvGfuJBgn8Jp1cpBkkCgYEA4Hs37+hRt7WWm72ErT1kbHIjBmBrhr41iJ6rLHVdUgIZrvIkhf/HDjJnkQ7369xrDn8UlLCJ02CDURmadkxVHcWEdFSFkUTCIrZCotLsLyqG4u3QoRyLqlLi07xRoKIX1COebpOZlR/ymptK5zH5vwBcrmAP2z4xzVIkyOYdNp8CgYEA1WqnFtqRtSWfn4bjjDwzwYfRimi3Ysi5cWgl4NvuhDVnfJWPLBWOoin65pB/DBBmQBkd1Okk240v6wLtXeG5wu+lhiUUcOqR1QKiCOxPPwqCkAsBddjm11CUzW/eqoScyCjpqcJkjgobiumROSG+sNqcZzcFnYrFZHQ/zS+5DbMCgYB03BBppvJrhW+rEcTw/qW5ws0y85zmXzy1K4yGr25uU8OVDF8xFs9EN78sh7NOqMdem49WPHTzgim6XW7IqMIipoFmod2Km1XJBkK4Sr9DaosftZfHV3npqA6HFWEVE2z+NbCzdckdFa2RCMi8hIq0APc+m6mFg+IfNeUKZKjOlwKBgEb0T5s0je8ZFCn4sdylA1wbqNoImLf6HlWLF9TbqkBAdSihnNk+TY6ywF05u1OqfCRhdV6dKQjptv2+mmZ1LsqV3IVt/I7SHiwg1Ph8gJhoCSKU1iZ1N8JsYxyuTeyJbIrLt2weWkozKBcWMpc5Feo8tHVIgNBxG9GbKL8yV6crAoGBAJyENGaLXvXsFD7gbohLjSb8gSATzu5eS6PaXn0FAfMfnM9jhhDDpNT9e20AsMLXkytN7own/gJq7dwc07ZAX2es5Z7b4tPLkUdfHhDra+NbkfZh7FdYmRQ5a1McAYvA9MXZLCmf+F8LIkQNZes3i5nqbsUDRPY3U82NA5uqkJAn";
	public static final String RSA_PRIVATE = "";
	
	private static final int SDK_PAY_FLAG = 1;

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
					Toast.makeText(PayDemoActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
				} else {
					// 该笔订单真实的支付结果，需要依赖服务端的异步通知。
					Toast.makeText(PayDemoActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
				}
				break;
			}
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_main);
	}
	
	/**
	 * 支付宝支付业务
	 * 
	 * @param v
	 */
	public void payV2(View v) {
		if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
			new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialoginterface, int i) {
							//
							finish();
						}
					}).show();
			return;
		}
	
		/**
		 * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
		 * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
		 * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险； 
		 * 
		 * orderInfo的获取必须来自服务端；
		 */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
		Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2);
		String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

		String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
		String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
		final String orderInfo = orderParam + "&" + sign;
		
		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				PayTask alipay = new PayTask(PayDemoActivity.this);
				Map<String, String> result = alipay.payV2(orderInfo, true);
				Log.i("msp", result.toString());
				
				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

}
