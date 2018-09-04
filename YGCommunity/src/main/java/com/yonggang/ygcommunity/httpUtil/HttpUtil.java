package com.yonggang.ygcommunity.httpUtil;

import android.util.Log;

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
import com.yonggang.ygcommunity.Entry.EventDetail;
import com.yonggang.ygcommunity.Entry.Expense;
import com.yonggang.ygcommunity.Entry.Filter;
import com.yonggang.ygcommunity.Entry.FirstImg;
import com.yonggang.ygcommunity.Entry.Folk;
import com.yonggang.ygcommunity.Entry.FolkDetails;
import com.yonggang.ygcommunity.Entry.Free;
import com.yonggang.ygcommunity.Entry.Fwt_Carousel;
import com.yonggang.ygcommunity.Entry.Garden;
import com.yonggang.ygcommunity.Entry.Gift;
import com.yonggang.ygcommunity.Entry.GridEventDetail;
import com.yonggang.ygcommunity.Entry.GridStatus;
import com.yonggang.ygcommunity.Entry.GridUser;
import com.yonggang.ygcommunity.Entry.Home;
import com.yonggang.ygcommunity.Entry.HotLine;
import com.yonggang.ygcommunity.Entry.House;
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
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by liyangyang on 2017/3/22.
 */

public class HttpUtil {

    public static final String BASE_URL = "http://zhyl.yong-gang.com/zhyl/Home/Index/";
//    public static String BASE_URL = "http://10.89.12.97/zhyl/Home/Index/";
//    public static String BASE_URL = "http://10.89.12.97/zhyl/index.php/Home/Index/";
//    public static final String BASE_URL = "http://10.89.13.157:9857/";

    public static int DEFAULT_TIMEOUT = 5;

    private Retrofit retrofit;

    private HttpService httpService;

    private static HttpUtil INSTANCE = new HttpUtil();

