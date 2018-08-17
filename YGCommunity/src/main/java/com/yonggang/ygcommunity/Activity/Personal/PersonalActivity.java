package com.yonggang.ygcommunity.Activity.Personal;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yalantis.ucrop.UCrop;
import com.yonggang.ygcommunity.Activity.MainActivity;
import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.Entry.User;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.Util.ImageUtils;
import com.yonggang.ygcommunity.Util.SpUtil;
import com.yonggang.ygcommunity.View.CircleImageView;
import com.yonggang.ygcommunity.YGApplication;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;
import com.yonggang.ygcommunity.httpUtil.ProgressSubscriber;
import com.yonggang.ygcommunity.httpUtil.SubscriberOnNextListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

public class PersonalActivity extends BaseActivity {

    @BindView(R.id.text_tel)
    TextView textTel;
    @BindView(R.id.img_head)
    CircleImageView imgHead;
    @BindView(R.id.text_name)
    TextView textName;
    @BindView(R.id.text_sex)
    TextView textSex;
    @BindView(R.id.layout_inNamed)
    LinearLayout layoutInNamed;
    @BindView(R.id.layout_named)
    RelativeLayout layoutNamed;
    @BindView(R.id.layout_identification)
    LinearLayout layoutIdentification;

    private YGApplication app;

    private PopupWindow window;

    private Uri photoUri;

    private final int PIC_FROM_CAMERA = 1;
    private final int PIC_FROMLOCALPHOTO = 0;

    private String username;
    private int sex;
    private String face = "1";

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();
        initData();
        int is_named = getIntent().getExtras().getInt("is_named");
        if (is_named == 0) {
            layoutInNamed.setVisibility(View.VISIBLE);
            layoutNamed.setVisibility(View.GONE);
            layoutIdentification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PersonalActivity.this, IdentificationActivity.class);
                    startActivityForResult(intent, 0x123);
                }
            });
        } else {
            layoutInNamed.setVisibility(View.GONE);
            layoutNamed.setVisibility(View.VISIBLE);
        }
    }

    private void initData() {
        username = app.getUser().getUsername();
        sex = app.getUser().getSex();
        textTel.setText(app.getUser().getPhone());
        Glide.with(PersonalActivity.this)
                .load(app.getUser().getFace_url())
                .centerCrop()
                .into(imgHead);
        textName.setText(app.getUser().getUsername());
        if (sex == 1) {
            textSex.setText("男");
        } else if (sex == 2) {
            textSex.setText("女");
        }
    }


    @OnClick({R.id.btn_complete, R.id.img_finish, R.id.layout_tel, R.id.layout_head, R.id.layout_name, R.id.layout_sex})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_complete:
                setFace(app.getUser().getUser_id(), username, sex, face);
                break;
            case R.id.img_finish:
                finish();
                break;
            case R.id.layout_tel:
                break;
            case R.id.layout_head:
                PhotoPicker.builder()
                        .setPhotoCount(1)
                        .setShowCamera(true)
                        .setPreviewEnabled(false)
                        .start(PersonalActivity.this);
                break;
            case R.id.layout_name:
                AlertDialog.Builder name = new AlertDialog.Builder(PersonalActivity.this);
                View inputView = getLayoutInflater().inflate(R.layout.item_input, null);
                final EditText input = (EditText) inputView.findViewById(R.id.input);
                name.setTitle("请输入新的昵称");
                name.setView(inputView)
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String setName = input.getText().toString();
                                if ("".equals(setName)) {
                                    Toast.makeText(app, "昵称不能为空", Toast.LENGTH_SHORT).show();
                                } else {
                                    username = setName;
                                    textName.setText(setName);
                                }
                            }
                        }).create().show();

                break;
            case R.id.layout_sex:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final String[] item = new String[]{"男", "女"};
                builder.setTitle("设置性别")
                        .setSingleChoiceItems(item, sex - 1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sex = which + 1;
                                textSex.setText(item[which]);
                            }
                        }).create().show();
                break;
        }
    }

//    private void initPop() {
//        View popupView = getLayoutInflater().inflate(R.layout.popup_head, null);
//        LinearLayout layout_camera = (LinearLayout) popupView.findViewById(R.id.layout_camera);
//        LinearLayout layout_pic = (LinearLayout) popupView.findViewById(R.id.layout_pic);
//        layout_camera.setOnClickListener(this);
//        layout_pic.setOnClickListener(this);
//        window = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
//        window.setFocusable(true);
//        window.setOutsideTouchable(true);
//        window.update();
//        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                WindowManager.LayoutParams lp = getWindow().getAttributes();
//                lp.alpha = 1f; //0.0-1.0
//                getWindow().setAttributes(lp);
//            }
//        });
//    }


