package com.yonggang.ygcommunity.httpUtil;


import com.yonggang.ygcommunity.Entry.ActDetail;
import com.yonggang.ygcommunity.Entry.Activity;
import com.yonggang.ygcommunity.Entry.Address;
import com.yonggang.ygcommunity.Entry.AdvImg;
import com.yonggang.ygcommunity.Entry.Alipay;
import com.yonggang.ygcommunity.Entry.Answer;
import com.yonggang.ygcommunity.Entry.Bbs;
import com.yonggang.ygcommunity.Entry.BbsSystem;
import com.yonggang.ygcommunity.Entry.BbsUser;
import com.yonggang.ygcommunity.Entry.Collect;
import com.yonggang.ygcommunity.Entry.Comments;
import com.yonggang.ygcommunity.Entry.Contacts;
import com.yonggang.ygcommunity.Entry.Event;
import com.yonggang.ygcommunity.Entry.EventDetail;
import com.yonggang.ygcommunity.Entry.Expense;
import com.yonggang.ygcommunity.Entry.Filter;
import com.yonggang.ygcommunity.Entry.FirstImg;
import com.yonggang.ygcommunity.Entry.Free;
import com.yonggang.ygcommunity.Entry.Fwt_Carousel;
import com.yonggang.ygcommunity.Entry.Gift;
import com.yonggang.ygcommunity.Entry.GridEvent;
import com.yonggang.ygcommunity.Entry.GridEventDetail;
import com.yonggang.ygcommunity.Entry.GridStatus;
import com.yonggang.ygcommunity.Entry.GridUser;
import com.yonggang.ygcommunity.Entry.Home;
import com.yonggang.ygcommunity.Entry.HotLine;
import com.yonggang.ygcommunity.Entry.HouseFamily;
import com.yonggang.ygcommunity.Entry.HttpResult;
import com.yonggang.ygcommunity.Entry.Info;
import com.yonggang.ygcommunity.Entry.Message;
import com.yonggang.ygcommunity.Entry.MyActivity;
import com.yonggang.ygcommunity.Entry.NewsItem;
import com.yonggang.ygcommunity.Entry.Notice;
import com.yonggang.ygcommunity.Entry.PayRecord;
import com.yonggang.ygcommunity.Entry.PicBean;
import com.yonggang.ygcommunity.Entry.Publish;
import com.yonggang.ygcommunity.Entry.Score;
import com.yonggang.ygcommunity.Entry.Search;
import com.yonggang.ygcommunity.Entry.Signed;
import com.yonggang.ygcommunity.Entry.SignedPerson;
import com.yonggang.ygcommunity.Entry.Signin;
import com.yonggang.ygcommunity.Entry.Title;
import com.yonggang.ygcommunity.Entry.TotalScore;
import com.yonggang.ygcommunity.Entry.User;
import com.yonggang.ygcommunity.Entry.Version;
import com.yonggang.ygcommunity.Entry.WechatPay;
import com.yonggang.ygcommunity.monitor.model.MonitorModel;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by liyangyang on 17/3/14.
 */
public interface HttpService {

    //获取版本号
    @POST("get_version")
    Observable<HttpResult<Version>> get_version();

    //获取新闻标题列表
    @POST("category_list")
    Observable<HttpResult<List<Title>>> category_list();

    //获取具体类型新闻
    @FormUrlEncoded
    @POST("clist")
    Observable<HttpResult<NewsItem>> c_list(
            @Field("cid") String cid,
            @Field("ctype") int ctype,
            @Field("page") int page,
            @Field("user_id") String user_id);

    //发送验证码
    @FormUrlEncoded
    @POST("send_msg")
    Observable<HttpResult<String>> send_msg(
            @Field("phone") String phone
    );

    //验证验证码
    @FormUrlEncoded
    @POST("check_code")
    Observable<HttpResult<String>> check_code(
            @Field("phone") String phone,
            @Field("code") String code
    );

    //账号注册
    @FormUrlEncoded
    @POST("register")
    Observable<HttpResult<String>> register(
            @Field("phone") String phone,
            @Field("username") String username,
            @Field("pwd") String pwd
    );

