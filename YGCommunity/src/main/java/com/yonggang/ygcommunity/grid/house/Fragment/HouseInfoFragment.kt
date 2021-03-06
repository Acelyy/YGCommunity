package com.yonggang.ygcommunity.grid.house.Fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.*
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.alibaba.fastjson.JSON
import com.autonavi.amap.mapcore.tools.GLFileUtil.getCacheDir
import com.baidu.ocr.sdk.OCR
import com.baidu.ocr.sdk.OnResultListener
import com.baidu.ocr.sdk.exception.OCRError
import com.baidu.ocr.sdk.model.IDCardParams
import com.baidu.ocr.sdk.model.IDCardResult
import com.baidu.ocr.ui.camera.CameraActivity
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bumptech.glide.Glide
import com.yalantis.ucrop.UCrop
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.Util.FileUtil
import com.yonggang.ygcommunity.YGApplication
import com.yonggang.ygcommunity.grid.house.HouseInfoActivity
import com.yonggang.ygcommunity.grid.house.SelectHouseActivity
import com.yonggang.ygcommunity.Entry.HouseInfo
import com.yonggang.ygcommunity.Util.ImageUtils
import com.yonggang.ygcommunity.httpUtil.HttpUtil
import com.yonggang.ygcommunity.httpUtil.ProgressSubscriber
import com.yonggang.ygcommunity.httpUtil.SubscriberOnNextListener
import kotlinx.android.synthetic.main.fragment_house_info.*
import me.iwf.photopicker.PhotoPicker
import me.iwf.photopicker.PhotoPreview
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [HouseInfoFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [HouseInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class HouseInfoFragment : Fragment() {

    private lateinit var app: YGApplication
    private lateinit var onInfoSubmit: HouseInfoActivity.OnInfoSubmitListener
    private lateinit var onRemoveFragment: HouseInfoActivity.OnRemoveFragment
    private var dialog: AlertDialog? = null
    private lateinit var myTextWatcher: MyTextWatcher
    private var address_pk: String? = null
    private lateinit var updateAddressBroadcast: UpdateAddressBroadcast
    private val photoPaths = ArrayList<String>()
    private lateinit var sfzh: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity.application as YGApplication
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_house_info, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initDatePicker()

        submit.setOnClickListener {
            setHouseInfo()
        }
        scanner.setOnClickListener {
            val intent = Intent(activity, CameraActivity::class.java)
            intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                    FileUtil.getSaveFile(activity).absolutePath)
            intent.putExtra(CameraActivity.KEY_NATIVE_ENABLE,
                    true)
            // KEY_NATIVE_MANUAL设置了之后CameraActivity中不再自动初始化和释放模型
            // 请手动使用CameraNativeHelper初始化和释放模型
            // 推荐这样做，可以避免一些activity切换导致的不必要的异常
            intent.putExtra(CameraActivity.KEY_NATIVE_MANUAL,
                    true)
            intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT)
            startActivityForResult(intent, REQUEST_CODE_CAMERA)
        }


        /**
         * 监听身份证的字符长度  18位时进行查询
         */
        myTextWatcher = MyTextWatcher(null)
        number.addTextChangedListener(myTextWatcher)
        number.text = Editable.Factory.getInstance().newEditable(sfzh)


        // 初始化选择器
        setPicker(layout_political, political, listPolitical, "政治面貌")
        setPicker(layout_education, education, listEducation, "文化程度")
        setPicker(layout_marriage, marriage, listMarriage, "婚姻状况")

        updateAddressBroadcast = UpdateAddressBroadcast()
        val filter = IntentFilter()
        filter.addAction("data")
        activity.registerReceiver(updateAddressBroadcast, filter)

        address.setOnClickListener {
            activity.startActivity<SelectHouseActivity>()
        }

        rg_type.check(R.id.floating)

    }

    override fun onDestroy() {
        super.onDestroy()
        activity.unregisterReceiver(updateAddressBroadcast)
    }

    /**
     *
     */
    inner class MyTextWatcher(var result: IDCardResult?) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (number.text.length == 18) {
                getHouseInfo(result, number.text.toString().trim())
            }
        }

    }

    /**
     * 统一设置选择器
     */
    private fun setPicker(layout: LinearLayout, tv: TextView, data: List<String>, title: String) {
        var index = 0
        layout.setOnClickListener {
            val pvOptions = OptionsPickerBuilder(activity, OnOptionsSelectListener { options1, _, _, _ ->
                index = options1
                tv.text = data[index]
            }).setOutSideCancelable(false).setContentTextSize(25).build<String>()
            pvOptions.setPicker(data)
            pvOptions.setSelectOptions(index)
            pvOptions.setTitleText("请选择$title")
            pvOptions.show()
        }
    }

    private fun initDatePicker() {
        val view = LayoutInflater.from(activity).inflate(R.layout.item_date_picker, null)
        val picker = view.find<DatePicker>(R.id.date)

        val builder = AlertDialog.Builder(activity)
        builder.setTitle("请选择时间")
                .setView(view)
                .setPositiveButton("确定") { _, _ ->
                    birth.text = SimpleDateFormat("yyyy-MM-dd").format(Date(picker.year - 1900, picker.month, picker.dayOfMonth))
                }.setNegativeButton("取消") { _, _ ->

                }
        if (dialog == null)
            dialog = builder.create()

        birth.setOnClickListener {
            dialog!!.show()
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment HouseInfoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(onInfoSubmit: HouseInfoActivity.OnInfoSubmitListener, onRemoveFragment: HouseInfoActivity.OnRemoveFragment, sfzh: String) =
                HouseInfoFragment().apply {
                    this.onInfoSubmit = onInfoSubmit
                    this.onRemoveFragment = onRemoveFragment
                    this.sfzh = sfzh
                }

        private val listEducation = listOf(
                "学龄前儿童",
                "幼儿园",
                "文盲",
                "小学以下",
                "小学",
                "中学",
                "高中（职高）",
                "大专",
                "本科",
                "硕士及以上"
        )

        private val listMarriage = listOf(
                "未婚",
                "初婚",
                "已婚",
                "再婚",
                "离异",
                "丧偶",
                "分居",
                "其他"
        )

        private val listPolitical = listOf(
                "群众",
                "团员",
                "中共党员",
                "其他"
        )

        private val REQUEST_CODE_CAMERA = 102
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_CAMERA) {
                if (data != null) {
                    val contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE)
                    val filePath = FileUtil.getSaveFile(activity).absolutePath
                    if (!TextUtils.isEmpty(contentType)) {
                        if (CameraActivity.CONTENT_TYPE_ID_CARD_FRONT == contentType) {
                            recIDCard(IDCardParams.ID_CARD_SIDE_FRONT, filePath)
                        } else if (CameraActivity.CONTENT_TYPE_ID_CARD_BACK == contentType) {
                            recIDCard(IDCardParams.ID_CARD_SIDE_BACK, filePath)
                        }
                    }
                }

            }
            if (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE) { // photopicker的返回
                var photos: List<String>? = null
                if (data != null) {
                    photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS)
                }
                if (photos != null) {
                    val photo = photos[0]
                    val uri = Uri.fromFile(File(photo))
                    startCropActivity(uri, File(photo).name)
                }
            }
            if (requestCode == UCrop.REQUEST_CROP) { // 裁剪的返回
                handleCropResult(data!!)
            }
        }

        if (resultCode == UCrop.RESULT_ERROR) {
            handleCropError(data!!)
        }
    }

    /**
     * 开始裁剪
     *
     * @param uri
     */
    private fun startCropActivity(uri: Uri, fileName: String) {
        var uCrop = UCrop.of(uri, Uri.fromFile(File(getCacheDir(activity), fileName)))
        val options = UCrop.Options()
        options.setCompressionFormat(Bitmap.CompressFormat.PNG)
        options.setCompressionQuality(90)
        options.setHideBottomControls(false)
        options.setFreeStyleCropEnabled(false)
        uCrop = uCrop.withAspectRatio(1f, 1f)
        uCrop.withOptions(options)
        uCrop.start(activity)
    }

    private fun handleCropResult(result: Intent) {
        val resultUri = UCrop.getOutput(result)
        if (resultUri != null) {
            val imgs = ArrayList<String>()
            val observable = rx.Observable.create(rx.Observable.OnSubscribe<String> {
                imgs.add(ImageUtils.bitmapToString(resultUri.path))
                it.onCompleted()
            }).subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            val subscriber = object : Subscriber<String>() {
                override fun onNext(t: String?) {

                }

                override fun onCompleted() {
                    val subscriberOnNextListener = SubscriberOnNextListener<String> {
                        Glide.with(activity).load(resultUri).into(head)
                        Snackbar.make(submit, "上传成功", Snackbar.LENGTH_LONG).show()

                    }
                    HttpUtil.getInstance().setPhote(ProgressSubscriber<HouseInfo>(subscriberOnNextListener, activity, "头像上传中"), number.text.toString().trim(), JSON.toJSONString(imgs))
                }

                override fun onError(e: Throwable?) {
                    Log.i("error", e.toString())
                }

            }
            observable.subscribe(subscriber)

        } else {
            Toast.makeText(activity, "裁剪头像出错，请重新尝试", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleCropError(result: Intent) {
        val cropError = UCrop.getError(result)
        if (cropError != null) {
            Toast.makeText(activity, cropError.message, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(activity, "未知错误", Toast.LENGTH_SHORT).show()
        }
    }


    private fun recIDCard(idCardSide: String, filePath: String) {
        val param = IDCardParams()
        param.imageFile = File(filePath)
        // 设置身份证正反面
        param.idCardSide = idCardSide
        // 设置方向检测
        param.isDetectDirection = true
        // 设置图像参数压缩质量0-100, 越大图像质量越好但是请求时间越长。 不设置则默认值为20
        param.imageQuality = 20

        OCR.getInstance(activity).recognizeIDCard(param, object : OnResultListener<IDCardResult> {
            override fun onResult(result: IDCardResult?) {
                if (result != null) {
                    myTextWatcher.result = result
                    number.text = Editable.Factory.getInstance().newEditable("" + result.idNumber)
                } else {
                    Snackbar.make(scanner, "扫描失败", Snackbar.LENGTH_LONG)
                }
            }

            override fun onError(error: OCRError) {
                Log.i("recIDCard", JSON.toJSONString(error))
            }
        })
    }


    /**
     * 根据身份证获取信息
     * @param id 身份证号
     */
    private fun getHouseInfo(result: IDCardResult?, id: String) {
        val subscriberOnNextListener = SubscriberOnNextListener<HouseInfo> {
            //            Log.i("getHouseInfo", it)

            Log.i("getHouseInfo", JSON.toJSONString(it))
            onRemoveFragment.onRemoveFragment()
            if (it != null) {
                address_pk = it.fwbm_pk
                name.text = Editable.Factory.getInstance().newEditable(if (it.xm == null) {
                    ""
                } else {
                    it.xm
                })
                when (it.xb) {
                    "男" -> rg_sex.check(R.id.man)
                    "女" -> rg_sex.check(R.id.woman)
                }
                birth.text = Editable.Factory.getInstance().newEditable(if (it.csrq == null) {
                    ""
                } else {
                    it.csrq
                })
                depart.text = Editable.Factory.getInstance().newEditable(if (it.gzdw != null) {
                    it.gzdw
                } else {
                    if (it.zdxx == null) {
                        ""
                    } else {
                        it.zdxx
                    }
                })
                nation.text = Editable.Factory.getInstance().newEditable(if (it.mz == null) {
                    ""
                } else {
                    it.mz
                })
                political.text = it.zzmm
                address.text = Editable.Factory.getInstance().newEditable(if (it.xjzdz == null) {
                    ""
                } else {
                    it.xjzdz
                })
                tell.text = Editable.Factory.getInstance().newEditable(if (it.lxdh == null) {
                    ""
                } else {
                    it.lxdh
                })
                marriage.text = it.hyzk
                household.text = Editable.Factory.getInstance().newEditable(if (it.hjdz == null) {
                    ""
                } else {
                    it.hjdz
                })
                house_id.text = Editable.Factory.getInstance().newEditable(if (it.hjbh == null) {
                    ""
                } else {
                    it.hjbh
                })
                hobby.text = Editable.Factory.getInstance().newEditable(if (it.xqah == null) {
                    ""
                } else {
                    it.xqah
                })
                education.text = it.whcd

                when (it.sfsy) {
                    1 -> rg_type.check(R.id.community)
                    2 -> rg_type.check(R.id.register)
                    3 -> rg_type.check(R.id.floating)
                    4 -> rg_type.check(R.id.member)
                }

                if (it.sfyf == 1) {
                    rg_yf.check(R.id.true_yf)
                } else {
                    rg_yf.check(R.id.false_yf)
                }

                if (it.sftf == 1) {
                    rg_tf.check(R.id.true_tf)
                } else {
                    rg_tf.check(R.id.false_ft)
                }

                if (it.sfjsb == 1) {
                    rg_jsb.check(R.id.true_jsb)
                } else {
                    rg_jsb.check(R.id.false_jsb)
                }

                if (it.sfkc == 1) {
                    rg_kc.check(R.id.true_kc)
                } else {
                    rg_kc.check(R.id.false_kc)
                }

                if (it.sfdj == 1) {
                    rg_dj.check(R.id.true_dj)
                } else {
                    rg_dj.check(R.id.false_dj)
                }

                if (it.sfpk == 1) {
                    rg_pkh.check(R.id.true_pkh)
                } else {
                    rg_pkh.check(R.id.false_pkh)
                }

                if (it.sfdbh == 1) {
                    rg_dbh.check(R.id.true_dbh)
                } else {
                    rg_dbh.check(R.id.false_dbh)
                }

                if (it.sfxsm == 1) {
                    rg_xsm.check(R.id.true_xsm)
                } else {
                    rg_xsm.check(R.id.false_xsm)
                }

                if (it.sffd == 1) {
                    rg_fd.check(R.id.true_fd)
                } else {
                    rg_fd.check(R.id.false_fd)
                }

                if (it.sfcj == 0) {
                    rg_cj.check(R.id.false_cj)
                } else {
                    when (it.cjdj) {
                        1 -> rg_cj.check(R.id.cj1);
                        2 -> rg_cj.check(R.id.cj2);
                        3 -> rg_cj.check(R.id.cj3);
                        4 -> rg_cj.check(R.id.cj4);
                        else -> rg_cj.check(R.id.false_cj)
                    }
                }

                disease.text = Editable.Factory.getInstance().newEditable(if (it.bz == null) {
                    ""
                } else {
                    it.bz
                })

                volunteerId.text = Editable.Factory.getInstance().newEditable(if (it.zyzzh == null) {
                    ""
                } else {
                    it.zyzzh
                })

                landlordTel.text = Editable.Factory.getInstance().newEditable(if (it.fdlxdh == null) {
                    ""
                } else {
                    it.fdlxdh
                })

                carNumber.text = Editable.Factory.getInstance().newEditable(if (it.cph == null) {
                    ""
                } else {
                    it.cph
                })

                hobby.text = Editable.Factory.getInstance().newEditable(if (it.xqah == null) {
                    ""
                } else {
                    it.xqah
                })
                if (it.tplj != "" && it.tplj != null) {
                    Glide.with(activity).load(it.tplj).into(head)
                }
                if (it.sfsy != 3) {
                    head.setOnClickListener {
                        PhotoPicker.builder()
                                .setPhotoCount(1)
                                .setShowCamera(true)
                                .setPreviewEnabled(false)
                                .start(activity)
                    }
                }

            } else {
                if (result != null) {
                    name.text = Editable.Factory.getInstance().newEditable(result.name.toString())
                    if (result.gender.toString() == "男")
                        rg_sex.check(R.id.man) else rg_sex.check(R.id.woman)
                    birth.text = Editable.Factory.getInstance().newEditable(result.birthday.toString())
                    nation.text = Editable.Factory.getInstance().newEditable(result.ethnic.toString())
//                    address.text = Editable.Factory.getInstance().newEditable(result.address.toString())
                } else {
                    //Snackbar.make(refresh, "扫描失败", Snackbar.LENGTH_LONG).show()
                    Log.i("扫描失败", "扫描失败")
                }
            }
        }
        HttpUtil.getInstance().getHouseInfo(ProgressSubscriber<HouseInfo>(subscriberOnNextListener, activity, "查询是否已存在信息"), id)

    }


    /**
     * 保存个人信息
     */
    private fun setHouseInfo() {
        if (number.text.toString().equals("")) {
            Snackbar.make(submit, "清输入身份证号", Snackbar.LENGTH_LONG).show()
            return
        }
        if (name.text.toString().equals("")) {
            Snackbar.make(submit, "请输入姓名", Snackbar.LENGTH_LONG).show()
            return
        }
        if (nation.text.toString().equals("")) {
            Snackbar.make(submit, "请输入民族", Snackbar.LENGTH_LONG).show()
            return
        }
        if (address_pk == null) {
            Snackbar.make(submit, "请选择居住地址", Snackbar.LENGTH_LONG).show()
            return
        }
        if (address.text.toString().equals("")) {
            Snackbar.make(submit, "请输入居住地址", Snackbar.LENGTH_LONG).show()
            return
        }
        if (tell.text.toString().equals("")) {
            Snackbar.make(submit, "请输入联系电话", Snackbar.LENGTH_LONG).show()
            return
        }
        val subscriberOnNextListener = SubscriberOnNextListener<String> {
            Log.i("setHouseInfo", it)
            onInfoSubmit.onInfoSubmit(it, when {
                community.isChecked -> 1
                register.isChecked -> 2
                floating.isChecked -> 3
                member.isChecked -> 4
                else -> 0
            })
        }

        val str = "" +
                "类型" + when {
            community.isChecked -> 1
            register.isChecked -> 2
            floating.isChecked -> 3
            member.isChecked -> 4
            else -> 0
        } + "\n" +
                "身份证：" + number.text.toString().trim() + "\n" +
                "姓名：" + name.text.toString().trim() + "\n" +
                "性别：" + when (rg_sex.checkedRadioButtonId) {
            R.id.man -> "男"
            else -> "女"
        } + "\n" +
                "出生日期：" + birth.text.toString().trim() + "\n" +
                "工作/学校：" + depart.text.toString().trim() + "\n" +
                "政治面貌：" + nation.text.toString().trim() + "\n" +
                "地址：" + address_pk + "\n" +
                "电话：" + tell.text.toString().trim() + "\n" +
                "婚姻状况：" + marriage.text.toString().trim() + "\n" +
                "文化程度：" + education.text.toString().trim() + "\n" +
                "户籍地址：" + household.text.toString().trim() + "\n" +
                "户籍号：" + house_id.text.toString().trim() + "\n" +
                "优抚：" + when (rg_yf.checkedRadioButtonId) {
            R.id.true_yf -> 1
            else -> 0
        } + "\n" +
                "特扶：" + when (rg_tf.checkedRadioButtonId) {
            R.id.true_tf -> 1
            else -> 0
        } + "\n" +
                "精神病：" + when (rg_jsb.checkedRadioButtonId) {
            R.id.true_jsb -> 1
            else -> 0
        } + "\n" +
                "空巢：" + when (rg_kc.checkedRadioButtonId) {
            R.id.true_kc -> 1
            else -> 0
        } + "\n" +
                "独居：" + when (rg_dj.checkedRadioButtonId) {
            R.id.true_dj -> 1
            else -> 0
        } + "\n" +
                "平困户：" + when (rg_pkh.checkedRadioButtonId) {
            R.id.true_pkh -> 1
            else -> 0
        } + "\n" +
                "低保：" + when (rg_dbh.checkedRadioButtonId) {
            R.id.true_dbh -> 1
            else -> 0
        } + "\n" +
                "新市民：" + when (rg_xsm.checkedRadioButtonId) {
            R.id.true_xsm -> 1
            else -> 0
        } + "\n" +
                "房东：" + when (rg_fd.checkedRadioButtonId) {
            R.id.true_fd -> 1
            else -> 0
        } + "\n" +
                "残疾：" + when {
            false_cj.isChecked -> 0
            else -> 1
        } + "====" + when {
            cj1.isChecked -> 1
            cj2.isChecked -> 2
            cj3.isChecked -> 3
            cj4.isChecked -> 4
            else -> 0
        } + "\n" +
                "重大疾病：" + disease.text.toString().trim() + "\n" +
                "志愿者证号：" + volunteerId.text.toString().trim() + "\n" +
                "房东联系方式：" + landlordTel.text.toString().trim() + "\n" +
                "车牌号：" + carNumber.text.toString().trim() + "\n" +
                "爱好：" + hobby.text.toString().trim()
        Log.i("提交", str)


        HttpUtil.getInstance().setHouseInfo(ProgressSubscriber<String>(subscriberOnNextListener, activity, "保存信息中"),
                when {
                    community.isChecked -> 1
                    register.isChecked -> 2
                    floating.isChecked -> 3
                    member.isChecked -> 4
                    else -> 0
                },//1
                number.text.toString().trim(),//2
                name.text.toString().trim(),//3

                when (rg_sex.checkedRadioButtonId) {
                    R.id.man -> "男"
                    else -> "女"
                },//4

                birth.text.toString().trim(),//5
                depart.text.toString().trim(),//6
                nation.text.toString().trim(),//7
                political.text.toString().trim(),//8
                address_pk,//9
                tell.text.toString().trim(),//10
                marriage.text.toString().trim(),//11
                education.text.toString().trim(),//12
                household.text.toString().trim(),//13
                house_id.text.toString().trim(),//14

                when (rg_yf.checkedRadioButtonId) {
                    R.id.true_yf -> 1
                    else -> 0
                },//15
                when (rg_tf.checkedRadioButtonId) {
                    R.id.true_tf -> 1
                    else -> 0
                },//16
                when (rg_jsb.checkedRadioButtonId) {
                    R.id.true_jsb -> 1
                    else -> 0
                },//17
                when (rg_kc.checkedRadioButtonId) {
                    R.id.true_kc -> 1
                    else -> 0
                },//18
                when (rg_dj.checkedRadioButtonId) {
                    R.id.true_dj -> 1
                    else -> 0
                },//19
                when (rg_pkh.checkedRadioButtonId) {
                    R.id.true_pkh -> 1
                    else -> 0
                },//20
                when (rg_dbh.checkedRadioButtonId) {
                    R.id.true_dbh -> 1
                    else -> 0
                },//21
                when (rg_xsm.checkedRadioButtonId) {
                    R.id.true_xsm -> 1
                    else -> 0
                },//22
                when (rg_fd.checkedRadioButtonId) {
                    R.id.true_fd -> 1
                    else -> 0
                },//23
                when {
                    false_cj.isChecked -> 0
                    else -> 1
                },//24
                when {
                    cj1.isChecked -> 1
                    cj2.isChecked -> 2
                    cj3.isChecked -> 3
                    cj4.isChecked -> 4
                    else -> 0
                },//25
                disease.text.toString().trim(),//26
                volunteerId.text.toString().trim(),//27
                landlordTel.text.toString().trim(),//28
                carNumber.text.toString().trim(),//29
                hobby.text.toString().trim(),//30
                app.grid.id//31
        )

    }

    inner class UpdateAddressBroadcast : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val house = intent!!.getStringExtra("address")
            address_pk = intent.getStringExtra("pk")
            Log.i("address_pk", address_pk)
            address.text = house
        }

    }


}
