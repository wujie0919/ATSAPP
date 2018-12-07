package com.dzrcx.jiaan.Main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dzrcx.jiaan.Bean.CoupanListBean;
import com.dzrcx.jiaan.Bean.Coupon;
import com.dzrcx.jiaan.Bean.CouponADBean;
import com.dzrcx.jiaan.Bean.CouponADBeanReturnContent;
import com.dzrcx.jiaan.Bean.CouponInfoOutBean;
import com.dzrcx.jiaan.Constans.YYConstans;
import com.dzrcx.jiaan.Constans.YYOptions;
import com.dzrcx.jiaan.Constans.YYUrl;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseActivity;
import com.dzrcx.jiaan.base.YYBaseFragment;
import com.dzrcx.jiaan.clicklistener.RequestInterface;
import com.dzrcx.jiaan.tools.GsonTransformUtil;
import com.dzrcx.jiaan.tools.MyUtils;
import com.dzrcx.jiaan.tools.NetHelper;
import com.dzrcx.jiaan.tools.SharedPreferenceTool;
import com.dzrcx.jiaan.tools.TimeDateUtil;
import com.dzrcx.jiaan.tools.YYRunner;
import com.dzrcx.jiaan.widget.YYNotdataView;
import com.dzrcx.jiaan.zxing.android.CaptureActivity;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 优惠券界面
 * Created by zhangyu on 16-1-4.
 */
public class CouponListFrg extends YYBaseFragment implements View.OnClickListener, RequestInterface {

    private ImageView iv_left_raw;
    private View couponView, headView;
    private TextView barTitle, tv_redeemcode;
    private RelativeLayout rl_ad;
    private RelativeLayout rl_ad_heard;
    private ImageView rightImage, iv_close_heard, iv_close, iv_ad, iv_ad_heard;
    private EditText et_redeemcode;
    private PullToRefreshListView pull_refresh_list;
    private CouponAdapter couponAdapter;
    private YYNotdataView notdataView;