    //账号登录
    @FormUrlEncoded
    @POST("login")
    Observable<HttpResult<User>> login(
            @Field("phone") String phone,
            @Field("pwd") String pwd,
            @Field("registration_id") String registration_id
    );

    // 找回密码
    @FormUrlEncoded
    @POST("getback_pwd")
    Observable<HttpResult<String>> getBack_pwd(
            @Field("phone") String phone,
            @Field("new_pwd") String new_pwd
    );

    // 获取新闻的评论
    @FormUrlEncoded
    @POST("getanswers")
    Observable<HttpResult<Comments>> getAnswers(
            @Field("get_news_id") String news_id,
            @Field("page") int page
    );

    // 发送评论
    @FormUrlEncoded
    @POST("setanswers")
    Observable<HttpResult<String>> sendAnswers(
            @Field("news_id") String news_id,
            @Field("comments_authorid") String comments_author_id,
            @Field("comments_content") String comments_content
    );

    // 回复评论
    @FormUrlEncoded
    @POST("setanswers")
    Observable<HttpResult<String>> sendAnswers(
            @Field("news_id") String news_id,
            @Field("par_comments_id") String comments_id,
            @Field("comments_authorid") String comments_author_id,
            @Field("comments_content") String comments_content
    );

    // 获取图片新闻详情
    @FormUrlEncoded
    @POST("pics_detail")
    Observable<HttpResult<List<PicBean>>> pics_detail(
            @Field("news_id") String news_id
    );

    // 设置新闻收藏状态
    @FormUrlEncoded
    @POST("setcollect")
    Observable<HttpResult<String>> setCollect(
            @Field("user_id") String user_id,
            @Field("news_id") String news_id,
            @Field("type") int type
    );

    // 获取新闻收藏状态
    @FormUrlEncoded
    @POST("detail_collect_infos")
    Observable<HttpResult<Integer>> news_collect(
            @Field("user_id") String user_id,
            @Field("news_id") String news_id
    );

    // 获取bbs新闻
    @FormUrlEncoded
    @POST("getbbs")
    Observable<HttpResult<Bbs>> getBbs(
            @Field("type") int type,
            @Field("page") int page
    );

    // 发布bbs新闻
    @FormUrlEncoded
    @POST("setbbs")
    Observable<String> setBbs(
            @Field("bbs_title") String bbs_title,
            @Field("bbs_content") String bbs_content,
            @Field("user_id") String user_id,
            @Field("set_position") String set_position,
            @Field("imgs") String imgs
    );

    // 获取bbs新闻的系统评论
    @FormUrlEncoded
    @POST("get_sys_answers")
    Observable<HttpResult<List<BbsSystem>>> get_sys_answers(
            @Field("nid") String nid
    );

    // 获取bbs新闻的网友评论
    @FormUrlEncoded
    @POST("get_user_answers")
    Observable<HttpResult<BbsUser>> get_user_answers(
            @Field("nid") String nid,
            @Field("page") int page
    );

    // bbs新闻发布评论
    @FormUrlEncoded
    @POST("set_bbsanswers")
    Observable<HttpResult<String>> set_bbs_answers(
            @Field("user_id") String user_id,
            @Field("nid") String nid,
            @Field("user_answers") String user_answers
    );

    // 获取服务台的热线列表
    @POST("phonelist")
    Observable<HttpResult<HotLine>> getLines(

    );

    // 修改个人信息
    @FormUrlEncoded
    @POST("set_face")
    Observable<HttpResult<User>> set_face(
            @Field("user_id") String user_id,
            @Field("username") String username,
            @Field("sex") int sex,
            @Field("face_url") String face
    );

    // 服务台轮播图
    @POST("fwt_carousel")
    Observable<HttpResult<List<Fwt_Carousel>>> fwt_carousel(

    );

    // 社区活动列表
    @FormUrlEncoded
    @POST("Activity_list_data")
    Observable<HttpResult<Activity>> getActivity(
            @Field("page") int page,
            @Field("state") String state,
            @Field("address") String address,
            @Field("type_id") String type_id
    );

