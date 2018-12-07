package com.dzrcx.jiaan.Constans;

public class YYUrl {
    /**
     * 正式地址
     */
    public static String rootUrl = "http://bdapi.tjdaoxing.cn:8080/service/";// 域名地址
    public static String UNPAYTAG = "00";

    /**
     * 上传图片提交按钮调用接口
     */
    public static String GETMODIFYAUDITINFO = rootUrl + "modifyAuditInfo.do";
    /**
     * 获取订单列表
     * 0进行中
     * 1已完成
     * 2所有订单
     */
    public static String GETORDERLIST = rootUrl + "queryOrders.do";
    /**
     * 获取电子围栏数据
     */
    public static String GETRAILINFO = rootUrl + "getRailInfo.do";
    /**
     * 订单获取详情
     */
    public static String GETORDEDETAIL = rootUrl + "orderDetail.do";
    /**
     * 获取正在执行的订单详情
     */
    public static String GETUNDOORDEDETAIL = rootUrl + "getUndoOrder.do";

    /**
     * 获取站点信息
     */
    public static String GETSTATIONCARLIST = rootUrl + "getStationCarList.do";
    /**
     * 根据站点查询车辆信息
     */
    public static String GETCARSBYIDS = rootUrl + "getCarsByIds.do";

    /**
     * 登录
     */
    public static String GETLOGIN = rootUrl + "login.do";
    /**
     * 获取用户信息
     */
    public static String GETUSERINFO = rootUrl + "getUserInfoFromSkey.do";
    /**
     * 获取手机信息用户信息
     */
    public static String ADDPHONEUSE = rootUrl + "addPhoneUse.do";

    /**
     * 发送验证码
     */
    public static String SENDVERIFYCODE = rootUrl + "sendVerifyCode.do";
    /**
     * 获取认证状态
     */
    public static String GETRENTERSTATE = rootUrl + "getRenterState.do";

    /**
     * 提现钱检察用户是否可以提现接口
     */
    public static String CHECKUSERFOREGIFT = rootUrl + "checkUserForegift.do";

    /**
     * 用户提现接口
     * <p/>
     * flag 1:取消提现 2:提现
     */
    public static String APPLICATIONWITHDRAWAL = rootUrl + "applicationWithdrawal.do";


    /**
     * 获取用户违章押金接口
     */
    public static String GETUSERDEPOSIT = rootUrl + "getUserDeposit.do";

    /**
     * 获取反馈接口内容
     */
    public static String GETFEELBACKCONTENT = rootUrl + "getCommentContent.do";//v2/6/
    /**
     * 反馈接口
     */
    public static String GETFEELBACK = rootUrl + "addFeedBackInfo.do";
    /**
     * /**
     * 获取认证图片
     */
    public static String GETIDENTIFYPIC = rootUrl + "getRenterInfo.do";
    /**
     * 获取文案
     */
    public static String GETDOCUMENT = rootUrl + "getDocument.do";
    /**
     * 新增订单
     */
    public static String GETADDORDER = rootUrl + "addOrder.do";//2.9开始
    /**
     * 更换更新订单接口
     * 1等待取车
     * 2正在使用
     * 3等待支付
     * 4订单完成
     * 5订单取消
     */
    public static String GETUPDATAORDER = rootUrl + "updateOrderState.do";
    /**
     * 车辆操作
     */
    public static String GETOPERATECAR = rootUrl + "operateCar.do";
    /**
     * 版本更新
     */
    public static String GETUPDATA = rootUrl + "getHeadVersion.do";


    /**
     * 　确认使用企业支付
     */
    public static String PAYBYENTERPRISE = rootUrl + "payByEnterprise.do";

    /**
     * 获取微信，支付宝订单信息
     */
    public static String BUILDPAY = rootUrl + "buildPay.do";
    /**
     * 获取微信，支付宝押金接口
     */
    public static String BUILDPAYFORFOREGIFT = rootUrl + "buildPayForForegift.do";


    /**
     * 启动界面的获取广告信息
     */
    public static String GETADPHOTOS = rootUrl + "getAdPhoto.do";

//    /**
//     * 个人历史订单统计信息
//     */
//    public static String GETCOUNTORDERS = rootUrl + "countOrders.do";

    /**
     * 个人企业账户中心
     */
    public static String GETENTERPRISECENTER = rootUrl + "enterpriseCenter.do";
    /**
     * 获取开发票金额订单或发票总额
     */
    public static String GETINVOICEROUTEINFO = rootUrl + "getInvoice.do";//v2/3/
    /**
     * 开发票
     */
    public static String GETINVOICE = rootUrl + "addInvoice.do";
    /**
     * 获取发票详情
     */
    public static String GET_INVOICE_DETAIL = rootUrl + "getInvoiceDetail.do";

    /**
     * 个人获取开票历史列表
     */
    public static String GETINVOICEHISTORY = rootUrl + "getInvoiceHistory.do";

    /**
     * 个人获取优惠券列表
     */
    public static String GETCOUPONLIST = rootUrl + "getCouponList.do";