    private HttpUtil() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);//设置超时时间
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(clientBuilder.build())
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        httpService = retrofit.create(HttpService.class);
    }

    public static HttpUtil getInstance() {
        return INSTANCE;
    }

    /**
     * 用来统一处理Http的flag,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    public static class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {

        @Override
        public T call(HttpResult<T> httpResult) {
            Log.i("result", httpResult.toString());
            if (httpResult.getFlag() == 0) {
                String msg = httpResult.getMsg();
                if (msg == null) {
                    msg = "msg为空";
                }
                throw new RuntimeException(msg);
            }
            return httpResult.getData();
        }
    }

    /**
     * 统一配置观察者
     *
     * @param o
     * @param <T>
     */
    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

    /**
     * 获取新闻标题列表
     *
     * @param subscriber
     */
    public void getCategory_list(Subscriber<List<Title>> subscriber) {
        Observable observable = httpService.category_list()
                .map(new HttpResultFunc<List<Title>>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取单个类型新闻列表
     *
     * @param subscriber
     * @param cid
     * @param ctype
     * @param page
     * @param user_id
     */
    public void getCList(Subscriber<NewsItem> subscriber, String cid, int ctype, int page, String user_id) {
        Observable observable = httpService.c_list(cid, ctype, page, user_id)
                .map(new HttpResultFunc<NewsItem>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 发送验证码
     *
     * @param subscriber
     * @param phone
     */
    public void send_ums(Subscriber<String> subscriber, String phone) {
        Observable observable = httpService.send_msg(phone)
                .map(new HttpResultFunc<String>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 验证验证码
     *
     * @param subscriber
     * @param phone
     * @param code
     */
    public void check_code(Subscriber<String> subscriber, String phone, String code) {
        Observable observable = httpService.check_code(phone, code)
                .map(new HttpResultFunc<String>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 注册账号
     *
     * @param subscriber
     * @param phone
     * @param username
     * @param pwd
     */
    public void register(Subscriber<String> subscriber, String phone, String username, String pwd) {
        Observable observable = httpService.register(phone, username, pwd)
                .map(new HttpResultFunc<String>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 账号登录
     *
     * @param subscriber
     * @param phone
     * @param pwd
     */
    public void login(Subscriber<User> subscriber, String phone, String pwd, String registration_id) {
        Observable observable = httpService.login(phone, pwd, registration_id)
                .map(new HttpResultFunc<User>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 找回密码
     *
     * @param subscriber
     * @param phone
     * @param new_wd
     */

    public void getBack_pwd(Subscriber<HttpResult<String>> subscriber, String phone, String new_wd) {
        Observable observable = httpService.getBack_pwd(phone, new_wd)
                .map(new HttpResultFunc<String>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取新闻的评论
     *
     * @param subscriber
     * @param news_id
     */
    public void getAnswers(Subscriber<Comments> subscriber, String news_id, int page) {
        Observable observable = httpService.getAnswers(news_id, page)
                .map(new HttpResultFunc<Comments>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 发送新闻评论
     *
     * @param subscriber
     * @param news_id
     * @param comments_author_id
     * @param comments_content
     */
    public void sendAnswer(Subscriber<String> subscriber, String news_id, String comments_author_id, String comments_content) {
        Observable observable = httpService.sendAnswers(news_id, comments_author_id, comments_content)
                .map(new HttpResultFunc<String>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 回复新闻评论
     *
     * @param subscriber
     * @param news_id
     * @param comments_id
     * @param comments_author_id
     * @param comments_content
     */
    public void sendAnswer(Subscriber<String> subscriber, String news_id, String comments_id, String comments_author_id, String comments_content) {
        Observable observable = httpService.sendAnswers(news_id, comments_id, comments_author_id, comments_content)
                .map(new HttpResultFunc<String>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取图片新闻详细
     *
     * @param subscriber
     * @param news_id
     */
    public void pics_detail(Subscriber<List<PicBean>> subscriber, String news_id) {
        Observable observable = httpService.pics_detail(news_id)
                .map(new HttpResultFunc<List<PicBean>>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 新闻点赞
     *
     * @param subscriber
     * @param user_id
     * @param news_id
     */
    public void setCollect(Subscriber<String> subscriber, String user_id, String news_id, int type) {
        Observable observable = httpService.setCollect(user_id, news_id, type)
                .map(new HttpResultFunc<String>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取新闻点赞
     *
     * @param subscriber
     * @param user_id
     * @param news_id
     */
    public void news_collect(Subscriber<Integer> subscriber, String user_id, String news_id) {
        Observable observable = httpService.news_collect(user_id, news_id)
                .map(new HttpResultFunc<Integer>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取BBS新闻
     *
     * @param subscriber
     * @param type
     * @param page
     */
    public void getBbs(Subscriber<Bbs> subscriber, int type, int page) {
        Observable observable = httpService.getBbs(type, page)
                .map(new HttpResultFunc<Bbs>());
        toSubscribe(observable, subscriber);

    }

    /**
     * 发布BBS新闻
     *
     * @param subscriber
     * @param bbs_title
     * @param bbs_content
     * @param user_id
     * @param imgs
     */
    public void setBbs(Subscriber subscriber, String bbs_title, String bbs_content, String user_id, String set_position, String imgs) {
        Observable observable = httpService.setBbs(bbs_title, bbs_content, user_id, set_position, imgs);
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取Bbs的系统评论
     *
     * @param subscriber
     * @param nid
     */
    public void get_sys_answers(Subscriber<List<BbsSystem>> subscriber, String nid) {
        Observable observable = httpService.get_sys_answers(nid)
                .map(new HttpResultFunc<List<BbsSystem>>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取Bbs的评论
     *
     * @param subscriber
     * @param nid
     */
    public void get_user_answers(Subscriber<BbsUser> subscriber, String nid, int page) {
        Observable observable = httpService.get_user_answers(nid, page)
                .map(new HttpResultFunc<BbsUser>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 发布bbs新闻评论
     *
     * @param subscriber
     * @param user_id
     * @param nid
     * @param user_answers
     */
    public void set_bbs_answer(Subscriber<String> subscriber, String user_id, String nid, String user_answers) {
        Observable observable = httpService.set_bbs_answers(user_id, nid, user_answers)
                .map(new HttpResultFunc<String>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取热线列表
     *
     * @param subscriber
     */
    public void getLines(Subscriber<HotLine> subscriber) {
        Observable observable = httpService.getLines()
                .map(new HttpResultFunc<HotLine>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 设置个人信息
     *
     * @param subscriber
     * @param user_id
     * @param username
     * @param sex
     * @param face
     */
    public void setFace(Subscriber<User> subscriber, String user_id, String username, int sex, String face) {
        Observable observable = httpService.set_face(user_id, username, sex, face)
                .map(new HttpResultFunc<User>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 服务台的轮播图
     *
     * @param subscriber
     */
    public void fwt_carousel(Subscriber<List<Fwt_Carousel>> subscriber) {
        Observable observable = httpService.fwt_carousel()
                .map(new HttpResultFunc<List<Fwt_Carousel>>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 社区活动列表
     *
     * @param subscriber
     * @param page
     */
    public void getActivity(Subscriber<Activity> subscriber, int page, String state, String address, String type_id) {
        Observable observable = httpService.getActivity(page, state, address, type_id)
                .map(new HttpResultFunc<Activity>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取筛选条件
     *
     * @param subscriber
     */
    public void getFilter(Subscriber<Filter> subscriber) {
        Observable observable = httpService.getFilter()
                .map(new HttpResultFunc<Filter>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取活动详情
     *
     * @param subscriber
     * @param id
     */
    public void getActDetail(Subscriber<ActDetail> subscriber, String user_id, String id) {
        Observable observable = httpService.getActDetail(user_id, id)
                .map(new HttpResultFunc<ActDetail>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 提交新闻错误
     *
     * @param subscriber
     * @param news_id
     * @param user_id
     * @param error_list
     * @param zdy_errors
     */
    public void getError(Subscriber<String> subscriber, String news_id, String user_id, String error_list, String zdy_errors) {
        Observable observable = httpService.getErrors(news_id, user_id, error_list, zdy_errors)
                .map(new HttpResultFunc<String>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 活动报名
     *
     * @param subscriber
     * @param user_id
     * @param activity_id
     * @param simple_id
     */
    public void signs(Subscriber<String> subscriber, String user_id, String activity_id, String simple_id) {
        Observable observable = httpService.signs(user_id, activity_id, 0, simple_id)
                .map(new HttpResultFunc<String>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 我的收藏
     *
     * @param subscriber
     * @param user_id
     * @param page
     */
    public void my_collect(Subscriber<Collect> subscriber, String user_id, int page) {
        Observable observable = httpService.my_collect(user_id, page)
                .map(new HttpResultFunc<Collect>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取版本
     *
     * @param subscriber
     */
    public void get_version(Subscriber<Version> subscriber) {
        Observable observable = httpService.get_version()
                .map(new HttpResultFunc<Version>());
        toSubscribe(observable, subscriber);

    }

    /**
     * 生活缴费家庭列表
     *
     * @param subscriber
     * @param user_id
     */
    public void tab_name_list(Subscriber<List<Home>> subscriber, String user_id) {
        Observable observable = httpService.tab_name_list(user_id)
                .map(new HttpResultFunc<List<Home>>());
        toSubscribe(observable, subscriber);

    }

    /**
     * 获取家庭缴费信息
     *
     * @param subscriber
     * @param id
     */
    public void account_tab(Subscriber<List<Expense>> subscriber, String id) {
        Observable observable = httpService.account_tab(id)
                .map(new HttpResultFunc<List<Expense>>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 新增家庭信息
     *
     * @param subscriber
     * @param user_id
     * @param tab_name
     * @param address
     */
    public void account_add(Subscriber<String> subscriber, String user_id, String tab_name, String address) {
        Observable observable = httpService.account_add(user_id, tab_name, address)
                .map(new HttpResultFunc<String>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 更新家庭信息
     *
     * @param subscriber
     * @param id
     * @param tab_name
     * @param address
     */
    public void account_update(Subscriber<String> subscriber, String id, String tab_name, String address) {
        Observable observable = httpService.account_update(id, tab_name, address)
                .map(new HttpResultFunc<String>());
        toSubscribe(observable, subscriber);
    }


    /**
     * 删除家庭信息
     *
     * @param subscriber
     * @param id
     */
    public void account_delete(Subscriber<String> subscriber, String id) {
        Observable observable = httpService.account_delete(id)
                .map(new HttpResultFunc<String>());
        toSubscribe(observable, subscriber);
    }


    /**
     * 我要报错
     *
     * @param subscriber
     * @param user_id
     * @param feedcontent
     * @param error_name
     * @param error_imgs
     */
    public void feedback(Subscriber<String> subscriber, String user_id, String feedcontent, String error_name, String error_imgs) {
        Observable observable = httpService.feedback(user_id, feedcontent, error_name, error_imgs)
                .map(new HttpResultFunc<String>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 取消收藏
     *
     * @param subscriber
     * @param user_id
     * @param ids
     */
    public void cancel_collect(Subscriber<String> subscriber, String user_id, String ids) {
        Observable observable = httpService.cancel_collect(user_id, ids)
                .map(new HttpResultFunc<String>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 公告列表
     *
     * @param subscriber
     * @param page
     */
    public void notice_list(Subscriber<Notice> subscriber, int page) {
        Observable observable = httpService.notice_list(page)
                .map(new HttpResultFunc<Notice>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 修改密码
     *
     * @param subscriber
     * @param user_id
     * @param old_pwd
     * @param new_pwd
     */
    public void modify_pwd(Subscriber<String> subscriber, String user_id, String old_pwd, String new_pwd) {
        Observable observable = httpService.modify_pwd(user_id, old_pwd, new_pwd)
                .map(new HttpResultFunc<String>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 我的消息
     *
     * @param subscriber
     * @param user_id
     * @param page
     */
    public void my_message(Subscriber<Message> subscriber, String user_id, int page) {
        Observable observable = httpService.my_message(user_id, page)
                .map(new HttpResultFunc<Message>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 我的跟帖
     *
     * @param subscriber
     * @param user_id
     * @param page
     */
    public void my_answers(Subscriber<Answer> subscriber, String user_id, int page) {
        Observable observable = httpService.my_answers(user_id, page)
                .map(new HttpResultFunc<Answer>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 我的发表
     *
     * @param subscriber
     * @param user_id
     * @param page
     */
    public void my_publish(Subscriber<Publish> subscriber, String user_id, int page) {
        Observable observable = httpService.my_publish(user_id, page)
                .map(new HttpResultFunc<Publish>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 已报名人
     *
     * @param subscriber
     * @param user_id
     */
    public void signs_person(Subscriber<List<SignedPerson>> subscriber, String user_id, String id) {
        Observable observable = httpService.signs_person(user_id, id)
                .map(new HttpResultFunc<List<SignedPerson>>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 取消报名
     *
     * @param subscriber
     * @param id
     * @param user_id
     * @param simple_id
     */
    public void cancel_verify(Subscriber<String> subscriber, String id, String user_id, String simple_id) {
        Observable observable = httpService.cancel_verify(id, user_id, simple_id)
                .map(new HttpResultFunc<String>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 联系人列表
     *
     * @param subscriber
     * @param user_id
     */
    public void contact_list(Subscriber<List<Contacts>> subscriber, String user_id) {
        Observable observable = httpService.contact_list(user_id)
                .map(new HttpResultFunc<List<Contacts>>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 添加联系人
     *
     * @param subscriber
     * @param user_id
     * @param name
     * @param phone
     * @param address
     */
    public void add_contact(Subscriber<String> subscriber, String user_id, String name, String phone, String address) {
        Observable observable = httpService.add_contact(user_id, name, phone, address)
                .map(new HttpResultFunc<String>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 删除联系人
     *
     * @param subscriber
     * @param simple_id
     */
    public void del_contact(Subscriber<String> subscriber, String simple_id) {
        Observable observable = httpService.del_contact(simple_id)
                .map(new HttpResultFunc<String>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取积分总数
     *
     * @param subscriber
     * @param user_id
     */
    public void getScore(Subscriber<TotalScore> subscriber, String user_id) {
        Observable observable = httpService.getScore(user_id)
                .map(new HttpResultFunc<TotalScore>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取积分详细列表
     *
     * @param subscriber
     * @param user_id
     * @param page
     */
    public void getScoreList(Subscriber<Score> subscriber, String user_id, int page) {
        Observable observable = httpService.getScoreList(user_id, page)
                .map(new HttpResultFunc<Score>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取积分兑换礼品
     *
     * @param subscriber
     */
    public void getGift(Subscriber<Gift> subscriber, int page) {
        Observable observable = httpService.getGift(page)
                .map(new HttpResultFunc<Gift>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 兑换商品
     *
     * @param subscriber
     * @param user_id
     * @param gift_id
     */
    public void exchange_gift(Subscriber<String> subscriber, String user_id, String gift_id, String rephone, String real_name, String address) {
        Observable observable = httpService.exchange_gift(user_id, gift_id, rephone, real_name, address)
                .map(new HttpResultFunc<String>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 兑换记录
     *
     * @param subscriber
     * @param user_id
     * @param page
     */
    public void gift_record(Subscriber<Gift> subscriber, String user_id, int page) {
        Observable observable = httpService.gift_record(user_id, page)
                .map(new HttpResultFunc<Gift>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 是否实名认证
     *
     * @param subscriber
     * @param user_id
     */
    public void check_authorized(Subscriber<Integer> subscriber, String user_id) {
        Observable observable = httpService.check_authorized(user_id)
                .map(new HttpResultFunc<Integer>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 实名认证
     *
     * @param subscriber
     */
    public void authorized(Subscriber<String> subscriber, String user_id, String real_name, String card_id) {
        Observable observable = httpService.authorized(user_id, real_name, card_id)
                .map(new HttpResultFunc<String>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取首页广告图
     *
     * @param subscriber
     */
    public void getFirstImg(Subscriber<FirstImg> subscriber, int start_num) {
        Observable observable = httpService.getFirstImg(start_num)
                .map(new HttpResultFunc<FirstImg>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取首页广告图
     *
     * @param subscriber
     */
    public void getAdvImg(Subscriber subscriber, int start_num) {
        Observable observable = httpService.getAdvImg(start_num)
                .map(new HttpResultFunc<AdvImg>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 支付宝测试接口
     *
     * @param subscriber
     */
    public void alipay(Subscriber<String> subscriber) {
        Observable observable = httpService.alipay()
                .map(new HttpResultFunc<String>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 绑定表号
     *
     * @param subscriber
     * @param id
     * @param type
     * @param surface
     */
    public void bind_account(Subscriber subscriber, String id, int type, String surface) {
        Observable observable = httpService.bind_account(id, type, surface)
                .map(new HttpResultFunc<String>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 查询欠费
     *
     * @param subscriber
     * @param id
     * @param type
     */
    public void check_costs(Subscriber subscriber, String id, int type) {
        Observable observable = httpService.check_costs(id, type)
                .map(new HttpResultFunc<Free>());
        toSubscribe(observable, subscriber);
    }


    /**
     * 支付宝支付
     *
     * @param subscriber
     * @param user_id
     * @param id
     * @param type
     */
    public void alipay(Subscriber subscriber, String user_id, String id, int type) {
        Observable observable = httpService.alipay(user_id, id, type)
                .map(new HttpResultFunc<Alipay>());
        toSubscribe(observable, subscriber);
    }


    /**
     * 微信支付
     *
     * @param subscriber
     * @param user_id
     * @param id
     * @param type
     */
    public void wxpay(Subscriber<WechatPay> subscriber, String user_id, String id, int type) {
        Observable observable = httpService.wxpay(user_id, id, type)
                .map(new HttpResultFunc<WechatPay>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 关于我们
     *
     * @param subscriber
     */
    public void getInfo(Subscriber subscriber, int sysname) {
        Observable observable = httpService.getInfo(sysname)
                .map(new HttpResultFunc<Info>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 我的活动
     *
     * @param subscriber
     * @param user_id
     * @param page
     */
    public void my_activity(Subscriber subscriber, String user_id, int page) {
        Observable observable = httpService.my_activity(user_id, page)
                .map(new HttpResultFunc<MyActivity>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 水电费缴费记录
     *
     * @param subscriber
     * @param user_id
     */
    public void pay_record(Subscriber subscriber, String user_id) {
        Observable observable = httpService.pay_record(user_id)
                .map(new HttpResultFunc<List<PayRecord>>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 删除表号
     *
     * @param subscriber
     * @param id
     * @param type
     */
    public void delete_surface(Subscriber subscriber, String id, int type) {
        Observable observable = httpService.delete_surface(id, type)
                .map(new HttpResultFunc<String>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 公告搜索
     *
     * @param subscriber
     * @param keywords
     */
    public void search_notice(Subscriber subscriber, String keywords) {
        Observable observable = httpService.search_notice(keywords)
                .map(new HttpResultFunc<Notice>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 新闻点赞
     *
     * @param subscriber
     * @param user_id
     * @param news_id
     */
    public void zan(Subscriber subscriber, String user_id, String news_id) {
        Observable observable = httpService.giveup(user_id, news_id)
                .map(new HttpResultFunc<String>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取新闻赞
     *
     * @param subscriber
     * @param news_id
     */
    public void getZan(Subscriber subscriber, String news_id) {
        Observable observable = httpService.getup(news_id)
                .map(new HttpResultFunc<Integer>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取活动已报名的人
     *
     * @param subscriber
     * @param id
     * @param page
     */
    public void get_more_verify(Subscriber subscriber, String id, int page) {
        Observable observable = httpService.get_more_verify(id, page)
                .map(new HttpResultFunc<Signed>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 新闻增加阅读量
     *
     * @param subscriber
     * @param news_id
     */
    public void news_pics_count(Subscriber subscriber, String news_id) {
        Observable observable = httpService.news_pics_count(news_id)
                .map(new HttpResultFunc<String>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取收货地址
     *
     * @param subscriber
     * @param user_id
     */
    public void getAddress(Subscriber subscriber, String user_id) {
        Observable observable = httpService.getAddress(user_id)
                .map(new HttpResultFunc<List<Address>>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 新增收货人
     *
     * @param subscriber
     * @param user_id
     * @param real_name
     * @param rephone
     * @param address
     */
    public void addAddress(Subscriber subscriber, String user_id, String real_name, String rephone, String address) {
        Observable observable = httpService.addAddress(user_id, real_name, rephone, address)
                .map(new HttpResultFunc<String>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 删除联系人
     *
     * @param subscriber
     * @param connect_id
     */
    public void deleteAddress(Subscriber subscriber, String connect_id) {
        Observable observable = httpService.deleteAddress(connect_id)
                .map(new HttpResultFunc<String>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取当月签到
     *
     * @param subscriber
     * @param user_id
     */
    public void get_curmonth(Subscriber subscriber, String user_id) {
        Observable observable = httpService.get_curmonth(user_id)
                .map(new HttpResultFunc<List<Signin>>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 签到
     *
     * @param subscriber
     * @param user_id
     */
    public void signin(Subscriber subscriber, String user_id) {
        Observable observable = httpService.signin(user_id)
                .map(new HttpResultFunc<String>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取当日签到情况
     *
     * @param subscriber
     * @param user_id
     */
    public void get_today_sign(Subscriber subscriber, String user_id) {
        Observable observable = httpService.get_today_sign(user_id)
                .map(new HttpResultFunc<Integer>());
        toSubscribe(observable, subscriber);
    }


    /**
     * 获取搜索条件
     *
     * @param subscriber
     */
    public void getSearch(Subscriber subscriber) {
        Observable observable = httpService.getSearch()
                .map(new HttpResultFunc<Search>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取数量
     *
     * @param subscriber
     */
    public void getCount(Subscriber subscriber) {
        Observable observable = httpService.getCount()
                .map(new HttpResultFunc<MonitorModel.GridCount>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取事件列表
     *
     * @param subscriber
     * @param kind
     * @param status
     * @param page
     */
    public void getEventList(Subscriber subscriber, int kind, String status, int page, String stime, String etime, String id) {
        Observable observable = httpService.getEventList(kind, status, page, stime, etime, id);
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取事件详情
     *
     * @param subscriber
     * @param id
     * @param type
     */
    public void getEventDetail(Subscriber subscriber, String id, int type) {
        Observable observable = httpService.getEventDetail(id, type)
                .map(new HttpResultFunc<EventDetail>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 网格化登录
     *
     * @param subscriber
     * @param username
     * @param password
     */
    public void grid_login(Subscriber subscriber, String username, String password) {
        Observable observable = httpService.grid_login(username, password)
                .map(new HttpResultFunc<GridUser>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取上报事件的筛选
     *
     * @param subscriber
     */
    public void getEventStatus(Subscriber subscriber) {
        Observable observable = httpService.getEventStatus()
                .map(new HttpResultFunc<GridStatus>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 上报事件
     *
     * @param subscriber
     * @param czfa
     * @param yzcd
     * @param sjfl
     * @param sqr
     * @param sqrdh
     * @param sjdw
     * @param sjdz
     * @param sjms
     * @param sjbt
     * @param xzqh
     * @param czlx
     * @param imgs
     */
    public void addEvent(Subscriber subscriber, String czfa, String yzcd, String sjfl, String sqr, String sqrdh, String sjdw, String sjdz, String sswg, String sjms, String sjbt, String xzqh, String czlx, String id, String imgs) {
        Observable observable = httpService.addEvent(czfa, yzcd, sjfl, sqr, sqrdh, sjdw, sjdz, sswg, sjms, sjbt, xzqh, czlx, id, imgs)
                .map(new HttpResultFunc<String>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取网格化事件列表
     *
     * @param subscriber
     * @param id
     * @param status
     */
    public void getGridEvent(Subscriber subscriber, String id, String status, int page) {
        Observable observable = httpService.getGridEvent(id, status, page);
//                .map(new HttpResultFunc<String>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取网格化事件详情
     *
     * @param subscriber
     * @param id
     */
    public void getGridEventDetail(Subscriber subscriber, String id) {
        Observable observable = httpService.getGridEventDetail(id)
                .map(new HttpResultFunc<GridEventDetail>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 提交网格化草稿事件
     *
     * @param subscriber
     * @param id
     */
    public void submitEvent(Subscriber subscriber, String id) {
        Observable observable = httpService.submitEvent(id)
                .map(new HttpResultFunc<String>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取人房信息
     *
     * @param subscriber
     * @param id
     */
    public void getHouseInfo(Subscriber subscriber, String id) {
        Observable observable = httpService.getHouseInfo(id)
                .map(new HttpResultFunc<HouseInfo>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取家庭成员信息
     *
     * @param subscriber
     * @param pk
     * @param type
     */
    public void getHouseFamily(Subscriber subscriber, String pk, int type) {
        Observable observable = httpService.getHouseFamily(pk, type)
                .map(new HttpResultFunc<List<HouseFamily>>());
        toSubscribe(observable, subscriber);
    }

    /**
     * @param subscriber
     * @param rypk
     * @param name
     * @param sex
     * @param id
     * @param relationship
     * @param phone
     * @param type
     * @param job
     */
    public void setHouseFamily(Subscriber subscriber, String rypk, String name, String sex, String id, String birth, String relationship, String phone, String type, String job) {
        Observable observable = httpService.setHouseFamily(rypk, name, sex, id, birth, relationship, phone, type, job)
                .map(new HttpResultFunc<String>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 上传个人信息
     *
     * @param subscriber
     * @param is_community
     * @param id
     * @param name
     * @param sex
     * @param birth
     * @param job
     * @param nation
     * @param political
     * @param address
     * @param phone
     * @param marriage
     * @param education
     * @param permanent
     * @param permanentCode
     * @param is_special
     * @param is_disability
     * @param disabilityLevel
     * @param is_mental
     * @param is_empty
     * @param is_alone
     * @param is_poor
     * @param is_help
     * @param is_low
     * @param is_new
     * @param is_landlord
     * @param disease
     * @param volunteerId
     * @param landlordTel
     * @param carNumber
     * @param hobby
     */
    public void setHouseInfo(Subscriber subscriber, int is_community, String id, String name, String sex, String birth, String job, String nation, String political, String address, String phone, String marriage, String education, String permanent, String permanentCode, int is_special, int is_disability, int disabilityLevel, int is_mental, int is_empty, int is_alone, int is_poor, int is_help, int is_low, int is_new, int is_landlord, String disease, String volunteerId, String landlordTel, String carNumber, String hobby) {
        Observable observable = httpService.setHouseInfo(is_community, id, name, sex, birth, job, nation, political, address, phone, marriage, education, permanent, permanentCode, is_special, is_disability, disabilityLevel, is_mental, is_empty, is_alone, is_poor, is_help, is_low, is_new, is_landlord, disease, volunteerId, landlordTel, carNumber, hobby)
                .map(new HttpResultFunc<String>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 民情日志获取
     *
     * @param subscriber
     * @param page
     */
    public void getFolkList(Subscriber subscriber, int page) {
        Observable observable = httpService.getFolkList(page)
                .map(new HttpResultFunc<List<Folk>>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 提交民情日记
     *
     * @param subscriber
     * @param name
     * @param dzbxxb_id
     * @param visit_place
     * @param interviewee
     * @param nature_id
     * @param content
     * @param hanling_process
     * @param result
     * @param tag
     * @param input_person
     * @param input_time
     * @param visit_time
     * @param mqrj_imgs
     */
    public void setFolk(Subscriber subscriber, String name, String dzbxxb_id, String visit_place, String interviewee, String nature_id, String content, String hanling_process, String result, String tag, String input_person, String input_time, String visit_time, String mqrj_imgs) {
        Observable observable = httpService.setFolk(name, dzbxxb_id, visit_place, interviewee, nature_id, content, hanling_process, result, tag, input_person, input_time, visit_time, mqrj_imgs)
                .map(new HttpResultFunc<String>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取民情新增选项基础数据
     *
     * @param subscriber
     */
    public void getFolkChoose(Subscriber subscriber) {
        Observable observable = httpService.getFolkChoose();
//                .map(new HttpResultFunc<String>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 民情日志获取
     *
     * @param subscriber
     * @param id
     */
    public void getFolkDetails(Subscriber subscriber, String id) {
        Observable observable = httpService.getFolkDetails(id)
                .map(new HttpResultFunc<FolkDetails>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取园区
     *
     * @param subscriber
     */
    public void getGarden(Subscriber subscriber) {
        Observable observable = httpService.getGarden()
                .map(new HttpResultFunc<List<Garden>>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取幢
     *
     * @param subscriber
     * @param garden
     */
    public void getBuilding(Subscriber subscriber, String garden) {
        Observable observable = httpService.getBuilding(garden)
                .map(new HttpResultFunc<List<String>>());
        toSubscribe(observable, subscriber);
    }

    public void getHouse(Subscriber subscriber, String garden, String building) {
        Observable observable = httpService.getHouse(garden, building)
                .map(new HttpResultFunc<List<House>>());
        toSubscribe(observable, subscriber);
    }
}