    // 社区活动筛选条件
    @POST("getactivity")
    Observable<HttpResult<Filter>> getFilter(

    );

    // 获取活动详情
    @FormUrlEncoded
    @POST("Activity")
    Observable<HttpResult<ActDetail>> getActDetail(
            @Field("user_id") String user_id,
            @Field("id") String id
    );

    // 提交新闻错误信息
    @FormUrlEncoded
    @POST("geterrors")
    Observable<HttpResult<String>> getErrors(
            @Field("news_id") String news_id,
            @Field("user_id") String user_id,
            @Field("error_list") String error_list,
            @Field("zdy_errors") String zdy_errors
    );

    // 活动报名
    @FormUrlEncoded
    @POST("signs")
    Observable<HttpResult<String>> signs(
            @Field("user_id") String user_id,
            @Field("activity_id") String activity_id,
            @Field("type") int type,
            @Field("simple_id") String simple_id
    );

    // 我的收藏
    @FormUrlEncoded
    @POST("my_collect")
    Observable<HttpResult<Collect>> my_collect(
            @Field("user_id") String user_id,
            @Field("page") int page
    );

    // 获取家庭信息
    @FormUrlEncoded
    @POST("tab_name_list ")
    Observable<HttpResult<List<Home>>> tab_name_list(
            @Field("user_id") String user_id
    );

    // 获取缴费信息
    @FormUrlEncoded
    @POST("account_tab")
    Observable<HttpResult<List<Expense>>> account_tab(
            @Field("id") String id
    );

    // 新增家庭信息
    @FormUrlEncoded
    @POST("account_add")
    Observable<HttpResult<String>> account_add(
            @Field("user_id") String user_id,
            @Field("tab_name") String tab_name,
            @Field("address") String address
    );

    // 更新家庭信息
    @FormUrlEncoded
    @POST("account_update")
    Observable<HttpResult<String>> account_update(
            @Field("id") String id,
            @Field("tab_name") String tab_name,
            @Field("address") String address

    );

    // 删除家庭信息
    @FormUrlEncoded
    @POST("account_delete")
    Observable<HttpResult<String>> account_delete(
            @Field("id") String id
    );


    // 意见反馈
    @POST("feedback")
    @FormUrlEncoded
    Observable<HttpResult<String>> feedback(
            @Field("user_id") String user_id,
            @Field("feedcontent") String feedcontent,
            @Field("error_name") String error_name,
            @Field("error_imgs") String error_imgs

    );

    // 取消我的收藏
    @POST("cancel_collect")
    @FormUrlEncoded
    Observable<HttpResult<String>> cancel_collect(
            @Field("user_id") String user_id,
            @Field("ids") String ids
    );

    // 公告列表
    @POST("notice_list")
    @FormUrlEncoded
    Observable<HttpResult<Notice>> notice_list(
            @Field("page") int page
    );

    // 修改密码
    @POST("modify_pwd")
    @FormUrlEncoded
    Observable<HttpResult<String>> modify_pwd(
            @Field("user_id") String user_id,
            @Field("old_pwd") String old_pwd,
            @Field("new_pwd") String new_pwd
    );

    // 消息列表
    @POST("my_message")
    @FormUrlEncoded
    Observable<HttpResult<Message>> my_message(
            @Field("user_id") String user_id,
            @Field("page") int page
    );

    // 我的跟帖
    @POST("myanswers")
    @FormUrlEncoded
    Observable<HttpResult<Answer>> my_answers(
            @Field("user_id") String user_id,
            @Field("page") int page
    );

    // 我的发表
    @POST("my_publish")
    @FormUrlEncoded
    Observable<HttpResult<Publish>> my_publish(
            @Field("user_id") String user_id,
            @Field("page") int page
    );

    // 已报名的人
    @POST("signscookie")
    @FormUrlEncoded
    Observable<HttpResult<List<SignedPerson>>> signs_person(
            @Field("user_id") String user_id,
            @Field("id") String id
    );

    // 取消审核
    @POST("canlcel_verify")
    @FormUrlEncoded
    Observable<HttpResult<String>> cancel_verify(
            @Field("id") String id,
            @Field("user_id") String user_id,
            @Field("simple_id") String simple_id
    );

