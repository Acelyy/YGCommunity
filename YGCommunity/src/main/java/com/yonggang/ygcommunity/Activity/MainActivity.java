package com.yonggang.ygcommunity.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yonggang.ygcommunity.Activity.Personal.MyActActivity;
import com.yonggang.ygcommunity.Activity.Server.ComActActivity;
import com.yonggang.ygcommunity.Activity.Server.NoticeDetailActivity;
import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.Entry.Bbs;
import com.yonggang.ygcommunity.Entry.NewsItem;
import com.yonggang.ygcommunity.Entry.Notice;
import com.yonggang.ygcommunity.Entry.Version;
import com.yonggang.ygcommunity.Fragment.AssemblyFragment;
import com.yonggang.ygcommunity.Fragment.MainFragment;
import com.yonggang.ygcommunity.Fragment.MineFragment;
import com.yonggang.ygcommunity.Fragment.ServerFragment;
import com.yonggang.ygcommunity.Permission.PermissionsActivity;
import com.yonggang.ygcommunity.Permission.PermissionsChecker;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.Util.DownLoadRunnable;
import com.yonggang.ygcommunity.Util.MyUtils;
import com.yonggang.ygcommunity.Util.SpUtil;
import com.yonggang.ygcommunity.YGApplication;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;

import java.io.File;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import rx.Subscriber;

public class MainActivity extends BaseActivity {
    @BindViews({R.id.pic_main, R.id.pic_server, R.id.pic_assembly, R.id.pic_mime})
    public List<ImageView> buttons;

    @BindViews({R.id.text_main, R.id.text_server, R.id.text_assembly, R.id.text_mine})
    public List<TextView> textViews;

    @BindColor(R.color.colorMainClick)
    public int colorMainClick;
    @BindColor(R.color.colorMainUnclick)
    public int colorMainUnclick;

    private Fragment[] fragments;

    private int currentItem = 0;

    private YGApplication app;

    private PermissionsChecker mPermissionsChecker; // 权限检测器