    /**
     * 余额充值
     */
    public static String BULIDPAYTORECHARGE = rootUrl + "buildPayToRecharge.do";//v2/3/

    /**
     * 充值历史
     */
    public static String GETRECHARGEHISTORY = rootUrl + "getRechargeHistory.do";
    /**
     * /**
     * 充值历史
     */
    public static String GETSHOWALLUSERTRADESRECORD = rootUrl + "showAllUserTradesRecord.do";
    /**
     * 余额支付
     */
    public static String GETYUEPAY = rootUrl + "payByUserAccount.do";

    /**
     * 刷新订单支付页面
     */
    public static String REVIEWORDER = rootUrl + "reviewOrder.do";


    /**
     * 获取系统配置
     */
    public static String GETSYSCONFIG = rootUrl + "getSysConfig.do";//v2/3/

    /**
     * 银联预授权
     */
    public static String BUILDUNIONPAYSDK = rootUrl + "buildUnionpaySDK.do";//v2/3/
    /**
     * 　获取充值返现金额
     */
    public static String GETBACKFEE = rootUrl + "getBackFee.do";//v2/3/
    /**
     * 　兑换验证码
     */
    public static String GETREDEEMCODE = rootUrl + "exchangeCouponCode.do";

    /**
     * 违章和事故查询
     */
    public static String GETVIOLATION = rootUrl + "getViolation.do";
    /**
     * 违章和事故支付
     */
    public static String GETVIOLATIONPAY = rootUrl + "buildPayForViolation.do";//v3/
    /**
     * 分享URL
     */
    public static String GETSHAREURL = rootUrl + "getShareUrl.do";//v2/7/
    /**
     * 活动详情内容
     */
    public static String GETACTIVITYCONTENT = rootUrl + "getActivity.do";//v2/7/
    /**
     * 获取兑换码上不广告条
     */
    public static String GETADPHOTO = rootUrl + "getAdPhoto.do";


    /**
     * 1.4　检测企业是否可支付
     */
    public static String CHECKENTPAY = rootUrl + "checkEntPay.do";


    /**
     * 1.3根据站点状态获取换车站点列表
     */
    public static String GETSTATIONBYSTAYTYPE = rootUrl + "getStationByStayType.do";//v2/9/

    /**
     * 获取充值返现现金卡
     */
    public static String SHOWALLRECHARGEBACK = rootUrl + "showAllRechargeBack.do";

    /**
     * 根据充值金额获取返现金额数量
     */
    public static String SHOWRECHARGEBACKAMOUNT = rootUrl + "showRechargeBackByAmount.do";

    /**
     * 违章和事故支付
     */
    public static String BUILDPAYFORVIOLATION = rootUrl + "buildPayForViolation.do";//v3/

    /**
     * 修改还车站点 3.0开始
     */
    public static String CHANGEBACKSTATION = rootUrl + "changeBackStation.do";

    /**
     * 获取图片和语音帮助提示3.0开始动态添加
     */
    public static String GETBRANDHELPMSG = rootUrl + "getBrandHelpMsg.do";

    /**
     * 获取日夜间时段
     */
    public static String GETDAYANDNIGHT = rootUrl + "getDayAndNight.do";

    /**
     * 获取有车提醒时间
     */
    public static String GETOPRATEREMAINTIME = rootUrl + "operateRemainTime.do";

    /**
     * 获取车辆品牌列表
     */
    public static String GETSHOWALLBRANDNAME = rootUrl + "showAllBrandName.do";
    /**
     * 获取分页车辆列表
     */
    public static String GETNEARSTATIONCARLIST = rootUrl + "getNearStationCarList.do";

    /**
     * 违章事故详情
     */
    public static String GETVIOLATIONDETAIL = rootUrl + "getViolationDetail.do";


    /**
     * 吐槽星辰服务类型接口
     */
    public static String SHOWALLCOMPLAINTYPE = rootUrl + "showAllComplainType.do";


    /**
     * 添加吐槽记录
     */
    public static String ADDFEEDBACKBYCOMPLAIN = rootUrl + "addFeedBackByComplain.do";


    /**
     * 上传图片接口
     * <p/>
     * flag1:身份证 2:驾驶证3:用户头像
     */
    public static String UPLOADUSERIMG = rootUrl + "upLoadUserImg.do";

    /**
     * 首页有车提醒
     */
    public static String ADDCARREMIND = rootUrl + "addCarRemind.do";

    /**
     * 车辆检验图片和说明
     */
    public static String UPLOADORDERPHOTO = rootUrl + "uploadOrderPhoto.do";

    /**
     * 推送消息列表
     */
    public static String GETPUSHMESSAGELIST = rootUrl + "getPushMessageList.do";

    /**
     * 更换手机号
     */
    public static String CHANGEMOBILE = rootUrl + "changeMobile.do";
    /**
     * 更换手机号
     */
    public static String buildPayForDailyRental = rootUrl + "buildPayForDailyRental.do";



}
