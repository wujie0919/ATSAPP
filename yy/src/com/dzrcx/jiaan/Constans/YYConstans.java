package com.dzrcx.jiaan.Constans;

import com.dzrcx.jiaan.Bean.BrandHelpMsgVO;
import com.dzrcx.jiaan.Bean.OrderDetailVo;
import com.dzrcx.jiaan.Bean.ShareContentBeanReturnContent;
import com.dzrcx.jiaan.Bean.SysConfigBean;
import com.dzrcx.jiaan.Bean.SysConfigInfo;
import com.dzrcx.jiaan.Bean.UserInfoBean;
import com.dzrcx.jiaan.Bean.UserInfoVo;
import com.dzrcx.jiaan.Bean.YYUser;
import com.dzrcx.jiaan.YYApplication;
import com.dzrcx.jiaan.tools.GsonTransformUtil;
import com.dzrcx.jiaan.tools.SharedPreferenceTool;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;

/**
 * 静态数据
 *
 * @author zhangyu
 */
public class YYConstans {
    /***
     * 车辆列表站点标签
     */
    public static int TAG_SECTIONBG = 0;//背景间隔
    public static int TAG_SECTION = 1;//站点类别
    public static int TAG_HASCARSECTION = 2;//有车类别
    public static int TAG_NOCARSECTION = 3;//无车类别
    /**
     * 数据返回状态标示Z
     */
    public final static int DATABACKTAG_NOCONNECT = 16902;// 无网络
    public final static int DATABACKTAG_LOCFAIL = 16903;// 定位失败
    public final static int DATABACKTAG_ERROR = 16904;// 服务器返回数据错误
    public final static int DATABACKTAG_SUCCESS = 16905;// 服务器返回数据成功
    // --数据请求tag
    public static int TAG_MAINACTIVITY2_1 = 16901;// mainactivity2_1请求数据tag；
    public static final int TAG_ORDERLISTATY2_1 = 16906;// 历史订单请求数据tag；
    public static final int TAG_GETUPDATA = 16907;// ；
    public static final int TAG_GETCOUNTORDERS = 16908;// 获取历史订单统计信息；
    public static final int TAG_GETSHARE = 16909;// 获取分享信息；
    public static final int TAG_GETACTIVITYCONTENT = 16910;// 获取活动信息tag
    // 数据tag--------//

    /**
     * 以下是百度导航
     */
    public static final String ROUTE_PLAN_NODE = "routePlanNode";
    public static final String SHOW_CUSTOM_ITEM = "showCustomItem";
    public static final String RESET_END_NODE = "resetEndNode";
    public static final String VOID_MODE = "voidMode";

    /**
     * 以下是汽车名字
     */
    public static final String CAR_BIYADI = "比亚迪E6";
    public static final String CAR_JIANGHUAI = "江淮IEV4";
    public static final String CAR_BEIQIEV150 = "北汽EV150";
    public static final String CAR_BEIQIEV160 = "北汽EV160";


    /**
     * 订单创建语音提示
     */
    public static final String sounds_create_def = "您已下单成功，我们将为您保留15分钟取车时间，15分钟后自动开始计费。取车时，如车辆正在充电，您可直接将充电枪拔出，将充电枪头放回充电桩的基座上即可，具体方法介绍可进入用户帮助界面中点击常见问题查看，如需帮助请点击下方客服电话，客服人员恭候您的来电。点击语音提示可重复听取。";

    /**
     * 车型使用教程
     */
    public static final String sounds_use_def = "车辆使用教程：进入车辆后，请您关好车门，启动车辆。将手刹松开，同时踩下刹车，然后挂档，慢慢松开刹车即可启动。如有其他问题可进入用户帮助界面点击用车指导查看车辆详细介绍，或点击客服电话，客服人员恭候您的来电。祝您用车愉快。点击语音提示可重复听取。";


    /**
     * 还车提醒
     */
    public static final String sounds_backcar_def = "环车注意事项：环车时，请您将车辆停放在正规停车位，环车后的停车费用由星辰租车承担，请在环车时注意将车辆熄火，档位回到P档或N档，关闭车灯和车窗，如有钥匙，请将钥匙拔出，并将手刹拉起，以防发生事故。感谢您的使用。";


    public static ArrayList<BrandHelpMsgVO> brandHelpMsgVOs;


    /**
     * 是否初始化百度导航（初始化成功才算）
     */
    public static boolean hasInitNavi = false;
    /**
     * 是否有未结束的订单
     */
    public static boolean hasUnFinishOrder = false;

    /**
     * 主界面的广告是否显示过
     */
    public static boolean hasShowMainAD = false;

    /**
     * 信鸽推送的token
     */
    public static String XG_token = "";

    /**
     * 缓存user
     */
    private static UserInfoBean userInfoBean;

    private static SysConfigBean sysConfig;

    private static LatLng useCarLng;

    public static ShareContentBeanReturnContent shareContentBeanReturnContent;
    public static OrderDetailVo currentOrderDetailVo;//正在进行的订单

    public static UserInfoBean getUserInfoBean() {
        if (userInfoBean == null) {
            try {
                userInfoBean = (UserInfoBean) GsonTransformUtil.fromJson(
                        SharedPreferenceTool.getPrefString(YYApplication
                                        .getApplication().getApplicationContext(),
                                SharedPreferenceTool.KEY_USERINFO, null),
                        UserInfoBean.class);
            } catch (Exception e) {
                SharedPreferenceTool.clearData(YYApplication
                        .getApplication().getApplicationContext());
            }

        }
        if (userInfoBean == null) {
            userInfoBean = new UserInfoBean();
            UserInfoVo userInfoVo = new UserInfoVo();
            YYUser yyUser = new YYUser();
            userInfoVo.setUser(yyUser);
            userInfoBean.setReturnContent(userInfoVo);
        }
        return userInfoBean;
    }

    public static void setUserInfoBean(UserInfoBean userInfoBean) {

        SharedPreferenceTool.setPrefString(YYApplication.getApplication()
                        .getApplicationContext(), SharedPreferenceTool.KEY_USERINFO,
                GsonTransformUtil.toJson(userInfoBean));
        if (userInfoBean == null) {
            YYConstans.hasUnFinishOrder = false;
        }

        YYConstans.userInfoBean = userInfoBean;
    }

    public static LatLng getUseCarLng() {
        return useCarLng;
    }

    public static void setUseCarLng(LatLng useCarLng) {
        YYConstans.useCarLng = useCarLng;
    }


    public static SysConfigBean getSysConfig() {

        if (sysConfig == null) {
            sysConfig = (SysConfigBean) GsonTransformUtil.fromJson(
                    SharedPreferenceTool.getPrefString(YYApplication
                                    .getApplication().getApplicationContext(),
                            SharedPreferenceTool.KEY_SYSCONFIG, null),
                    SysConfigBean.class);
        }
        if (sysConfig == null) {
            sysConfig = new SysConfigBean();
            SysConfigInfo sysConfigInfo = new SysConfigInfo();
            sysConfigInfo.setServicePhone("4008797979");
            sysConfig.setReturnContent(sysConfigInfo);
        }
        return sysConfig;
    }

    public static void setSysConfig(SysConfigBean sysConfig) {
        SharedPreferenceTool.setPrefString(YYApplication.getApplication()
                        .getApplicationContext(), SharedPreferenceTool.KEY_SYSCONFIG,
                GsonTransformUtil.toJson(sysConfig));
        YYConstans.sysConfig = sysConfig;
    }
}
