package com.dzrcx.jiaan.User;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.dzrcx.jiaan.Bean.YYBaseResBean;
import com.dzrcx.jiaan.Constans.YYConstans;
import com.dzrcx.jiaan.Constans.YYUrl;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.YYApplication;
import com.dzrcx.jiaan.base.YYBaseFragment;
import com.dzrcx.jiaan.clicklistener.RequestInterface;
import com.dzrcx.jiaan.tools.GsonTransformUtil;
import com.dzrcx.jiaan.tools.MyUtils;
import com.dzrcx.jiaan.tools.NetHelper;
import com.dzrcx.jiaan.tools.YYRunner;
import com.dzrcx.jiaan.utils.ImageFactory;
import com.dzrcx.jiaan.widget.ChooseAreaDialog;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddValueInvoice extends YYBaseFragment implements View.OnClickListener, RequestInterface {
    private View invoiceMoneyView;
    private TextView tv_address_after, tv_route_after, tv_commit, tv_invocicecontent, tv_recipertxt, tv_receivephonetxt;
    //private ImageView iv_left_raw;
    //private TextView tv_title;
    private EditText et_makeup, et_money_after, et_detailaddress,et_forcer;
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
    private Map<String, String> imageMap = new HashMap<String, String>();

    public void setIntent(Intent intent) {
        this.bundle = intent.getExtras();
    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (invoiceMoneyView == null) {
            invoiceMoneyView = LayoutInflater.from(mContext).inflate(
                    R.layout.fragment_add_value_invoice, null);
            initView();
            if (YYConstans.getUserInfoBean().getReturnContent().getUser().getName().equals(YYConstans.getUserInfoBean().getReturnContent().getUser().getMobile())) {
                showNonameDialog("无法获取您的真实姓名，请联系客服询问");
            }
        }
        return invoiceMoneyView;
    }

    public void initView() {
        ll_cash_drowable = (LinearLayout) invoiceMoneyView.findViewById(R.id.ll_cash_drowable);
//        tv_title = (TextView) invoiceMoneyView.findViewById(R.id.tv_title);
//        iv_left_raw = (ImageView) invoiceMoneyView.findViewById(R.id.iv_left_raw);
//        iv_left_raw.setVisibility(View.VISIBLE);
        ll_business_license = (LinearLayout) invoiceMoneyView.findViewById(R.id.ll_business_license);
        ll_bank_open_account_license = (LinearLayout) invoiceMoneyView.findViewById(R.id.ll_bank_open_account_license);
        iv_business_license = (ImageView) invoiceMoneyView.findViewById(R.id.iv_business_license);
        iv_bank_open_account_license = (ImageView) invoiceMoneyView.findViewById(R.id.iv_bank_open_account_license);
        iv_business_license.setVisibility(View.GONE);
        iv_bank_open_account_license.setVisibility(View.GONE);
        ll_business_license.setOnClickListener(this);
        ll_bank_open_account_license.setOnClickListener(this);
        tv_invocicecontent = (TextView) invoiceMoneyView.findViewById(R.id.tv_invocicecontent);
        tv_commit = (TextView) invoiceMoneyView.findViewById(R.id.tv_commit);
        et_money_after = (EditText) invoiceMoneyView.findViewById(R.id.et_money_after);
        tv_route_after = (TextView) invoiceMoneyView.findViewById(R.id.tv_route_after);
        tv_address_after = (TextView) invoiceMoneyView.findViewById(R.id.tv_address_after);

        et_taxpayer_identity = (EditText) invoiceMoneyView.findViewById(R.id.et_taxpayer_identity);
        et_forcer = (EditText) invoiceMoneyView.findViewById(R.id.et_forcer);
        et_company_address = (EditText) invoiceMoneyView.findViewById(R.id.et_company_address);
        et_company_phone = (EditText) invoiceMoneyView.findViewById(R.id.et_company_phone);
        et_bank_name = (EditText) invoiceMoneyView.findViewById(R.id.et_bank_name);
        et_bank_account_number = (EditText) invoiceMoneyView.findViewById(R.id.et_bank_account_number);
/**
 * 1==金额；2==行程
 */
//        if ("1".equals(bundle.getString("type"))) {
////            tv_title.setText("按金额开票");
        et_money_after.setVisibility(View.VISIBLE);
        et_money_after.setHint("请输入开票金额,<200元邮费到付");
//        } else if ("2".equals(bundle.getString("type"))) {
////            tv_title.setText("按行程开票");
//           tv_route_after.setVisibility(View.VISIBLE);
//            tv_route_after.setText(bundle.getString("amount") + "元");
//        }

        et_makeup = (EditText) invoiceMoneyView.findViewById(R.id.et_makeup);
        tv_recipertxt = (TextView) invoiceMoneyView.findViewById(R.id.tv_recipertxt);
        if (!YYConstans.getUserInfoBean().getReturnContent().getUser().getName().equals(YYConstans.getUserInfoBean().getReturnContent().getUser().getMobile())) {
            tv_recipertxt.setText(YYConstans.getUserInfoBean().getReturnContent().getUser().getName() + "");
        }
        tv_receivephonetxt = (TextView) invoiceMoneyView.findViewById(R.id.tv_receivephonetxt);
        tv_receivephonetxt.setText(YYConstans.getUserInfoBean().getReturnContent().getUser().getMobile() + "");
        et_detailaddress = (EditText) invoiceMoneyView.findViewById(R.id.et_detailaddress);
        rl_invocicecontent = (RelativeLayout) invoiceMoneyView.findViewById(R.id.rl_invocicecontent);
        rl_address = (RelativeLayout) invoiceMoneyView.findViewById(R.id.rl_address);
        MyUtils.setViewsOnClick(this, rl_invocicecontent, tv_commit, rl_address);//, iv_left_raw删了

        et_money_after.addTextChangedListener(new TextWatcher() {
            private boolean isChanged = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isChanged) {// ----->如果字符未改变则返回
                    return;
                }
                String str = s.toString();
                isChanged = true;
                StringBuffer stringBuffer = new StringBuffer();


                for (int k = 0; k < str.length(); k++) {

                    switch (k) {
                        case 0:
                            if (str.charAt(k) == '.') {
                                stringBuffer.append(0).append(str.charAt(k));
                            } else {
                                stringBuffer.append(str.charAt(k));
                            }
                            break;
                        case 1:
                            if (stringBuffer.charAt(0) == '0' && str.charAt(k) == '0') {
                                continue;
                            } else if (stringBuffer.charAt(0) == '0' && str.charAt(k) != '.') {
                                stringBuffer.append('.');
                                stringBuffer.append(str.charAt(k));
                            } else {
                                stringBuffer.append(str.charAt(k));
                            }
                            break;
                        default:
                            if (str.charAt(k) == '.' && stringBuffer.indexOf(".", 0) > 0) {
                                continue;
                            } else if (stringBuffer.indexOf(".", 0) > 0 && stringBuffer.length() - stringBuffer.indexOf(".", 0) > 2) {
                                continue;
                            } else {
                                stringBuffer.append(str.charAt(k));
                            }
                            break;
                    }

                }

                et_money_after.setText(stringBuffer.toString());
                et_money_after.setSelection(et_money_after.length());
                stringBuffer.delete(0, stringBuffer.length());
                stringBuffer = null;
                isChanged = false;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_money_after.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                                    @Override
                                                    public void onFocusChange(View v, boolean hasFocus) {
                                                        if (!hasFocus) {
                                                            String str = et_money_after.getText().toString();

                                                            if (!TextUtils.isEmpty(str) && str.indexOf(".", 0) > 0 && str.length() - str.indexOf(".", 0) < 3) {
                                                                int ssss = str.length() - str.indexOf(".", 0) - 1;
                                                                switch (ssss) {
                                                                    case 0:
                                                                        str = str + "00";
                                                                        break;
                                                                    case 1:
                                                                        str = str + "0";
                                                                        break;
                                                                }
                                                                et_money_after.setText(str);
                                                            }
                                                        }
                                                    }
                                                }

        );

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left_raw:
                getActivity().finish();
                break;
            case R.id.rl_invocicecontent:
                break;
            case R.id.rl_address:
                if (areaDialog == null) {
                    areaDialog = new ChooseAreaDialog(mContext);
                    Window window = areaDialog.getWindow();
                    window.setGravity(Gravity.BOTTOM);
                    WindowManager.LayoutParams windowparams = window.getAttributes();
                    windowparams.width = MyUtils.getScreenWidth(mContext);
                    window.setAttributes(windowparams);
                    areaDialog.setChooseAreaListener(new ChooseAreaDialog.ChooseAreaListener() {
                        @Override
                        public void onChooseBack(String province, String city, String county) {
                            provinceStr = province;
                            cityStr = city;
                            districtStr = county;
                            tv_address_after.setText(province);
                            tv_address_after.append(city);
                            tv_address_after.append(county);
                            tv_address_after.requestFocus();
                        }
                    });
                }
                areaDialog.show();
                break;
            case R.id.tv_commit:
                et_forcer.setVisibility(View.VISIBLE);
                et_forcer.requestFocus();
                et_forcer.setVisibility(View.GONE);
                requestData(false);
                break;
            case R.id.ll_business_license://营业执照
                checkPhotoAlbumOrCameraDialog(1);
                break;
            case R.id.ll_bank_open_account_license://银行开户许可证
                checkPhotoAlbumOrCameraDialog(2);
                break;
        }
    }

    //从相册选择或使用照相机 弹窗
    private void checkPhotoAlbumOrCameraDialog(final int id) {
        //请选择支付账户弹窗
        final Dialog payDialog = new Dialog(mContext, R.style.ActionSheet);
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.dialog_check_proto_or_camera, null);
        payDialog.setCancelable(true);
        payDialog.setContentView(view);
        payDialog.show();

        TextView tv_photo_album = (TextView) view.findViewById(R.id.tv_photo_album);//相册
        TextView tv_camera = (TextView) view.findViewById(R.id.tv_camera);//相机
        //相册
        tv_photo_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payDialog.dismiss();
                //调用相册
                Intent intent1 = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent1, id);
            }
        });
        //相机
        tv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payDialog.dismiss();
                toCamera(id + 100);//这里他妈的有错误相机照相返回后显示不出来
            }
        });


        Window dlgWindow = payDialog.getWindow();
        WindowManager.LayoutParams lp = dlgWindow.getAttributes();
        lp.gravity = Gravity.CENTER;
        //lp.width = MyUtils.getScreenWidth(activity2_1) / 10 * 8;
        dlgWindow.setAttributes(lp);
    }

    private String takePictureSavePath;

    private void toCamera(int tag) {
        String saveRootPath = YYApplication.sdCardRootPath;
        if (!TextUtils.isEmpty(saveRootPath)) {
            Intent intent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            // 下面这句指定调用相机拍照后的照片存储的路径
            String timeStamp = new SimpleDateFormat(
                    "yyyyMMdd_HHmmss").format(new Date());
            File dirFil = new File(saveRootPath
                    + "/yiyi/image/imageCach/");
            if (!dirFil.exists()) {
                dirFil.mkdirs();
            }
            File makeFile = new File(dirFil, "xiaoma_"
                    + timeStamp + ".jpeg");
            takePictureSavePath = makeFile.getAbsolutePath();
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(makeFile));
            startActivityForResult(intent, tag);
        } else {
            MyUtils.showToast(mContext, "！存储设备部不可用");
        }
    }

    //处理相册相机返回信息
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("onActivityResult", requestCode + takePictureSavePath);
        //获取图片路径
        if ((requestCode == 1 || requestCode == 2) && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getActivity().getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            showImage(imagePath, requestCode);
            uploadImage(imagePath, requestCode);
            c.close();
        }
        if ((requestCode == 101 || requestCode == 102) && resultCode == Activity.RESULT_OK) {
            showImage(takePictureSavePath, requestCode);
            uploadImage(takePictureSavePath, requestCode);
        }
    }

    //加载图片
    private void showImage(String imaePath, int requestCode) {
        Bitmap bm = ImageFactory.getBitmap(imaePath);
        if (requestCode == 1 || requestCode == 101) {
            iv_business_license.setVisibility(View.VISIBLE);
            iv_business_license.setImageBitmap(bm);
            Log.e("onActivityResult", "走了添加图片");
        } else if (requestCode == 2 || requestCode == 102) {
            iv_bank_open_account_license.setVisibility(View.VISIBLE);
            iv_bank_open_account_license.setImageBitmap(bm);
        }
    }

    public void requestData(boolean isContinue) {
        if (NetHelper.checkNetwork(getActivity())) {
            showNoNetDlg();
            MyUtils.showToast(getActivity(), "网络异常，请检查网络连接或重试");
            return;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
        params.put("type", "3");//增值税发票
        params.put("title", et_makeup.getText() + "");
        if ((et_makeup.getText() + "").isEmpty()) {
            MyUtils.showToast(getActivity(), "请输入发票抬头信息");
            return;
        }

        //增值税发票新增字段__________________________________________________
        params.put("taxpayerNo", et_taxpayer_identity.getText() + "");
        if ((et_taxpayer_identity.getText() + "").isEmpty()) {
            MyUtils.showToast(getActivity(), "请输入纳税人识别号");
            return;
        }

        params.put("companyAddress", et_company_address.getText() + "");
        if ((et_company_address.getText() + "").isEmpty()) {
            MyUtils.showToast(getActivity(), "请输入公司地址");
            return;
        }

        params.put("companyMobile", et_company_phone.getText() + "");
        if ((et_company_phone.getText() + "").isEmpty()) {
            MyUtils.showToast(getActivity(), "请输入公司电话");
            return;
        }

        params.put("bankName", et_bank_name.getText() + "");
        if ((et_bank_name.getText() + "").isEmpty()) {
            MyUtils.showToast(getActivity(), "请输入开户行名称");
            return;
        }

        params.put("bankAccount", et_bank_account_number.getText() + "");
        if ((et_bank_account_number.getText() + "").isEmpty()) {
            MyUtils.showToast(getActivity(), "请输入银行账号");
            return;
        }

        if (TextUtils.isEmpty(imageMap.get("iv_business_license"))) {
            MyUtils.showToast(getActivity(), "请添加营业执照副本");
            return;
        }
        params.put("businessLicense", imageMap.get("iv_business_license"));

        if (TextUtils.isEmpty(imageMap.get("iv_bank_open_account_license"))) {
            MyUtils.showToast(getActivity(), "请添加开户行许可证");
            return;
        }
        params.put("accountPermitCertificate", imageMap.get("iv_bank_open_account_license"));

        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

        params.put("content", (tv_invocicecontent.getText() + "").substring(1, tv_invocicecontent.getText().length() - 1));
        params.put("recipient", tv_recipertxt.getText() + "");
        if ((tv_recipertxt.getText() + "").isEmpty()) {
            MyUtils.showToast(getActivity(), "请输入收件人姓名");
            return;
        }
        params.put("mobile", tv_receivephonetxt.getText() + "");
        if ((tv_receivephonetxt.getText() + "").isEmpty() || tv_receivephonetxt.getText().length() != 11) {
            MyUtils.showToast(getActivity(), "请输入正确的电话号码");
            return;
        }

        if (TextUtils.isEmpty(provinceStr)) {
            MyUtils.showToast(getActivity(), "请输选择地区");
            return;
        }
        params.put("province", provinceStr);
        params.put("city", cityStr);
        params.put("district", districtStr);
        params.put("address", et_detailaddress.getText() + "");
        if ((et_detailaddress.getText() + "").isEmpty()) {
            MyUtils.showToast(getActivity(), "请输入详细地址");
            return;
        }

//        if ("2".equals(bundle.getString("type"))) {
//            params.put("orderIdStr", bundle.getString("orderIdStr"));
//            if (Double.parseDouble(bundle.getString("amount") + "") < 200) {
//                if (!isContinue) {
//                    showChooseDialog("由于发票金额小于200元，快递需要到付，是否开开具？");
//                    return;
//                }
//            }
//            params.put("amount", bundle.getString("amount"));
//        } else if ("1".equals(bundle.getString("type"))) {
        if ((et_money_after.getText() + "").isEmpty()) {
            MyUtils.showToast(getActivity(), "请输入发票开具额度");
            return;
        }

        if (Double.parseDouble(et_money_after.getText() + "") > Double.parseDouble(bundle.getString("amount"))) {
            MyUtils.showToast(getActivity(), "您输入的额度超过发票可开具金额，请重新输入");
            return;
        }
        if (Double.parseDouble(et_money_after.getText() + "") <= 0) {
            MyUtils.showToast(getActivity(), "请输入正确的额度");
            return;
        }
        if (Double.parseDouble(et_money_after.getText() + "") < 200) {
            if (!isContinue) {
                showChooseDialog("由于发票金额小于200元，快递需要到付，是否开开具？");
                return;
            }
        }
        params.put("amount", et_money_after.getText() + "");
//
//        }
        //dialogShow();
//        YYRunner.getData(0, YYRunner.Method_POST,
//                "http://192.168.31.195:8090/yytsserver-controller/addInvoice.do", params, this);
        showConfirmInvoiceDialog(params);

    }

    @Override
    public void onError(int tag, String error) {
        dismmisDialog();
        MyUtils.showToast(getActivity(), "数据传输错误，请重试");
    }

    @Override
    public void onComplete(int tag, String json) {
        dismmisDialog();
        YYBaseResBean yyBaseResBean = GsonTransformUtil.fromJson(json, YYBaseResBean.class);
        if (yyBaseResBean.getErrno() != 0) {
            MyUtils.showToast(getActivity(), yyBaseResBean.getError());
        } else {
            Intent intent = new Intent(getActivity(),InvoiceCompileteActivity.class);
            startActivity(intent);
            getActivity().finish();
            //showMessageDialog("您的发票申请我们已经收到，稍后我们将电话和您联系并确认");
        }
    }

    /**
     * 打开拨打客服电话弹窗
     */
    public void showChooseDialog(String messge) {
        if (chooseDlg == null) {
            chooseDlg = new Dialog(mContext, R.style.MyDialog);
            View mDlgCallView = LayoutInflater.from(mContext).inflate(
                    R.layout.dlg_choose, null);
            TextView tv_cancel_txt = (TextView) mDlgCallView
                    .findViewById(R.id.tv_cancel_txt);
            tv_cancel_txt.setText("再想想");
            tv_cancel_txt.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    chooseDlg.dismiss();
                }
            });
            final TextView tv_call_txt = (TextView) mDlgCallView
                    .findViewById(R.id.tv_call_txt);
            final TextView tv_number = (TextView) mDlgCallView
                    .findViewById(R.id.tv_number);
            if (!TextUtils.isEmpty(messge)) {
                tv_number.setText(messge);
            }
            tv_call_txt.setText("继续");
            tv_call_txt.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stubt
                    requestData(true);
                    chooseDlg.dismiss();
                }
            });
            chooseDlg.setCanceledOnTouchOutside(false);
            chooseDlg.setContentView(mDlgCallView);
        }
        chooseDlg.show();

        Window dlgWindow = chooseDlg.getWindow();
        WindowManager.LayoutParams lp = dlgWindow.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = MyUtils.getScreenWidth(mContext) / 10 * 7;
        dlgWindow.setAttributes(lp);

    }

    /**
     * 打开拨打客服电话弹窗
     */
    public void showMessageDialog(String messge) {
        if (messageDlg == null) {
            messageDlg = new Dialog(mContext, R.style.MyDialog);
            messageDlg.setCanceledOnTouchOutside(false);
            messageDlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    Intent intent = new Intent(getActivity(), InvoiceIssueAty.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    messageDlg.dismiss();
                }
            });
            View mDlgCallView = LayoutInflater.from(mContext).inflate(
                    R.layout.dlg_message, null);
            TextView tv_cancel_txt = (TextView) mDlgCallView
                    .findViewById(R.id.tv_do_txt);
            tv_cancel_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    messageDlg.dismiss();
                }
            });
            final TextView tv_number = (TextView) mDlgCallView
                    .findViewById(R.id.tv_message);
            tv_number.setText(messge);
            messageDlg.setCanceledOnTouchOutside(false);
            messageDlg.setContentView(mDlgCallView);
        }
        messageDlg.show();

        Window dlgWindow = messageDlg.getWindow();
        WindowManager.LayoutParams lp = dlgWindow.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = MyUtils.getScreenWidth(mContext) / 10 * 7;
        dlgWindow.setAttributes(lp);

    }

    @Override
    public void onLoading(long count, long current) {

    }

    /**
     * 没有姓名dialog
     */
    public void showNonameDialog(String messge) {
        if (nonameDlg == null) {
            nonameDlg = new Dialog(mContext, R.style.MyDialog);
            View mDlgCallView = LayoutInflater.from(mContext).inflate(
                    R.layout.dlg_choose, null);
            TextView tv_cancel_txt = (TextView) mDlgCallView
                    .findViewById(R.id.tv_cancel_txt);
            tv_cancel_txt.setText("取消");
            tv_cancel_txt.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    getActivity().finish();
                }
            });
            final TextView tv_call_txt = (TextView) mDlgCallView
                    .findViewById(R.id.tv_call_txt);
            final TextView tv_number = (TextView) mDlgCallView
                    .findViewById(R.id.tv_number);
            if (!TextUtils.isEmpty(messge)) {
                tv_number.setText(messge);
            }
            tv_call_txt.setText("联系客服");
            tv_call_txt.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stubt
                    try {
                        MobclickAgent.onEvent(mContext, "click_call");
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri
                                .parse("tel:"
                                        + YYConstans.getSysConfig().getReturnContent().getServicePhone()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        mContext.overridePendingTransition(
                                R.anim.activity_up, R.anim.activity_push_no_anim);
                        nonameDlg.dismiss();
                        getActivity().finish();
                    } catch (Exception e) {
                        MyUtils.showToast(mContext, "请检查是否开启电话权限");
                    }

                }
            });
            nonameDlg.setCanceledOnTouchOutside(false);
            nonameDlg.setContentView(mDlgCallView);
        }
        nonameDlg.show();

        Window dlgWindow = nonameDlg.getWindow();
        WindowManager.LayoutParams lp = dlgWindow.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = MyUtils.getScreenWidth(mContext) / 10 * 7;
        dlgWindow.setAttributes(lp);
    }

    private void uploadImage(String path, final int tag) {
        dialogShow();
        if (path != null) {
            dialogShow();
            String uploadName = "sfyz/service_operation/authentication/" + YYConstans.getUserInfoBean().getReturnContent().getUser().getUserId() + "/" + System.currentTimeMillis() + ".jpg";
            final String uploadUrl = YYApplication.OSSBaseUrl + uploadName;
            PutObjectRequest putObjectRequest = new PutObjectRequest(YYApplication.bucketName, uploadName, path);
            putObjectRequest.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
                @Override
                public void onProgress(PutObjectRequest putObjectRequest, final long l, final long l1) {
                }
            });

            OSSAsyncTask asyncTask = YYApplication.getOss().asyncPutObject(putObjectRequest, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                @Override
                public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
                    baseHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dismmisDialog();
                            switch (tag) {
                                case 1:
                                case 101:
                                    imageMap.put("iv_business_license", uploadUrl);
                                    break;
                                case 2:
                                case 102:
                                    imageMap.put("iv_bank_open_account_license", uploadUrl);
                                    break;

                            }
                        }
                    }, 50);

                }

                @Override
                public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                    baseHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dismmisDialog();
                        }
                    }, 50);
                }
            });

        }
    }

    //确认发票信息
    private void showConfirmInvoiceDialog(final Map<String,String> params) {
        final Dialog pricingMannerDialog = new Dialog(mContext, R.style.ActionSheet);
        LinearLayout dialog_root_view = (LinearLayout) mContext.getLayoutInflater().inflate(
                R.layout.dialog_root_view, null);

        ImageView iv_left_raw = (ImageView) dialog_root_view.findViewById(R.id.iv_left_raw);
        ImageView iv_confirm_bitmap = (ImageView) dialog_root_view.findViewById(R.id.iv_confirm_bitmap);
        Button btn_cancle = (Button) dialog_root_view.findViewById(R.id.btn_cancle);
        Button btn_confirm = (Button) dialog_root_view.findViewById(R.id.btn_confirm);

        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pricingMannerDialog.dismiss();
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogShow();
                YYRunner.getData(0, YYRunner.Method_POST,
                        YYUrl.GETINVOICE, params, AddValueInvoice.this);
            }
        });


        iv_left_raw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pricingMannerDialog.dismiss();
            }
        });


        Bitmap bitmap = Bitmap.createBitmap(ll_cash_drowable.getWidth(), ll_cash_drowable.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        ll_cash_drowable.draw(canvas);
        iv_confirm_bitmap.setImageBitmap(bitmap);

        Window win = pricingMannerDialog.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(lp);
        pricingMannerDialog.setCancelable(true);
        pricingMannerDialog.setContentView(dialog_root_view);
        pricingMannerDialog.show();

    }

}
