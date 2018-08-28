package com.yonggang.ygcommunity.grid.house.Fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
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
import android.widget.DatePicker
import android.widget.LinearLayout
import android.widget.TextView
import com.alibaba.fastjson.JSON
import com.baidu.ocr.sdk.OCR
import com.baidu.ocr.sdk.OnResultListener
import com.baidu.ocr.sdk.exception.OCRError
import com.baidu.ocr.sdk.model.IDCardParams
import com.baidu.ocr.sdk.model.IDCardResult
import com.baidu.ocr.ui.camera.CameraActivity
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.Util.FileUtil
import com.yonggang.ygcommunity.grid.house.HouseInfoActivity
import com.yonggang.ygcommunity.httpUtil.HouseInfo
import com.yonggang.ygcommunity.httpUtil.HttpUtil
import com.yonggang.ygcommunity.httpUtil.ProgressSubscriber
import com.yonggang.ygcommunity.httpUtil.SubscriberOnNextListener
import kotlinx.android.synthetic.main.fragment_house_info.*
import org.jetbrains.anko.find
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

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

    private lateinit var onInfoSubmit: HouseInfoActivity.OnInfoSubmitListener
    private lateinit var RemoveFragements: HouseInfoActivity.RemoveFragements
    private var dialog: AlertDialog? = null
    private lateinit var myTextWatcher: MyTextWatcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        // 初始化选择器
        setPicker(layout_political, political, listPolitical, "政治面貌")
        setPicker(layout_education, education, listEducation, "文化程度")
        setPicker(layout_marriage, marriage, listMarriage, "婚姻状况")
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
        fun newInstance(onInfoSubmit: HouseInfoActivity.OnInfoSubmitListener,RF:HouseInfoActivity.RemoveFragements) =
                HouseInfoFragment().apply {
                    this.onInfoSubmit = onInfoSubmit
                    this.RemoveFragements = RF
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
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
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
            Log.i("getHouseInfo", JSON.toJSONString(it))
            if (it != null) {
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

                if (it.sfsy == 1) {
                    rg_type.check(R.id.community)
                } else {
                    if (it.sfhj == 1) {
                        rg_type.check(R.id.register)
                    } else {
                        rg_type.check(R.id.floating)
                    }
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
                    }
                }

                disease.text = Editable.Factory.getInstance().newEditable(if (it.bz == null) {
                    ""
                } else {
                    it.bz
                })

                volunteerId.text = Editable.Factory.getInstance().newEditable(if (it.zyzh == null) {
                    ""
                } else {
                    it.zyzh
                })

                disease.text = Editable.Factory.getInstance().newEditable(if (it.fdlxdh == null) {
                    ""
                } else {
                    it.fdlxdh
                })

                disease.text = Editable.Factory.getInstance().newEditable(if (it.cph == null) {
                    ""
                } else {
                    it.cph
                })

                disease.text = Editable.Factory.getInstance().newEditable(if (it.xqah == null) {
                    ""
                } else {
                    it.xqah
                })
            } else {
                if (result != null) {
                    name.text = Editable.Factory.getInstance().newEditable(result.name.toString())
                    if (result.gender.toString() == "男")
                        rg_sex.check(R.id.man) else rg_sex.check(R.id.woman)
                    birth.text = Editable.Factory.getInstance().newEditable(result.birthday.toString())
                    nation.text = Editable.Factory.getInstance().newEditable(result.ethnic.toString())
                    address.text = Editable.Factory.getInstance().newEditable(result.address.toString())
                }
            }
        }
        HttpUtil.getInstance().getHouseInfo(ProgressSubscriber<HouseInfo>(subscriberOnNextListener, activity, "查询是否已存在信息"), id)
        RemoveFragements.RemoveFragemt()
    }


    /**
     * 保存个人信息
     */
    private fun setHouseInfo() {
        val subscriberOnNextListener = SubscriberOnNextListener<String> {
            Log.i("setHouseInfo", it)
            onInfoSubmit.onInfoSubmit(it, if (rg_type.checkedRadioButtonId == R.id.community) {
                1
            } else {
                0
            })
        }
        HttpUtil.getInstance().setHouseInfo(ProgressSubscriber<String>(subscriberOnNextListener, activity, "保存信息中"),
                when (rg_type.checkedRadioButtonId) {
                    R.id.community -> 0
                    R.id.register -> 0
                    R.id.floating -> 1
                    else -> 0
                },
                when (rg_type.checkedRadioButtonId) {
                    R.id.community -> 1
                    R.id.register -> 1
                    R.id.floating -> 0
                    else -> 0
                },
                number.text.toString().trim(),
                name.text.toString().trim(),

                when (rg_sex.checkedRadioButtonId) {
                    R.id.man -> "男"
                    else -> "女"
                },

                birth.text.toString().trim(),
                depart.text.toString().trim(),
                nation.text.toString().trim(),
                political.text.toString().trim(),
                address.text.toString().trim(),
                tell.text.toString().trim(),
                marriage.text.toString().trim(),
                education.text.toString().trim(),
                household.text.toString().trim(),
                house_id.text.toString().trim(),

                when (rg_yf.checkedRadioButtonId) {
                    R.id.true_yf -> 1
                    else -> 0
                },
                when (rg_tf.checkedRadioButtonId) {
                    R.id.true_tf -> 1
                    else -> 0
                },
                when (rg_jsb.checkedRadioButtonId) {
                    R.id.true_jsb -> 1
                    else -> 0
                },
                when (rg_kc.checkedRadioButtonId) {
                    R.id.true_kc -> 1
                    else -> 0
                },
                when (rg_dj.checkedRadioButtonId) {
                    R.id.true_dj -> 1
                    else -> 0
                },
                when (rg_pkh.checkedRadioButtonId) {
                    R.id.true_pkh -> 1
                    else -> 0
                },
                when (rg_dbh.checkedRadioButtonId) {
                    R.id.true_dbh -> 1
                    else -> 0
                },
                when (rg_xsm.checkedRadioButtonId) {
                    R.id.true_xsm -> 1
                    else -> 0
                },
                when (rg_fd.checkedRadioButtonId) {
                    R.id.true_fd -> 1
                    else -> 0
                },
                when (rg_cj.checkedRadioButtonId) {
                    R.id.false_cj -> 0
                    else -> 1
                },
                when (rg_cj.checkedRadioButtonId) {
                    R.id.cj1 -> 1
                    R.id.cj2 -> 2
                    R.id.cj3 -> 3
                    R.id.cj4 -> 4
                    else -> 0
                },
                disease.text.toString().trim(),
                volunteerId.text.toString().trim(),
                landlordTel.text.toString().trim(),
                carNumber.text.toString().trim(),
                hobby.text.toString().trim()
        )
    }


}
