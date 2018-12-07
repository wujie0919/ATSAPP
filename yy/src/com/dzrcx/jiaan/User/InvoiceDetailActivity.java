package com.dzrcx.jiaan.User;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dzrcx.jiaan.Bean.InvoiceDetailBean;
import com.dzrcx.jiaan.Constans.YYConstans;
import com.dzrcx.jiaan.Constans.YYOptions;
import com.dzrcx.jiaan.Constans.YYUrl;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseActivity;
import com.dzrcx.jiaan.clicklistener.RequestInterface;
import com.dzrcx.jiaan.tools.GsonTransformUtil;
import com.dzrcx.jiaan.tools.MyUtils;
import com.dzrcx.jiaan.tools.NetHelper;
import com.dzrcx.jiaan.tools.YYRunner;
import com.dzrcx.jiaan.widget.ChooseAreaDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * 普通发票详情
 * Created by chenh on 2017/1/17.
 */

public class InvoiceDetailActivity extends YYBaseActivity {
    private View invoiceMoneyView;
    private TextView tv_address_after, tv_route_after, tv_commit, tv_invocicecontent, tv_recipertxt, tv_receivephonetxt;
    private ImageView iv_left_raw;
    //private TextView tv_title;
    private EditText et_makeup, et_money_after, et_detailaddress;
    //增值税发票新增_______________________________________
    private EditText et_taxpayer_identity, et_company_address, et_company_phone, et_bank_name, et_bank_account_number;
    //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    private RelativeLayout rl_invocicecontent, rl_address;
    private Bundle bundle;
    private Dialog messageDlg, chooseDlg, nonameDlg;
    private ChooseAreaDialog areaDialog;
    private String provinceStr, cityStr, districtStr;
    //                    营业执照              银行开户许可证                   //页面缓存图片用
    private LinearLayout ll_business_license, ll_bank_open_account_license, ll_cash_drowable;
    private ImageView iv_business_license, iv_bank_open_account_license;
    private String invoiceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_detail);
        invoiceId = getIntent().getStringExtra("invoiceId");
        initView();
        requestData();
    }

    public void initView() {
        ll_cash_drowable = (LinearLayout) findViewById(R.id.ll_cash_drowable);
//        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_left_raw = (ImageView) findViewById(R.id.iv_left_raw);
        iv_left_raw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//        iv_left_raw.setVisibility(View.VISIBLE);
        ll_business_license = (LinearLayout) findViewById(R.id.ll_business_license);
        ll_bank_open_account_license = (LinearLayout) findViewById(R.id.ll_bank_open_account_license);
        iv_business_license = (ImageView) findViewById(R.id.iv_business_license);
        iv_bank_open_account_license = (ImageView) findViewById(R.id.iv_bank_open_account_license);
//        iv_business_license.setVisibility(View.GONE);
//        iv_bank_open_account_license.setVisibility(View.GONE);
        tv_invocicecontent = (TextView) findViewById(R.id.tv_invocicecontent);
        tv_commit = (TextView) findViewById(R.id.tv_commit);
        et_money_after = (EditText) findViewById(R.id.et_money_after);
        tv_route_after = (TextView) findViewById(R.id.tv_route_after);
        tv_address_after = (TextView) findViewById(R.id.tv_address_after);

        et_taxpayer_identity = (EditText) findViewById(R.id.et_taxpayer_identity);
        et_company_address = (EditText) findViewById(R.id.et_company_address);
        et_company_phone = (EditText) findViewById(R.id.et_company_phone);
        et_bank_name = (EditText) findViewById(R.id.et_bank_name);
        et_bank_account_number = (EditText) findViewById(R.id.et_bank_account_number);

        et_makeup = (EditText) findViewById(R.id.et_makeup);
        tv_recipertxt = (TextView) findViewById(R.id.tv_recipertxt);
        if (!YYConstans.getUserInfoBean().getReturnContent().getUser().getName().equals(YYConstans.getUserInfoBean().getReturnContent().getUser().getMobile())) {
            tv_recipertxt.setText(YYConstans.getUserInfoBean().getReturnContent().getUser().getName() + "");
        }
        tv_receivephonetxt = (TextView) findViewById(R.id.tv_receivephonetxt);
        tv_receivephonetxt.setText(YYConstans.getUserInfoBean().getReturnContent().getUser().getMobile() + "");
        et_detailaddress = (EditText) findViewById(R.id.et_detailaddress);
        rl_invocicecontent = (RelativeLayout) findViewById(R.id.rl_invocicecontent);
        rl_address = (RelativeLayout) findViewById(R.id.rl_address);
    }

    private void setupView(InvoiceDetailBean.InvoiceDetailBean1 invoiceDetailBean1) {
        //ImageLoader.getInstance().displayImage(uploadUrl.replace("oss", "img") + "@300w_200h_1e_1c_6-2ci.jpg", iv_auth_sfz, YYOptions.OPTION_DEF);
        ImageLoader.getInstance().displayImage(invoiceDetailBean1.getAccountPermitCertificate()
                , iv_business_license, YYOptions.OPTION_DEF);
        ImageLoader.getInstance().displayImage(invoiceDetailBean1.getBusinessLicense()
                , iv_bank_open_account_license, YYOptions.OPTION_DEF);
        tv_invocicecontent = (TextView) findViewById(R.id.tv_invocicecontent);
        tv_commit = (TextView) findViewById(R.id.tv_commit);
        et_money_after.setText(invoiceDetailBean1.getAmount());//金额
        tv_route_after = (TextView) findViewById(R.id.tv_route_after);
        tv_address_after.setText(invoiceDetailBean1.getProvince() + invoiceDetailBean1.getCity() + invoiceDetailBean1.getDistrict());
        et_taxpayer_identity.setText(invoiceDetailBean1.getTaxpayerNo());
        et_company_address.setText(invoiceDetailBean1.getCompanyAddress());
        et_company_phone.setText(invoiceDetailBean1.getCompanyMobile());
        et_bank_name.setText(invoiceDetailBean1.getBankName());
        et_bank_account_number.setText(invoiceDetailBean1.getBankAccount());
        et_makeup.setText(invoiceDetailBean1.getTitle());
        tv_recipertxt.setText(invoiceDetailBean1.getRecipient());
        tv_receivephonetxt.setText(invoiceDetailBean1.getMobile());
        et_detailaddress.setText(invoiceDetailBean1.getAddress());
    }

    public void requestData() {
        if (NetHelper.checkNetwork(this)) {
            showNoNetDlg();
            MyUtils.showToast(this, "网络异常，请检查网络连接或重试");
            return;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
        params.put("id", invoiceId);//增值税发票

        YYRunner.getData(0, YYRunner.Method_POST,
                YYUrl.GET_INVOICE_DETAIL, params, new RequestInterface() {
                    @Override
                    public void onError(int tag, String error) {
                        Log.e("onComplete", "错误" + error);
                    }

                    @Override
                    public void onComplete(int tag, String json) {
                        InvoiceDetailBean invoiceDetailBean
                                = (InvoiceDetailBean) GsonTransformUtil
                                .fromJson(json, InvoiceDetailBean.class);
                        if (invoiceDetailBean != null) {
                            setupView(invoiceDetailBean.getReturnContent());
                        }
                    }

                    @Override
                    public void onLoading(long count, long current) {

                    }
                });

    }

}