    //handler更新ui
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case DownloadManager.STATUS_SUCCESSFUL:
                    Toast.makeText(MainActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
                    install(MainActivity.this);
                    break;
                case DownloadManager.STATUS_RUNNING:
                    break;
                case DownloadManager.STATUS_FAILED:
                    break;
                case DownloadManager.STATUS_PENDING:
                    break;
            }
            return false;
        }
    });

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CAMERA
    };

    private static final int REQUEST_CODE = 0; // 请求码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        fragments = new Fragment[]{new MainFragment(), new ServerFragment(), new AssemblyFragment(), new MineFragment()};
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_content, fragments[0])
                .add(R.id.main_content, fragments[1])
                .add(R.id.main_content, fragments[2])
                .add(R.id.main_content, fragments[3])
                .hide(fragments[1]).hide(fragments[2])
                .hide(fragments[3])
                .show(fragments[0])
                .commit();
        app = (YGApplication) getApplication();
        buttons.get(0).setSelected(true);
        textViews.get(0).setTextColor(colorMainClick);
        mPermissionsChecker = new PermissionsChecker(this);
        getVersion();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();
        Log.i("screen123", screenWidth + "-------" + screenHeight);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getBoolean("adv", false)) {
                String url = bundle.getString("url");
                Bundle bundle1 = new Bundle();
                bundle1.putString("url", url);
                stepActivity(bundle1, AdvertismentActivity.class);
            }
        }
        SpUtil.setFirst(this, false);
        SpUtil.saveUser(this, app.getUser());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        }
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        }
        if (resultCode == 0x321) {
            ((MineFragment) fragments[3]).changeLoginStatus(false);
        }
    }

    private void getVersion() {
        Subscriber subscriber = new Subscriber<Version>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("version_error", e.toString());
            }

            @Override
            public void onNext(final Version data) {
                Log.i("version", data.toString());
                String version_new = data.getVersion();
                String version_local = GetVersion(MainActivity.this);
                if ("".equals(version_new) || "".equals(version_local)) {
                    return;
                }
                if (Double.parseDouble(version_new) > Double.parseDouble(version_local)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("有新版本，是否更新")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String url = data.getFile();
                                    //String url = "http://gdown.baidu.com/data/wisegame/fd84b7f6746f0b18/baiduyinyue_4802.apk";
                                    new Thread(new DownLoadRunnable(MainActivity.this, url, "永联一点通", 0, handler)).start();
                                }
                            }).setNegativeButton("取消", null)
                            .create().show();
                }
            }
        };
        HttpUtil.getInstance().get_version(subscriber);
    }


    /**
     * 切换Fragment的方法
     *
     * @param index 页码
     */
    private void changeFragment(int index) {
        if (currentItem != index) {
            if (index != 0) {
                JCVideoPlayer.releaseAllVideos();
            }
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[currentItem]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.main_content, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        buttons.get(currentItem).setSelected(false);
        buttons.get(index).setSelected(true);

        textViews.get(currentItem).setTextColor(colorMainUnclick);
        textViews.get(index).setTextColor(colorMainClick);
        currentItem = index;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.i("111111", "1111111111");
            if (JCVideoPlayer.backPress()) {
                Log.i("2222222", "1111111111");
            } else {
                moveTaskToBack(true);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.layout_main, R.id.layout_server, R.id.layout_assembly, R.id.layout_mine})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_main:
                changeFragment(0);
                break;
            case R.id.layout_server:
                changeFragment(1);
                break;
            case R.id.layout_assembly:
                changeFragment(2);
                break;
            case R.id.layout_mine:
                changeFragment(3);
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            MineFragment mineFragment = (MineFragment) fragments[3];
            mineFragment.changeLoginStatus(false);
        } else {
            boolean login = bundle.getBoolean("login", false);
            if (login) {
                MineFragment mineFragment = (MineFragment) fragments[3];
                mineFragment.changeLoginStatus(true);
                ServerFragment serverFragment = (ServerFragment) fragments[1];
                serverFragment.checkAuth();
            } else {
                String type = bundle.getString("JPush");
                switch (type) {
                    case "news":
                        NewsItem.NewsBean newsBean = (NewsItem.NewsBean) bundle.getSerializable("newsItem");
                        Bundle bundle_news = new Bundle();
                        bundle_news.putSerializable("newsItem", newsBean);
                        stepActivity(bundle_news, NewsActivity.class);
                        break;
                    case "pic":
                        NewsItem.NewsBean picBean = (NewsItem.NewsBean) bundle.getSerializable("newsItem");
                        Bundle bundle_pics = new Bundle();
                        bundle_pics.putSerializable("newsItem", picBean);
                        stepActivity(bundle_pics, PicActivity.class);
                        break;
                    case "video":
                        NewsItem.NewsBean videoBean = (NewsItem.NewsBean) bundle.getSerializable("newsItem");
                        Bundle videoBundle = new Bundle();
                        videoBundle.putSerializable("newsItem", videoBean);
                        stepActivity(videoBundle, VideoDetailActivity.class);
                        break;
                    case "list":
                        stepActivity(ComActActivity.class);
                        break;
                    case "my":
                        if (app.getUser() != null) {
                            stepActivity(MyActActivity.class);
                        }
                        break;
                    case "notice":
                        Notice.NoticeBean notice = (Notice.NoticeBean) bundle.getSerializable("notice");
                        Bundle bundle_notice = new Bundle();
                        bundle_notice.putSerializable("notice", notice);
                        stepActivity(bundle_notice, NoticeDetailActivity.class);
                        break;
                    case "bbs":
                        Bbs.BbsBean bbs = (Bbs.BbsBean) bundle.getSerializable("bbs");
                        Bundle bundle_bbs = new Bundle();
                        bundle_bbs.putSerializable("notice", bbs);
                        stepActivity(bundle_bbs, BbsDetailActivity.class);
                        break;
                    case "exit":
                        app.setUser(null);
                        SpUtil.saveUser(MainActivity.this, null);
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("您的账号已被冻结，请重新登录！")
                                .setCancelable(false)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        stepActivity(LoginActivity.class);
                                    }
                                }).setNegativeButton("取消", null);
                        break;
                }
            }
        }

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    // 取得版本号
    public static String GetVersion(Context context) {
        try {
            PackageInfo manager = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return manager.versionName;
        } catch (Exception e) {
            return "";
        }
    }

    public void install(Context context) {
        Log.i("install", "start");
        File file = MyUtils.getCacheFile(MyUtils.APP_NAME, context);
        if (file == null || !file.exists()) {
            return;
        }
        Intent installintent = new Intent();
        installintent.setAction(Intent.ACTION_VIEW);
        // 在Boradcast中启动活动需要添加Intent.FLAG_ACTIVITY_NEW_TASK
        installintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        installintent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        context.startActivity(installintent);
        Log.i("install", "finish");
    }

}