    // 联系人列表
    @POST("contact_list")
    @FormUrlEncoded
    Observable<HttpResult<List<Contacts>>> contact_list(
            @Field("user_id") String user_id
    );

    // 添加联系人
    @POST("add_contact")
    @FormUrlEncoded
    Observable<HttpResult<String>> add_contact(
            @Field("user_id") String user_id,
            @Field("name") String name,
            @Field("phone") String phone,
            @Field("address") String address
    );

    // 删除联系人
    @POST("del_contact")
    @FormUrlEncoded
    Observable<HttpResult<String>> del_contact(
            @Field("simple_id") String simple_id
    );

    // 获取积分总数
    @POST("my_total_score")
    @FormUrlEncoded
    Observable<HttpResult<TotalScore>> getScore(
            @Field("user_id") String user_id
    );

    // 获取积分列表
    @POST("my_score")
    @FormUrlEncoded
    Observable<HttpResult<Score>> getScoreList(
            @Field("user_id") String user_id,
            @Field("page") int page
    );

    // 获取积分兑换礼品
    @POST("gift_list_data")
    @FormUrlEncoded
    Observable<HttpResult<Gift>> getGift(
            @Field("page") int page
    );

    // 兑换积分
    @POST("score_gift")
    @FormUrlEncoded
    Observable<HttpResult<String>> exchange_gift(
            @Field("user_id") String user_id,
            @Field("gift_id") String gift_id,
            @Field("rephone") String rephone,
            @Field("real_name") String real_name,
            @Field("address") String address
    );

    // 兑换记录
    @POST("gift_record")
    @FormUrlEncoded
    Observable<HttpResult<Gift>> gift_record(
            @Field("user_id") String user_id,
            @Field("page") int page
    );

    // 是否实名认证
    @POST("check_authorized")
    @FormUrlEncoded
    Observable<HttpResult<Integer>> check_authorized(
            @Field("user_id") String user_id
    );

    // 实名认证
    @POST("authorized")
    @FormUrlEncoded
    Observable<HttpResult<String>> authorized(
            @Field("user_id") String user_id,
            @Field("real_name") String real_name,
            @Field("card_id") String card_id
    );

    // 获取首页广告图
    @POST("getfirstimg")
    @FormUrlEncoded
    Observable<HttpResult<FirstImg>> getFirstImg(
            @Field("start_num") int start_num
    );

    // 获取首页广告图
    @POST("getfirstimg")
    @FormUrlEncoded
    Observable<HttpResult<AdvImg>> getAdvImg(
            @Field("start_num") int start_num
    );

    // 支付宝测试接口
    @POST("alipay")
    Observable<HttpResult<String>> alipay(

    );

    // 绑定表号
    @POST("bind_account")
    @FormUrlEncoded
    Observable<HttpResult<String>> bind_account(
            @Field("id") String id,
            @Field("type") int type,
            @Field("surface") String surface
    );

    // 获取欠费信息
    @POST("check_costs")
    @FormUrlEncoded
    Observable<HttpResult<Free>> check_costs(
            @Field("id") String id,
            @Field("type") int type
    );

    // 支付宝支付
    @POST("alipay")
    @FormUrlEncoded
    Observable<HttpResult<Alipay>> alipay(
            @Field("user_id") String user_id,
            @Field("id") String id,
            @Field("type") int type
    );

    //微信测试接口
    @POST("wxpay")
    @FormUrlEncoded
    Observable<HttpResult<WechatPay>> wxpay(
            @Field("user_id") String user_id,
            @Field("id") String id,
            @Field("type") int typ
    );

    // 关于我们
    @POST("about_us")
    @FormUrlEncoded
    Observable<HttpResult<Info>> getInfo(
            @Field("sysname") int sysname
    );

    // 我的活动
    @POST("my_activity")
    @FormUrlEncoded
    Observable<HttpResult<MyActivity>> my_activity(
            @Field("user_id") String user_id,
            @Field("page") int page
    );

