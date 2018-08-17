package com.yonggang.ygcommunity.Activity.Personal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.PhotoPicker.PhotoAdapter;
import com.yonggang.ygcommunity.PhotoPicker.RecyclerItemClickListener;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.Util.ImageUtils;
import com.yonggang.ygcommunity.View.RadioGroup;
import com.yonggang.ygcommunity.YGApplication;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;
import com.yonggang.ygcommunity.httpUtil.ProgressSubscriber;
import com.yonggang.ygcommunity.httpUtil.SubscriberOnNextListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

public class OptionActivity extends BaseActivity {

    @BindView(R.id.text_option)
    EditText textOption;
    @BindView(R.id.list_pic)
    RecyclerView listPic;
    @BindView(R.id.option)
    RadioGroup option;

    private List<String> images;

    private PhotoAdapter adapter;

    private ArrayList<String> photoPaths = new ArrayList<String>();

    private String option_type = "";//报错类型

    private YGApplication app;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ProgressDialog dialog = new ProgressDialog(OptionActivity.this);
            dialog.setMessage("压缩中");
            switch (msg.what) {
                case 0:
                    dialog.show();
                    break;
                case 1:
                    dialog.dismiss();
                    SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<String>() {
                        @Override
                        public void onNext(String s) {
                            Log.i("file", s);
                            Toast.makeText(OptionActivity.this, "已成功提交", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    };
                    HttpUtil.getInstance().feedback(new ProgressSubscriber<String>(onNextListener, OptionActivity.this, "发布中"), app.getUser().getUser_id(), textOption.getText().toString().trim(), option_type, JSON.toJSONString(images));
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();
        adapter = new PhotoAdapter(this, photoPaths);
        listPic.setLayoutManager(new StaggeredGridLayoutManager(5, OrientationHelper.VERTICAL));
        listPic.setAdapter(adapter);

        listPic.addOnItemTouchListener(new RecyclerItemClickListener(this,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (adapter.getItemViewType(position) == PhotoAdapter.TYPE_ADD) {
                            PhotoPicker.builder()
                                    .setPhotoCount(PhotoAdapter.MAX)
                                    .setShowCamera(true)
                                    .setPreviewEnabled(false)
                                    .setSelected(photoPaths)
                                    .start(OptionActivity.this);
                        } else {
                            PhotoPreview.builder()
                                    .setPhotos(photoPaths)
                                    .setCurrentItem(position)
                                    .start(OptionActivity.this);
                        }
                    }
                }));

        option.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                option_type = ((RadioButton) (OptionActivity.this.findViewById(checkedId))).getText().toString();
            }
        });
    }

    @OnClick({R.id.img_finish, R.id.btn_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_finish:
                finish();
                break;
            case R.id.btn_commit:
                upload_error(textOption.getText().toString().trim(), option_type);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK &&
                (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {
            List<String> photos = null;
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            }
            photoPaths.clear();
            if (photos != null) {
                photoPaths.addAll(photos);
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void upload_error(String error_content, String error_type) {
        if ("".equals(error_content)) {
            Toast.makeText(this, "报错内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("".equals(error_type)) {
            Toast.makeText(this, "请选择错误类型", Toast.LENGTH_SHORT).show();
            return;
        }
        if (photoPaths.size() == 0) {
            Toast.makeText(app, "请至少选择一张图片", Toast.LENGTH_SHORT).show();
            return;
        }
        new WorkThread().start();
    }

    /**
     * 用于执行BitMap转Base64操作
     */
    class WorkThread extends Thread {
        @Override
        public void run() {
            handler.sendEmptyMessage(0);
            super.run();
            images = new ArrayList<>();
            for (int i = 0; i < photoPaths.size(); i++) {
                try {
                    images.add(ImageUtils.bitmapToString(photoPaths.get(i)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Log.i("base64", JSON.toJSONString(images));
            handler.sendEmptyMessage(1);
        }
    }

}
