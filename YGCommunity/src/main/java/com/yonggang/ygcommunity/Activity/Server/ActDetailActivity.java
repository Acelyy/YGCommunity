package com.yonggang.ygcommunity.Activity.Server;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.Entry.ActDetail;
import com.yonggang.ygcommunity.Entry.User;
import com.yonggang.ygcommunity.PhotoPicker.RecyclerItemClickListener;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.View.CircleImageView;
import com.yonggang.ygcommunity.YGApplication;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

public class ActDetailActivity extends BaseActivity {
    @BindView(R.id.imag_piv)
    ImageView imagPiv;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.content)
    WebView content;
    @BindView(R.id.location)
    TextView location;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.type)
    TextView type;
    @BindView(R.id.btn_baoming)
    Button btnBaoming;
    @BindView(R.id.tel)
    TextView tel;
    @BindView(R.id.img_call)
    ImageView imgCall;
    @BindView(R.id.sign)
    TextView sign;
    @BindView(R.id.layout_signed)
    LinearLayout layoutSigned;
    @BindView(R.id.list_signed)
    RecyclerView listSigned;
    @BindView(R.id.img_shared)
    ImageView imgShared;

    private String id;

    private YGApplication app;

    private int account_sign = 0;//是否已经报名

    SignedAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_detail);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();
        id = getIntent().getExtras().getString("id");
        WebSettings webSettings = content.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        content.setWebChromeClient(new WebChromeClient() {
            /**
             * 处理JavaScript Alert事件
             */
            @Override
            public boolean onJsAlert(WebView view, String url,
                                     String message, final JsResult result) {
                //用Android组件替换
                new AlertDialog.Builder(ActDetailActivity.this)
                        .setTitle("提示")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        })
                        .setCancelable(false)
                        .create().show();
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getActDetail(id);
    }

    @Override
    protected void onDestroy() {
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().startSync();
        CookieManager.getInstance().removeSessionCookie();
        content.clearCache(true);
        content.clearHistory();
        super.onDestroy();
    }

    /**
     * 获取活动详情
     *
     * @param id
     */
    private void getActDetail(final String id) {
        Subscriber subscriber = new Subscriber<ActDetail>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final ActDetail data) {
                Log.i("ActDetail", data.toString());
                account_sign = data.getAccount_sign();
                title.setText(data.getTitle());
                location.setText(data.getAddress());
                content.loadUrl(data.getContent());
                time.setText(data.getDate_start() + "-" + data.getDate_end());
                sign.setText("名额" + data.getMax_join() + "人|已报名" + data.getJoin_no() + "人|已入选" + data.getChecked() + "人");
                type.setText(data.getType_id());
                tel.setText(data.getConnect_name() + "-" + data.getConnect_fs());
                imgCall.setVisibility(View.VISIBLE);
                imgCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ActDetailActivity.this);
                        builder.setTitle("呼叫" + data.getConnect_fs() + "？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        call(data.getConnect_fs());
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .create().show();
                    }
                });
                btnBaoming.setText(account_sign == 0 ? "立即报名" : "继续报名");
                Glide.with(app)
                        .load(data.getImages())
                        .error(R.mipmap.pic_loading_error)
                        .centerCrop()
                        .into(imagPiv);
                List<User> userinfo = data.getUserinfo();
                if (userinfo != null && !userinfo.isEmpty()) {
                    LinearLayoutManager lm = new LinearLayoutManager(ActDetailActivity.this);
                    lm.setOrientation(LinearLayoutManager.HORIZONTAL);
                    listSigned.setLayoutManager(lm);
                    adapter = new SignedAdapter(userinfo);
                    listSigned.setAdapter(adapter);
                    listSigned.addOnItemTouchListener(new RecyclerItemClickListener(ActDetailActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            if (adapter.getItemViewType(position) == SignedAdapter.TYPE_MORE) {
                                Bundle bundle = new Bundle();
                                bundle.putString("id", id);
                                stepActivity(bundle, SignedPersonActivity.class);
                            }
                        }
                    }));
                } else {
                    layoutSigned.setVisibility(View.GONE);
                }
                imgShared.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity_shared(data);
                    }
                });

            }
        };
        HttpUtil.getInstance().getActDetail(subscriber, app.getUser() == null ? "" : app.getUser().getUser_id(), id);
    }

    private void call(String number) {
        //用intent启动拨打电话
        Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + number));
        startActivity(intent);
    }

    @OnClick({R.id.img_finish, R.id.btn_baoming})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_finish:
                finish();
                break;
            case R.id.btn_baoming:
                sign();
                break;

        }
    }

    /**
     * 活动页面分享
     */
    private void activity_shared(ActDetail actDetail) {
        UMWeb web = new UMWeb(actDetail.getHdfx());
        String title = actDetail.getTitle();
        web.setTitle(title);//标题
        web.setThumb(new UMImage(this, actDetail.getImages()));  //缩略图
        web.setDescription(title);//描述
        new ShareAction(ActDetailActivity.this)
                .withMedia(web)
                .setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE, SHARE_MEDIA.SINA)
                .setCallback(shareListener)
                .open();
    }

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(ActDetailActivity.this, "分享成功", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(ActDetailActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(ActDetailActivity.this, "取消分享", Toast.LENGTH_LONG).show();

        }
    };

    /**
     *
     */
    private void sign() {
        if (app.getUser() == null) {
            Toast.makeText(app, "请先登录再进行报名", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (account_sign == 0) {
                Intent intent = new Intent(this, ContactsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, SignedActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    }

    /**
     *
     */
    class SignedAdapter extends RecyclerView.Adapter<SignedAdapter.ViewHolder> {

        private List<User> data;
        public final static int TYPE_ITEM = 1;
        public final static int TYPE_MORE = 2;

        public SignedAdapter(List<User> data) {
            this.data = data;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = null;
            switch (viewType) {
                case TYPE_ITEM:
                    itemView = LayoutInflater.from(ActDetailActivity.this).inflate(R.layout.item_signed, null);
                    break;
                case TYPE_MORE:
                    itemView = LayoutInflater.from(ActDetailActivity.this).inflate(R.layout.item_signed_more, null);
                    break;
            }
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (getItemViewType(position) == TYPE_ITEM) {
                holder.name.setText(data.get(position).getUsername());
                Glide.with(getApplicationContext())
                        .load(data.get(position).getFace_url())
                        .into(holder.head);
            }
        }

        @Override
        public int getItemCount() {
            return data.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            return position == data.size() ? TYPE_MORE : TYPE_ITEM;
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private CircleImageView head;
            private TextView name;

            public ViewHolder(View itemView) {
                super(itemView);
                head = (CircleImageView) itemView.findViewById(R.id.head);
                name = (TextView) itemView.findViewById(R.id.name);
            }
        }
    }

}
