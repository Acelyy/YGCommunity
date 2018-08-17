package com.yonggang.ygcommunity.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yonggang.ygcommunity.Activity.LoginActivity;
import com.yonggang.ygcommunity.Activity.Personal.AnswerActivity;
import com.yonggang.ygcommunity.Activity.Personal.CollectionActivity;
import com.yonggang.ygcommunity.Activity.Personal.MessageActivity;
import com.yonggang.ygcommunity.Activity.Personal.MyActActivity;
import com.yonggang.ygcommunity.Activity.Personal.OptionActivity;
import com.yonggang.ygcommunity.Activity.Personal.PersonalActivity;
import com.yonggang.ygcommunity.Activity.Personal.PublishActivity;
import com.yonggang.ygcommunity.Activity.Personal.ScoreActivity;
import com.yonggang.ygcommunity.Activity.Personal.SettingActivity;
import com.yonggang.ygcommunity.Activity.Personal.SignUpActivity;
import com.yonggang.ygcommunity.Activity.RegisterActivity;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.View.CircleImageView;
import com.yonggang.ygcommunity.YGApplication;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;
import com.yonggang.ygcommunity.httpUtil.ProgressSubscriber;
import com.yonggang.ygcommunity.httpUtil.SubscriberOnNextListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liyangyang on 2017/2/25.
 */

public class MineFragment extends Fragment {

    @BindView(R.id.head_mine_inLogin)
    LinearLayout headMineInLogin;
    @BindView(R.id.head_mine_login)
    RelativeLayout headMineLogin;

    @BindView(R.id.mine_head)
    CircleImageView mineHead;
    @BindView(R.id.mine_name)
    TextView mineName;
    @BindView(R.id.txt_sign)
    TextView txt_sign;

    private YGApplication app;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_mine, container, false);
        ButterKnife.bind(this, view);
        app = (YGApplication) getActivity().getApplication();
        txt_sign.setText("立即签到>>");
        changeLoginStatus(true);
        return view;

    }

    @OnClick({R.id.txt_sign,R.id.mine_head, R.id.button_to_login, R.id.text_to_register, R.id.line_message, R.id.line_love, R.id.line_comment, R.id.line_send, R.id.line_score, R.id.line_activity, R.id.line_option, R.id.line_setting})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.txt_sign:
                goActivity(SignUpActivity.class);
                break;
            case R.id.mine_head:
                if (app.getUser() == null) {
                    Toast.makeText(app, "请先进行登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                check_authorized();
                break;
            case R.id.button_to_login:
                goActivity(LoginActivity.class);
                break;
            case R.id.text_to_register:
                goActivity(RegisterActivity.class);
                break;
            case R.id.line_message:
                if (app.getUser() == null) {
                    Toast.makeText(app, "请先进行登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                goActivity(MessageActivity.class);
                break;
            case R.id.line_love:
                if (app.getUser() == null) {
                    Toast.makeText(app, "请先进行登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                goActivity(CollectionActivity.class);
                break;
            case R.id.line_comment:
                if (app.getUser() == null) {
                    Toast.makeText(app, "请先进行登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                goActivity(AnswerActivity.class);
                break;
            case R.id.line_send:
                if (app.getUser() == null) {
                    Toast.makeText(app, "请先进行登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                goActivity(PublishActivity.class);
                break;
            case R.id.line_score:
                if (app.getUser() == null) {
                    Toast.makeText(app, "请先进行登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                goActivity(ScoreActivity.class);
                break;
            case R.id.line_activity:
                if (app.getUser() == null) {
                    Toast.makeText(app, "请先进行登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                goActivity(MyActActivity.class);
                break;
            case R.id.line_option:
                if (app.getUser() == null) {
                    Toast.makeText(app, "请先进行登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                goActivity(OptionActivity.class);
                break;
            case R.id.line_setting:
                if (app.getUser() == null) {
                    Toast.makeText(app, "请先进行登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivityForResult(intent, 0x123);
                break;
        }
    }

    private void goActivity(Class clz) {
        Intent intent = new Intent(getActivity(), clz);
        startActivity(intent);
    }

    /**
     * 切换登录状态
     *
     * @param is_login
     */
    public void changeLoginStatus(boolean is_login) {
        if (is_login && app.getUser() != null) {
            headMineLogin.setVisibility(View.VISIBLE);
            headMineInLogin.setVisibility(View.GONE);
            mineName.setText(app.getUser().getUsername());
            Glide.with(MineFragment.this)
                    .load(app.getUser().getFace_url())
                    .into(mineHead);
        } else {
            headMineLogin.setVisibility(View.GONE);
            headMineInLogin.setVisibility(View.VISIBLE);
            Glide.with(MineFragment.this)
                    .load(R.mipmap.pic_head)
                    .into(mineHead);
        }
    }

    /**
     * 获取是否实名认证
     */
    private void check_authorized() {
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<Integer>() {
            @Override
            public void onNext(Integer data) {
                Log.i("check_authorized", data.toString());
                Intent intent = new Intent(getActivity(), PersonalActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("is_named", data);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        };
        HttpUtil.getInstance().check_authorized(new ProgressSubscriber<Integer>(onNextListener, getActivity(), "获取信息中..."), app.getUser().getUser_id());
    }
}