//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.layout_camera:
//                doHandlerPhoto(PIC_FROM_CAMERA);
//                window.dismiss();
//                break;
//            case R.id.layout_pic:
//                doHandlerPhoto(PIC_FROMLOCALPHOTO);
//                window.dismiss();
//                break;
//        }
//    }

//    private void showPop() {
//        window.showAtLocation(findViewById(R.id.person_main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.alpha = 0.5f; //0.0-1.0
//        getWindow().setAttributes(lp);
//    }


//    private void doHandlerPhoto(int type) {
//        try {
//            File pictureFileDir = new File(
//                    Environment.getExternalStorageDirectory(), "/upload");
//            if (!pictureFileDir.exists()) {
//                boolean is = pictureFileDir.mkdirs();
//            }
//            File picFile = new File(pictureFileDir, "upload.png");
//            if (!picFile.exists()) {
//                picFile.createNewFile();
//            }
//            photoUri = Uri.fromFile(picFile);
//
//            if (type == PIC_FROMLOCALPHOTO) {
//                Intent intent = getCropImageIntent();
//                startActivityForResult(intent, PIC_FROMLOCALPHOTO);
//            } else {
//                Intent cameraIntent = new Intent(
//                        MediaStore.ACTION_IMAGE_CAPTURE);
//                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
//                startActivityForResult(cameraIntent, PIC_FROM_CAMERA);
//            }
//
//        } catch (Exception e) {
//            Log.i("HandlerPicError", e.toString());
//        }
//    }

//    public Intent getCropImageIntent() {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
//        intent.setType("image/*");
//        setIntentParams(intent);
//        return intent;
//    }
//
//    private void cropImageUriByTakePhoto() {
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(photoUri, "image/*");
//        setIntentParams(intent);
//        startActivityForResult(intent, PIC_FROMLOCALPHOTO);
//    }
//
//    /**
//     *
//     */
//    private void setIntentParams(Intent intent) {
//        intent.putExtra("crop", "true");
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        intent.putExtra("outputX", 600);
//        intent.putExtra("outputY", 600);
//        intent.putExtra("noFaceDetection", true);
//        intent.putExtra("scale", true);
//        intent.putExtra("return-data", false);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
//        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE) { // photopicker的返回
//                final Uri selectedUri = data.getData();
//                if (selectedUri != null) {
//                    startCropActivity(data.getData());
//                } else {
//                    Toast.makeText(PersonalActivity.this, "所选图片不符要求，请重新选择", Toast.LENGTH_SHORT).show();
//                }
                List<String> photos = null;
                if (data != null) {
                    photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                }
                if (photos != null) {
                    String photo = photos.get(0);
                    Uri uri = Uri.fromFile(new File(photo));
                    startCropActivity(uri);
                }
            } else if (requestCode == UCrop.REQUEST_CROP) { // 裁剪的返回
                handleCropResult(data);
            }

        } else if (resultCode == UCrop.RESULT_ERROR) {
            handleCropError(data);
        } else {
            layoutInNamed.setVisibility(View.GONE);
            layoutNamed.setVisibility(View.VISIBLE);
            layoutIdentification.setOnClickListener(null);
        }
    }

    /**
     * 开始裁剪
     *
     * @param uri
     */
    private void startCropActivity(@NonNull Uri uri) {
        String destinationFileName = "zhylhead.png";
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));
        UCrop.Options options = new UCrop.Options();
        options.setCompressionFormat(Bitmap.CompressFormat.PNG);
        options.setCompressionQuality(90);
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(false);
        uCrop = uCrop.withAspectRatio(1, 1);
        uCrop.withOptions(options);
        uCrop.start(PersonalActivity.this);
    }

    private void handleCropResult(@NonNull Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
//            ResultActivity.startWithUri(SampleActivity.this, resultUri);
            Bitmap bitmap = decodeUriAsBitmap(resultUri);
            face = ImageUtils.bitmapToString(resultUri.getPath());
            imgHead.setImageBitmap(bitmap);
        } else {
            Toast.makeText(PersonalActivity.this, "裁剪头像出错，请重新尝试", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Toast.makeText(PersonalActivity.this, cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(PersonalActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * URI转Bitmap
     *
     * @param uri
     * @return
     */
    private Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(
                    getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }


    private void setFace(String user_id, String username, int sex, String face) {
        Log.i("face123", face);
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<User>() {
            @Override
            public void onNext(User user) {
                Log.i("sface", user.toString());
                app.setUser(user);
                SpUtil.saveUser(PersonalActivity.this, user);
                Toast.makeText(app, "修改成功", Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putBoolean("login", true);
                goActivity(bundle, MainActivity.class);
                finish();
            }
        };
        HttpUtil.getInstance().setFace(new ProgressSubscriber<User>(onNextListener, this, "修改中"), user_id, username, sex, face);
    }


}
