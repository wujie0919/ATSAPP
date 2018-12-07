package com.dzrcx.jiaan.User;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dzrcx.jiaan.Bean.EnterpriseCenterBean;
import com.dzrcx.jiaan.Bean.EnterpriseItemInfo;
import com.dzrcx.jiaan.Constans.YYConstans;
import com.dzrcx.jiaan.Constans.YYUrl;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseFragment;
import com.dzrcx.jiaan.clicklistener.RequestInterface;
import com.dzrcx.jiaan.tools.GsonTransformUtil;
import com.dzrcx.jiaan.tools.LG;
import com.dzrcx.jiaan.tools.MyUtils;
import com.dzrcx.jiaan.tools.NetHelper;
import com.dzrcx.jiaan.tools.YYRunner;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 企业账户
 */
public class CompanyAccountFrg extends YYBaseFragment implements OnClickListener, RequestInterface {
    private LayoutInflater inflater;
    private View companyView, headview_companyaccount;
    private TextView barTitle;
    private ImageView rightImage, iv_left_raw;
    private PullToRefreshListView pull_refresh_list;
    private CompanyAdapter companyAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: 15-12-17
        if (companyView == null) {
            companyView = LayoutInflater.from(mContext).inflate(R.layout.frg_companyaccount, null);
            headview_companyaccount = LayoutInflater.from(mContext).inflate(R.layout.headview_companyaccount, null);
            iv_left_raw = (ImageView) companyView.findViewById(R.id.iv_left_raw);
            iv_left_raw.setVisibility(View.VISIBLE);
            barTitle = (TextView) companyView.findViewById(R.id.tv_title);
            barTitle.setText("企业账户");
            iv_left_raw.setOnClickListener(this);
            rightImage = (ImageView) companyView.findViewById(R.id.iv_right);
//            rightImage.setVisibility(View.VISIBLE);
//            rightImage.setOnClickListener(this);
            pull_refresh_list = (PullToRefreshListView) companyView.findViewById(R.id.pull_refresh_list);
            //ListView listView = pull_refresh_list.getRefreshableView();
            //listView.addHeaderView(headview_companyaccount);
            pull_refresh_list.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            pull_refresh_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                @Override
                public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                    getEnterpriseList(false);
                }

                @Override
                public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

                }
            });
            companyAdapter = new CompanyAdapter(mContext);
            pull_refresh_list.setAdapter(companyAdapter);


        }
        getEnterpriseList(true);
        return companyView;
    }


    private void getEnterpriseList(boolean isShowdialog) {

        if (NetHelper.checkNetwork(mContext)) {
            pull_refresh_list.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pull_refresh_list.onRefreshComplete();
                }
            }, 100);
            showNoNetDlg();
            MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
            return;
        }
        if (isShowdialog)
            dialogShow();
        /**
         * 请求企业账户信息
         */
        dialogShow();
        Map<String, String> params = new HashMap<String, String>();
        params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
        YYRunner.getData(1001, YYRunner.Method_POST,
                YYUrl.GETENTERPRISECENTER, params, this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left_raw:
                getActivity().finish();
                break;
            case R.id.iv_right:
            case R.id.companyac_buttonlayout_call:
                showCallDialog();
                break;
            case R.id.companyac_buttonlayout_regist:
                showMessageDialog("非常抱歉，手机暂不支持注册，请前往星辰出行企业官网www.qiye.yiyizuche.cn进行注册");
                break;
            default:
                break;
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroy();
    }

    @Override
    public void onError(int tag, String error) {
        dismmisDialog();
        MyUtils.showToast(getActivity(), "数据传输错误，请重试");
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
                EnterpriseCenterBean enterPriseBean = (EnterpriseCenterBean) GsonTransformUtil.fromJson(json, EnterpriseCenterBean.class);
                if (enterPriseBean != null && enterPriseBean.getErrno() == 0 && enterPriseBean.getReturnContent() != null && enterPriseBean.getReturnContent().getEnterpriseCenterList() != null && enterPriseBean.getReturnContent().getEnterpriseCenterList().size() > 0) {
                    companyAdapter.setEnterpriseList(enterPriseBean.getReturnContent().getEnterpriseCenterList());
                    companyAdapter.notifyDataSetChanged();
                } else {
                }

                break;
        }

    }

    @Override
    public void onLoading(long count, long current) {

    }


    class CompanyAdapter extends BaseAdapter {

        private Context mContext;
        private ArrayList<EnterpriseItemInfo> enterpriseList;

        public CompanyAdapter(Context mContext) {
            this.mContext = mContext;
        }


        public ArrayList<EnterpriseItemInfo> getEnterpriseList() {
            return enterpriseList;
        }


        public void setEnterpriseList(ArrayList<EnterpriseItemInfo> enterpriseList) {
            this.enterpriseList = enterpriseList;
        }

        @Override
        public int getCount() {
            return this.enterpriseList == null ? 0 : this.enterpriseList.size();
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
            LG.d("getvoew-------------");
            ViewHold viewHold = null;
            if (convertView == null) {
                viewHold = new ViewHold();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_frg_companyaccount, null);
                viewHold.spitline = (LinearLayout) convertView.findViewById(R.id.companyaccount_spitline);
                viewHold.label0 = (TextView) convertView.findViewById(R.id.companyaccount_label0);
                viewHold.label1 = (TextView) convertView.findViewById(R.id.companyaccount_label1);
                viewHold.label2 = (TextView) convertView.findViewById(R.id.companyaccount_label2);
                convertView.setTag(viewHold);
            } else {
                viewHold = (ViewHold) convertView.getTag();
            }
            if (position == 0) {
                viewHold.spitline.setVisibility(View.GONE);
            } else {
                viewHold.spitline.setVisibility(View.VISIBLE);
            }

            EnterpriseItemInfo itemInfo = enterpriseList.get(position);
            viewHold.label0.setText(itemInfo.getName());

            if (itemInfo.getLimitType() == 1) {
                viewHold.label2.setText(itemInfo.getDeptLeft() + " 元");
                viewHold.label1.setText(itemInfo.getAmount() + " 元");
            } else {
                viewHold.label2.setText("不限额");
                viewHold.label1.setText("不限额");
            }

            return convertView;
        }


        private class ViewHold {
            private LinearLayout spitline;
            private TextView label0, label1, label2;

        }

    }

    private Bitmap initBitmap(Bitmap adBitmap, int id) {

        if (adBitmap == null || adBitmap.isRecycled()) {
            adBitmap = BitmapreadBitMap(id);
        }

        return adBitmap;
    }


    private Bitmap BitmapreadBitMap(int resID) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
// 获取资源图片
        InputStream is = this.getResources().openRawResource(resID);
        return BitmapFactory.decodeStream(is, null, opt);
    }
}