    // 缴费记录
    @POST("jfjl")
    @FormUrlEncoded
    Observable<HttpResult<List<PayRecord>>> pay_record(
            @Field("user_id") String user_id
    );

    // 删除表号
    @POST("delete_surface")
    @FormUrlEncoded
    Observable<HttpResult<String>> delete_surface(
            @Field("id") String id,
            @Field("type") int type
    );

    // 公告列表
    @POST("notice_list")
    @FormUrlEncoded
    Observable<HttpResult<Notice>> search_notice(
            @Field("keywords") String keywords
    );

    // 点赞功能
    @POST("giveup")
    @FormUrlEncoded
    Observable<HttpResult<String>> giveup(
            @Field("user_id") String user_id,
            @Field("news_id") String news_id
    );

    // 获取点赞
    @POST("getup")
    @FormUrlEncoded
    Observable<HttpResult<Integer>> getup(
            @Field("news_id") String news_id
    );

    // 获取已报名的账号
    @POST("get_more_verify")
    @FormUrlEncoded
    Observable<HttpResult<Signed>> get_more_verify(
            @Field("id") String id,
            @Field("page") int page
    );

    // 新闻点击量
    @POST("news_pics_count")
    @FormUrlEncoded
    Observable<HttpResult<String>> news_pics_count(
            @Field("news_id") String news_id
    );

    // 获取收货地址
    @POST("get_receiver_info")
    @FormUrlEncoded
    Observable<HttpResult<List<Address>>> getAddress(
            @Field("user_id") String user_id
    );

    // 新增收货人
    @POST("set_receiver_info")
    @FormUrlEncoded
    Observable<HttpResult<String>> addAddress(
            @Field("user_id") String user_id,
            @Field("real_name") String real_name,
            @Field("rephone") String rephone,
            @Field("address") String address
    );

    // 删除收货人
    @POST("delete_receiver_info")
    @FormUrlEncoded
    Observable<HttpResult<String>> deleteAddress(
            @Field("connect_id") String connect_id
    );

    // 查询当月签到情况
    @POST("get_curmonth")
    @FormUrlEncoded
    Observable<HttpResult<List<Signin>>> get_curmonth(
            @Field("user_id") String user_id
    );

    // 签到
    @POST("signin")
    @FormUrlEncoded
    Observable<HttpResult<String>> signin(
            @Field("user_id") String user_id
    );

    // 获取当天签到情况
    @POST("get_today_sign")
    @FormUrlEncoded
    Observable<HttpResult<Integer>> get_today_sign(
            @Field("user_id") String user_id
    );

    // 获取bbs事件列表
    @POST("bbs_eventlist")
    @FormUrlEncoded
    Observable<Event> getBbsList(
            @Field("page") int page,
            @Field("bbs_pd_status") String status
    );

    // 获取400事件列表
    @POST("http://zhyl.yong-gang.com/zhyl/index.php/Home/Index/phone_eventlist")
    @FormUrlEncoded
    Observable<Event> getPhoneList(
            @Field("page") int page,
            @Field("phone_pd_status") String status
    );

    // 获取bbs事件详情
    @POST("bbs_event_detail")
    @FormUrlEncoded
    Observable<HttpResult<EventDetail>> getEventDetail(
            @Field("id") String id
    );

    // 获取400事件详情
    @POST("http://zhyl.yong-gang.com/zhyl/index.php/Home/Index/phone_detail")
    @FormUrlEncoded
    Observable<HttpResult<EventDetail>> getPhoneDetail(
            @Field("id") String id
    );

    // 获取搜索条件
    @GET("gird_serarch_data")
    Observable<HttpResult<Search>> getSearch(

    );

    // 获取事件数量
    @GET("event_count")
    Observable<HttpResult<MonitorModel.GridCount>> getCount(

    );

    // 获取事件列表
    @POST("eventlist")
    @FormUrlEncoded
    Observable<Event> getEventList(
            @Field("kind") int kind,
            @Field("status") String status,
            @Field("page") int page,
            @Field("stime") String stime,
            @Field("etime") String etime,
            @Field("bmid") String id
    );

