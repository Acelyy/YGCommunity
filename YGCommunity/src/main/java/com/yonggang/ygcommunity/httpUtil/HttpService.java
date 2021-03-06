package com.yonggang.ygcommunity.httpUtil;


import com.yonggang.ygcommunity.Entry.ActDetail;
import com.yonggang.ygcommunity.Entry.Activity;
import com.yonggang.ygcommunity.Entry.Address;
import com.yonggang.ygcommunity.Entry.AdvImg;
import com.yonggang.ygcommunity.Entry.Alipay;
import com.yonggang.ygcommunity.Entry.Answer;
import com.yonggang.ygcommunity.Entry.Assignor;
import com.yonggang.ygcommunity.Entry.Bbs;
import com.yonggang.ygcommunity.Entry.BbsSystem;
import com.yonggang.ygcommunity.Entry.BbsUser;
import com.yonggang.ygcommunity.Entry.CheckDetails;
import com.yonggang.ygcommunity.Entry.Collect;
import com.yonggang.ygcommunity.Entry.Comments;
import com.yonggang.ygcommunity.Entry.Contacts;
import com.yonggang.ygcommunity.Entry.Depart;
import com.yonggang.ygcommunity.Entry.Event;
import com.yonggang.ygcommunity.Entry.EventDetail;
import com.yonggang.ygcommunity.Entry.Expense;
import com.yonggang.ygcommunity.Entry.Filter;
import com.yonggang.ygcommunity.Entry.FirstImg;
import com.yonggang.ygcommunity.Entry.Folk;
import com.yonggang.ygcommunity.Entry.FolkChoose;
import com.yonggang.ygcommunity.Entry.FolkDetails;
import com.yonggang.ygcommunity.Entry.Free;
import com.yonggang.ygcommunity.Entry.Fwt_Carousel;
import com.yonggang.ygcommunity.Entry.Garden;
import com.yonggang.ygcommunity.Entry.Gift;
import com.yonggang.ygcommunity.Entry.GridEvent;
import com.yonggang.ygcommunity.Entry.GridEventDetail;
import com.yonggang.ygcommunity.Entry.GridStatus;
import com.yonggang.ygcommunity.Entry.GridUser;
import com.yonggang.ygcommunity.Entry.Gztj;
import com.yonggang.ygcommunity.Entry.HcrwList;
import com.yonggang.ygcommunity.Entry.Home;
import com.yonggang.ygcommunity.Entry.HotLine;
import com.yonggang.ygcommunity.Entry.House;
import com.yonggang.ygcommunity.Entry.HouseFamily;
import com.yonggang.ygcommunity.Entry.HouseInfo;
import com.yonggang.ygcommunity.Entry.HouseQuery;
import com.yonggang.ygcommunity.Entry.HttpResult;
import com.yonggang.ygcommunity.Entry.Info;
import com.yonggang.ygcommunity.Entry.Message;
import com.yonggang.ygcommunity.Entry.MissionBean;
import com.yonggang.ygcommunity.Entry.MissionDetail;
import com.yonggang.ygcommunity.Entry.MyActivity;
import com.yonggang.ygcommunity.Entry.NewsItem;
import com.yonggang.ygcommunity.Entry.Notice;
import com.yonggang.ygcommunity.Entry.Notify;
import com.yonggang.ygcommunity.Entry.PayRecord;
import com.yonggang.ygcommunity.Entry.PicBean;
import com.yonggang.ygcommunity.Entry.Publish;
import com.yonggang.ygcommunity.Entry.Score;
import com.yonggang.ygcommunity.Entry.Search;
import com.yonggang.ygcommunity.Entry.Signed;
import com.yonggang.ygcommunity.Entry.SignedPerson;
import com.yonggang.ygcommunity.Entry.Signin;
import com.yonggang.ygcommunity.Entry.Swiper;
import com.yonggang.ygcommunity.Entry.Title;
import com.yonggang.ygcommunity.Entry.TotalScore;
import com.yonggang.ygcommunity.Entry.Trail;
import com.yonggang.ygcommunity.Entry.User;
import com.yonggang.ygcommunity.Entry.Version;
import com.yonggang.ygcommunity.Entry.Visit;
import com.yonggang.ygcommunity.Entry.VisitDetail;
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

    // 支付成功告知后台修改中间状态
    @POST("pay_mid_state")
    @FormUrlEncoded
    Observable<HttpResult<String>> sendMid(
            @Field("id") String id
    );

    // 微信支付主动查询订单状态
    @POST("wxpay_order_query")
    @FormUrlEncoded
    Observable<HttpResult<Boolean>> queryOrder(
            @Field("out_trade_no") String orderId,
            @Field("id") String id
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
            @Field("gird_pwd") String password,
            @Field("registration_id") String registration_id
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
//            @Field("xzqh") String xzqh,
//            @Field("czlx") String czlx,
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
            @Field("sfsy") int type
    );

    // 上传个人信息
    @FormUrlEncoded
    @POST("set_rfxx")
    Observable<HttpResult<String>> setHouseInfo(
            @Field("sfsy") int is_community,//1
            @Field("sfzh") String id,//2
            @Field("xm") String name,//3
            @Field("xb") String sex,//4
            @Field("csrq") String birth,//5
            @Field("gzdw") String job,//6
            @Field("mz") String nation,//7
            @Field("zzmm") String political,//8
            @Field("fwbm_pk") String address,//9
            @Field("lxdh") String phone,//10
            @Field("hyzk") String marriage,//11
            @Field("whcd") String education,//12
            @Field("hjdz") String permanent,//13
            @Field("hjbh") String permanentCode,//14

            @Field("sfyf") int is_special,//15
            @Field("sftf") int is_help,//16
            @Field("sfjsb") int is_mental,//17
            @Field("sfkc") int is_empty,//18
            @Field("sfdj") int is_alone,//19
            @Field("sfpk") int is_poor,//20
            @Field("sfdbh") int is_low,//22
            @Field("sfxsm") int is_new,//22
            @Field("sffd") int is_landlord,//23
            @Field("sfcj") int is_disability,//24
            @Field("cjdj") int disabilityLevel,//25

            @Field("bz") String disease,//26
            @Field("zyzzh") String volunteerId,//27
            @Field("fdlxdh") String landlordTel,//28
            @Field("cph") String carNumber,//29
            @Field("xqah") String hobby,//30
            @Field("sbrid") String sbrid//31
    );


    //上传头像
    @POST("upload_sfz_imgs")
    @FormUrlEncoded
    Observable<HttpResult<String>> setPhote(
            @Field("sfzh") String sfzh,
            @Field("imgs") String imgs
    );

    //获取人房列表
    @POST("get_rfxx_list")
    @FormUrlEncoded
    Observable<HttpResult<List<HouseInfo>>> getHouseList(
            @Field("page") int page,
            @Field("mg_id") String mg_id
    );

    //民情日志
    @FormUrlEncoded
    @POST("get_mqrj_list")
    Observable<HttpResult<List<Folk>>> getFolkList(
            @Field("page") int page
    );

    //提交民情日志
    @FormUrlEncoded
    @POST("set_mqrj")
    Observable<HttpResult<String>> setFolk(
            @Field("name") String name,
            @Field("dzbxxb_id") String dzbxxb_id,
            @Field("visit_place") String visit_place,
            @Field("interviewee") String interviewee,
            @Field("nature_id") String nature_id,
            @Field("content") String content,
            @Field("handling_process") String handling_process,
            @Field("result") String result,
            @Field("tag") String tag,
            @Field("input_person") String input_person,
            @Field("input_time") String input_time,
            @Field("visit_time") String visit_time,
            @Field("mqrj_imgs") String mqrj_imgs

    );

    //获取民情新增选项基础数据
    @GET("get_mqrj")
    Observable<FolkChoose> getFolkChoose(
    );

    //获取民情详情
    @FormUrlEncoded
    @POST("get_mqrj_detail")
    Observable<HttpResult<FolkDetails>> getFolkDetails(
            @Field("id") String id
    );

    // 获取园区列表
    @GET("get_yq_list")
    Observable<HttpResult<List<Garden>>> getGarden(

    );

    // 获取幢号
    @POST("get_lh_list")
    @FormUrlEncoded
    Observable<HttpResult<List<String>>> getBuilding(
            @Field("yqmc") String garden
    );

    // 获取楼号
    @POST("get_fh_list")
    @FormUrlEncoded
    Observable<HttpResult<List<House>>> getHouse(
            @Field("yqmc") String garden,
            @Field("lh") String building
    );

    // 获取工作统计
    @POST("drgztj")
    @FormUrlEncoded
    Observable<HttpResult<Gztj>> getGztj(
            @Field("mg_id") String mg_id
    );

    // 上报信访人员
    @POST("set_xfry_info")
    @FormUrlEncoded
    Observable<HttpResult<String>> setXfry(
            @Field("pcsj") String pcsj,
            @Field("zdry") String zdry,
            @Field("sswg") String sswg,
            @Field("mdlx") String mdlx,
            @Field("sjrs") String sjrs,
            @Field("wkcs") String wkcs,
            @Field("sbrid") String sbrid,
            @Field("swqk") String swqk,
            @Field("xwqk") String xwqk,
            @Field("telephone") String telephone,
            @Field("comment") String comment
    );

    // 获取信访人员
    @POST("get_xfry_info")
    @FormUrlEncoded
    Observable<HttpResult<List<Visit>>> getXfry(
            @Field("pcsj") String pcsj,
            @Field("sbrid") String sbrid
    );

    // 获取信访人员详情
    @POST("get_xfry_detail")
    @FormUrlEncoded
    Observable<HttpResult<VisitDetail>> getXfryDetails(
            @Field("id") String id
    );

    // 更新信访人员详情
    @POST("set_ups_xfry")
    @FormUrlEncoded
    Observable<HttpResult<String>> upXfryDetails(
            @Field("id") String id,
            @Field("pcsj") String pcsj,
            @Field("zdry") String zdry,
            @Field("sswg") String sswg,
            @Field("mdlx") String mdlx,
            @Field("sjrs") String sjrs,
            @Field("wkcs") String wkcs,
            @Field("sbrid") String sbrid,
            @Field("swqk") String swqk,
            @Field("xwqk") String xwqk,
            @Field("telephone") String telephone,
            @Field("comment") String comment
    );

    // 核查任务
    @POST("get_hcrw_list")
    @FormUrlEncoded
    Observable<HttpResult<List<HcrwList>>> getHcrw(
            @Field("page") int page,
            @Field("sbrid") String sbrid
    );

    //获取工单处理列表
    @POST("get_gdcl_list")
    @FormUrlEncoded
    Observable<HttpResult<List<MissionBean>>> getMissionList(
            @Field("appauth") int appauth,
            @Field("sbrid") String id
    );

    //获取事件核查列表详情
    @POST("get_hcrw_detail")
    @FormUrlEncoded
    Observable<HttpResult<CheckDetails>> getTaskDetails(
            @Field("id") String id
    );

    //提交事件核查列表详情
    @POST("app_wgy_end_event")
    @FormUrlEncoded
    Observable<HttpResult<String>> setTaskDetails(
            @Field("id") String id,
            @Field("sbrid") String sbrid,
            @Field("imgs") String imgs,
            @Field("comment") String comment
    );

    // 获取工单详情
    @POST("get_eventlist_detail")
    @FormUrlEncoded
    Observable<HttpResult<MissionDetail>> getMissionDetail(
            @Field("id") String id,
            @Field("mg_id") String mg_id
    );

    // 事件签收
    @POST("app_event_qs")
    @FormUrlEncoded
    Observable<HttpResult<String>> signEvent(
            @Field("id") String id,
            @Field("sbrid") String sbrid,
            @Field("appauth") int appauth
    );

    // 事件完结
    @POST("app_end_event")
    @FormUrlEncoded
    Observable<HttpResult<String>> endEvent(
            @Field("id") String id,
            @Field("sbrid") String sbrid,
            @Field("appauth") int appauth
    );

    // 发出核查通知
    @POST("app_event_hctz")
    @FormUrlEncoded
    Observable<HttpResult<String>> sendCheck(
            @Field("id") String id,
            @Field("sbrid") String sbrid,
            @Field("comment") String comment
    );

    // 获取转派部门的列表
    @GET("get_bm_list")
    Observable<HttpResult<List<Depart>>> getDepartList(

    );

    // 获取指派人列表
    @POST("get_bm_person")
    @FormUrlEncoded
    Observable<HttpResult<List<Assignor>>> getAssignorList(
            @Field("mg_id") String id
    );

    // 转派部门
    @POST("app_bm_event")
    @FormUrlEncoded
    Observable<HttpResult<String>> transfer(
            @Field("id") String id,
            @Field("bmids") String ids,
            @Field("comment") String comment
    );

    // 任务指派
    @POST("app_person_event")
    @FormUrlEncoded
    Observable<HttpResult<String>> assignor(
            @Field("mg_id") String mg_id,
            @Field("id") String id,
            @Field("perids") String ids,
            @Field("comment") String comment
    );

    // 获取轮播背景
    @GET("app_gird_lbt")
    Observable<HttpResult<List<Swiper>>> getSwiper(

    );

    // 获取事件轨迹
    @POST("app_event_gj")
    @FormUrlEncoded
    Observable<HttpResult<List<Trail>>> getTrail(
            @Field("id") String id
    );

    //消息通知的列表
    @GET("gird_get_jlist")
    Observable<HttpResult<List<Notify>>> getNotify(

    );

    //人房查询
    @POST("set_rfxx_tj")
    @FormUrlEncoded
    Observable<HttpResult<List<HouseQuery>>> getHouseQuery(
            @Field("xm") String xm,
            @Field("lxdh") String lxdh,
            @Field("sfzh") String sfzh,
            @Field("cph") String cph,
            @Field("page") int page

    );
}