    private final String DECODED_CONTENT_KEY = "codedContent";
    private final String DECODED_BITMAP_KEY = "codedBitmap";
    private boolean isShowAD; //后台是否有广告

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (couponView == null) {
            couponView = LayoutInflater.from(mContext).inflate(R.layout.frg_couponlist, null);
            iv_left_raw = (ImageView) couponView.findViewById(R.id.iv_left_raw);
            iv_left_raw.setVisibility(View.VISIBLE);
            barTitle = (TextView) couponView.findViewById(R.id.tv_title);
            barTitle.setText("我的优惠券");
            iv_left_raw.setOnClickListener(this);
            rightImage = (ImageView) couponView.findViewById(R.id.iv_right);
            rightImage.setVisibility(View.VISIBLE);
            rightImage.setImageResource(R.drawable.icon_scan);
            rightImage.setPadding(MyUtils.dip2px(mContext, 25), 0, MyUtils.dip2px(mContext, 13), 0);
            rightImage.setOnClickListener(this);
            rightImage.setVisibility(View.GONE);
            /*
            广告栏
             */
            rl_ad = (RelativeLayout) couponView.findViewById(R.id.rl_ad);
            iv_close = (ImageView) couponView.findViewById(R.id.iv_close);
            iv_ad = (ImageView) couponView.findViewById(R.id.iv_ad);
            iv_ad.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MyUtils.getScreenWidth(mContext) * 69 / 640));
            iv_close.setOnClickListener(this);
            rl_ad.setOnClickListener(this);

            /**
             * 兑换码输入框
             */
            headView = inflater.inflate(R.layout.headview_redeemcode, null);
            rl_ad_heard = (RelativeLayout) headView.findViewById(R.id.rl_ad_heard);
            iv_ad_heard = (ImageView) headView.findViewById(R.id.iv_ad_heard);
            iv_close_heard = (ImageView) headView.findViewById(R.id.iv_close_heard);
            iv_ad_heard.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MyUtils.getScreenWidth(mContext) * 69 / 640));
            iv_close_heard.setOnClickListener(this);
            rl_ad_heard.setOnClickListener(this);

            et_redeemcode = (EditText) headView.findViewById(R.id.et_redeemcode);
            tv_redeemcode = (TextView) headView.findViewById(R.id.tv_redeemcode);
            tv_redeemcode.setOnClickListener(this);
            notdataView = new YYNotdataView(mContext, 225);
            notdataView.setMessage("亲爱的\n您还没有优惠券哦！");
            LinearLayout linearLayout = new LinearLayout(mContext);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.addView(headView);
            linearLayout.addView(notdataView);
            notdataView.setVisible(false);
            pull_refresh_list = (PullToRefreshListView) couponView.findViewById(R.id.pull_refresh_list);
            pull_refresh_list.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            pull_refresh_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                @Override
                public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                    getListData(false);
                }


                @Override
                public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

                }
            });
            pull_refresh_list.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    if (SCROLL_STATE_TOUCH_SCROLL == scrollState) {//滑动则清除
//                        View currentFocus = getActivity().getCurrentFocus();
//                        if (currentFocus != null) {
//                            currentFocus.clearFocus();
//                        }
                        et_redeemcode.clearFocus();
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if (!isShowAD) {
                        rl_ad.setVisibility(View.GONE);
                        rl_ad_heard.setVisibility(View.GONE);
                        return;
                    }
                    if (getScrollY() <= 0) {
                        rl_ad.setVisibility(View.GONE);
                    } else {
                        rl_ad.setVisibility(View.VISIBLE);
                    }
                }
            });
            couponAdapter = new CouponAdapter();
            pull_refresh_list.getRefreshableView().addHeaderView(linearLayout);
            pull_refresh_list.setAdapter(couponAdapter);
        }
        getListData(true);
        return couponView;
    }

    @Override
    public void onResume() {
        super.onResume();
        rl_ad_heard.setVisibility(View.GONE);
        rl_ad.setVisibility(View.GONE);
        if (!"RecommendFrg".equals(getActivity().getIntent().getExtras().getString("activity"))) {
            getADData(true);
        }
    }

    public int getScrollY() {
        View c = pull_refresh_list.getRefreshableView().getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = pull_refresh_list.getRefreshableView().getFirstVisiblePosition();
        int top = c.getTop();
        return -top + firstVisiblePosition * c.getHeight();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_close:
            case R.id.iv_close_heard:
                SharedPreferenceTool.setPrefString(mContext, "CouponListFrgClick", "noshow");
                isShowAD = false;
                rl_ad.setVisibility(View.GONE);
                rl_ad_heard.setVisibility(View.GONE);
                break;
            case R.id.rl_ad:
            case R.id.rl_ad_heard:
                startActivity(RecommendAty.class, null);
                break;
            case R.id.iv_left_raw:
                getActivity().finish();
                break;
            case R.id.iv_right:
                startActivityForResult(CaptureActivity.class, null, 1001);
                break;
            case R.id.tv_redeemcode:
                String code = et_redeemcode.getText() + "";
                if ("".equals(code.trim())) {
                    MyUtils.showToast(getActivity(), "请输入兑换码");
                    return;
                }
                getRedeemcode(code);
                break;
        }
    }

    private void getListData(boolean isShowDialog) {
        if (NetHelper.checkNetwork(mContext)) {
            pull_refresh_list.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pull_refresh_list.onRefreshComplete();
                }
            }, 100);
            MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
            return;
        }
        if (isShowDialog)
            dialogShow();
        Map<String, String> params = new HashMap<String, String>();
        params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
        YYRunner.getData(1001,
                YYRunner.Method_POST,
                YYUrl.GETCOUPONLIST, params,
                CouponListFrg.this);


    }

    private void getADData(boolean isShowDialog) {
        if (NetHelper.checkNetwork(mContext)) {
            pull_refresh_list.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pull_refresh_list.onRefreshComplete();
                }
            }, 100);
            MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
            return;
        }
        if (isShowDialog)
            dialogShow();
        Map<String, String> params = new HashMap<String, String>();
        params.put("adType", "4");
        params.put("type", "0");
        params.put("size", "0");
        YYRunner.getData(1003,
                YYRunner.Method_POST,
                YYUrl.GETADPHOTO, params,
                CouponListFrg.this);
    }

    private void getRedeemcode(String code) {
        if (NetHelper.checkNetwork(mContext)) {
            pull_refresh_list.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pull_refresh_list.onRefreshComplete();
                }
            }, 100);
            MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
            return;
        }
        dialogShow();
        Map<String, String> params = new HashMap<String, String>();
        params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
        params.put("code", code);
        YYRunner.getData(1002,
                YYRunner.Method_POST,
                YYUrl.GETREDEEMCODE, params,
                CouponListFrg.this);
    }

    @Override
    public void onError(int tag, String error) {
        dismmisDialog();
        MyUtils.showToast(mContext, "数据传输错误，请重试");
        pull_refresh_list.postDelayed(new Runnable() {
            @Override
            public void run() {
                pull_refresh_list.onRefreshComplete();
            }
        }, 100);

    }

    @Override
    public void onComplete(int tag, String json) {
        dismmisDialog();
        pull_refresh_list.postDelayed(new Runnable() {
            @Override
            public void run() {
                pull_refresh_list.onRefreshComplete();
            }
        }, 100);
        switch (tag) {
            case 1001:
                CoupanListBean coupanListBean = (CoupanListBean) GsonTransformUtil
                        .fromJson(json, CoupanListBean.class);

                if (coupanListBean != null && coupanListBean.getErrno() == 0 && coupanListBean.getReturnContent() != null) {
                    couponAdapter.setCouponList(coupanListBean.getReturnContent().getAvailableList());


                    if (coupanListBean.getReturnContent().getUnavailableList() != null) {
                        for (int i = 0; i < coupanListBean.getReturnContent().getUnavailableList().size(); i++) {
                            coupanListBean.getReturnContent().getUnavailableList().get(i).setIsAble(-1);
                        }
                    }
                    couponAdapter.getCouponList().addAll(coupanListBean.getReturnContent().getUnavailableList());
                    couponAdapter.notifyDataSetChanged();
                } else {
                    couponAdapter.setCouponList(null);
                    couponAdapter.notifyDataSetChanged();
                }
                if (couponAdapter.getCount() == 0) {
                    notdataView.setVisible(true);
//                    headView.setVisibility(View.GONE);
                } else {
                    notdataView.setVisible(false);
//                    headView.setVisibility(View.VISIBLE);
                }
                break;
            case 1002:
                CouponInfoOutBean couponInfoOutBean = (CouponInfoOutBean) GsonTransformUtil
                        .fromJson(json, CouponInfoOutBean.class);
                if (couponInfoOutBean.getErrno() != 0) {
                    MyUtils.showToast(getActivity(), couponInfoOutBean.getError());
                } else {
                    getListData(false);
                    showMessageDialog("兑换成功!\n优惠券已放入您账户中");
                }
                break;
            case 1003:
                CouponADBeanReturnContent couponADBeanReturnContent = (CouponADBeanReturnContent) GsonTransformUtil.fromJson(json, CouponADBeanReturnContent.class);
                if (couponADBeanReturnContent.getErrno() != 0) {
                    MyUtils.showToast(getActivity(), couponADBeanReturnContent.getError());
                } else {
                    if (couponADBeanReturnContent.getReturnContent() != null && couponADBeanReturnContent.getReturnContent().size() > 0) {
                        fillADData(couponADBeanReturnContent.getReturnContent().get(0));
                    } else {
                        isShowAD = false;
                    }
                }

                break;
        }
    }

    private void fillADData(CouponADBean couponADBean) {
        if (!SharedPreferenceTool.getPrefString(mContext, "CouponListFrg_ADID", "-1").equals(couponADBean.getAdId() + "") || !"noshow".equals(SharedPreferenceTool.getPrefString(mContext, "CouponListFrgClick", ""))) {
            SharedPreferenceTool.setPrefString(mContext, "CouponListFrg_ADID", couponADBean.getAdId() + "");
            SharedPreferenceTool.setPrefString(mContext, "CouponListFrgClick", "");
            rl_ad.setVisibility(View.VISIBLE);
            rl_ad_heard.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(
                    couponADBean.getPhotoUrl(), iv_ad, YYOptions.OPTION_DEF);
            ImageLoader.getInstance().displayImage(
                    couponADBean.getPhotoUrl(), iv_ad_heard, YYOptions.OPTION_DEF);
            isShowAD = true;
        } else {
            rl_ad.setVisibility(View.GONE);
            rl_ad_heard.setVisibility(View.GONE);
            isShowAD = false;
        }
    }

    @Override
    public void onLoading(long count, long current) {

    }

    class CouponAdapter extends BaseAdapter {


        private ArrayList<Coupon> couponList;

        public ArrayList<Coupon> getCouponList() {
            return couponList;
        }

        public void setCouponList(ArrayList<Coupon> couponList) {
            this.couponList = couponList;
        }

        @Override
        public int getCount() {
            return couponList == null ? 0 : couponList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHold viewHold = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_couponlist, null);
                viewHold = new ViewHold();
                viewHold.itemLayout = (LinearLayout) convertView.findViewById(R.id.item_coupon_layout);
                viewHold.couponType = (TextView) convertView.findViewById(R.id.item_coupon_type);
                viewHold.couponAmount = (TextView) convertView.findViewById(R.id.item_coupon_amout);
                viewHold.couponTime = (TextView) convertView.findViewById(R.id.item_coupon_time);
                viewHold.couponStr = (TextView) convertView.findViewById(R.id.item_coupon_str);

                viewHold.itemLayout.getLayoutParams().height = initHeight();

                convertView.setTag(viewHold);
            } else {
                viewHold = (ViewHold) convertView.getTag();
            }
            Coupon coupon = couponList.get(position);
            viewHold.couponAmount.setText(coupon.getAmount() + "元");
            viewHold.couponType.setText(coupon.getCouponName());
            if (coupon.getIsAble() == -1) {
                //失效
                viewHold.itemLayout.setSelected(false);
                viewHold.couponTime.setText("已过期" + TimeDateUtil.formatTime(coupon.getEndTimes(), "(yyyy-MM-dd)"));
                viewHold.couponStr.setVisibility(View.GONE);
            } else {
                viewHold.itemLayout.setSelected(true);
                viewHold.couponTime.setText("有效期至" + TimeDateUtil.formatTime(coupon.getEndTimes(), "yyyy-MM-dd"));
                viewHold.couponStr.setVisibility(View.VISIBLE);
            }


            return convertView;
        }

        private int initHeight() {
            int hei = (int) ((MyUtils.getScreenWidth(mContext) - MyUtils.dip2px(mContext, 30)) * 1.0 * 177 / 569);
            return hei;
        }


    }

    class ViewHold {
        private LinearLayout itemLayout;
        private TextView couponType, couponAmount, couponTime, couponStr;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == YYBaseActivity.RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(DECODED_CONTENT_KEY);
                if (content != null && content.startsWith("yyzc")) {
                    getRedeemcode(content.substring(5));
                }
            }
        }
    }
}