    // 获取事件详情
    @POST("event_detail")
    @FormUrlEncoded
    Observable<HttpResult<EventDetail>> getEventDetail(
            @Field("id") String id,
            @Field("type") int type
    );

    // 网格化登录
    @POST("gird_login")
    @FormUrlEncoded
    Observable<HttpResult<GridUser>> grid_login(
            @Field("gird_username") String username,
            @Field("gird_pwd") String password
    );


    // 事件上报条件
    @GET("eventstatus")
    Observable<HttpResult<GridStatus>> getEventStatus(

    );

    // 事件上报
    @POST("addeventlist")
    @FormUrlEncoded
    Observable<HttpResult<String>> addEvent(
            @Field("czfa") String czfa,
            @Field("yzcd") String yzcd,
            @Field("sjfl") String sjfl,
            @Field("sqr") String sqr,
            @Field("sqrdh") String sqrdh,
            @Field("sjdw") String sjdw,
            @Field("sjdz") String sjdz,
            @Field("sswg") String sswg,
            @Field("sjms") String sjms,
            @Field("sjbt") String sjbt,
            @Field("xzqh") String xzqh,
            @Field("czlx") String czlx,
            @Field("sbrid") String id,
            @Field("imgs") String imgs
    );

    // 网格化事件列表
    @POST("gird_eventlist")
    @FormUrlEncoded
    Observable<GridEvent> getGridEvent(
            @Field("sbrid") String id,
            @Field("status") String status,
            @Field("page") int page
    );

    // 网格化事件详情
    @POST("gird_event_detail")
    @FormUrlEncoded
    Observable<HttpResult<GridEventDetail>> getGridEventDetail(
            @Field("id") String id
    );

    // 事件草稿提交
    @POST("gird_formal_event")
    @FormUrlEncoded
    Observable<HttpResult<String>> submitEvent(
            @Field("id") String id
    );

    // 身份信息获取
    @POST("get_rfxx")
    @FormUrlEncoded
    Observable<HttpResult<HouseInfo>> getHouseInfo(
            @Field("sfzh") String id
    );

    // 设置家庭成员信息
    @POST("set_jtcyxx")
    @FormUrlEncoded
    Observable<HttpResult<String>> setHouseFamily(
            @Field("rypk") String rypk,
            @Field("xm") String name,
            @Field("xb") String sex,
            @Field("sfzh") String id,
            @Field("csrq") String birth,
            @Field("gx") String relationship,
            @Field("lxfs") String phone,
            @Field("type") String type,
            @Field("zy") String job
    );

    // 获取家庭成员信息
    @POST("get_jtcyxx")
    @FormUrlEncoded
    Observable<HttpResult<List<HouseFamily>>> getHouseFamily(
            @Field("rypk") String pk,
            @Field("jzlx") int type
    );

    // 上传个人信息
    @FormUrlEncoded
    @POST("set_rfxx")
    Observable<HttpResult<String>> setHouseInfo(
            @Field("sfldrk") int is_floating,
            @Field("sfsy") int is_community,
            @Field("sfzh") String id,
            @Field("xm") String name,
            @Field("xb") String sex,
            @Field("csrq") String birth,
            @Field("gzdw") String job,
            @Field("mz") String nation,
            @Field("zzmm") String political,
            @Field("fwbm_pk") String address,
            @Field("lxdh") String phone,
            @Field("hyzk") String marriage,
            @Field("whcd") String education,
            @Field("hjdz") String permanent,
            @Field("hjbh") String permanentCode,

            @Field("sfyf") int is_special,
            @Field("sfcj") int is_disability,
            @Field("sfjsb") int is_mental,
            @Field("sfkc") int is_empty,
            @Field("sfdj") int is_alone,
            @Field("sfpk") int is_poor,
            @Field("sftf") int is_help,
            @Field("sfdbh") int is_low,
            @Field("sfxsm") int is_new,
            @Field("sffd") int is_landlord,

            @Field("bz") String disease,
            @Field("zyzzh") String volunteerId,
            @Field("fdlxdh") String landlordTel,
            @Field("cph") String carNumber,
            @Field("xqah") String hobby
    );
}
