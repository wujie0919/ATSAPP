package com.dzrcx.jiaan.User;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.dzrcx.jiaan.Constans.YYOptions;
import com.dzrcx.jiaan.Constans.YYUrl;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.SearchCar.WebAty;
import com.dzrcx.jiaan.YYApplication;
import com.dzrcx.jiaan.base.YYBaseFragment;
import com.dzrcx.jiaan.clicklistener.RequestInterface;
import com.dzrcx.jiaan.tools.GsonTransformUtil;
import com.dzrcx.jiaan.tools.MyUtils;
import com.dzrcx.jiaan.tools.NetHelper;
import com.dzrcx.jiaan.tools.PhotoUtil;
import com.dzrcx.jiaan.tools.YYRunner;
import com.dzrcx.jiaan.widget.pickerview.TimePickerDialog;
import com.dzrcx.jiaan.widget.pickerview.data.Type;
import com.dzrcx.jiaan.widget.pickerview.listener.OnDateSetListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 驾照身份证认证
 */
public class IdentificationFrg extends YYBaseFragment implements
        RequestInterface, OnClickListener, OnDateSetListener {

    private ImageView iv_left_raw;
    private TextView barTitle, authDate;

    private EditText authName, authCardNO;
    private RelativeLayout rl_auth_sfz, rl_auth_jsz;
    private ImageView iv_auth_sfz, iv_auth_jsz;
    private ImageView iv_remove_sfz, iv_remove_jsz;


    private TextView authxieyi;
    private TextView tv_commit;

    private static final int TAG_getdata = 169504;
    private static final int TAG_uploadimg = 169505;
    private static final int TAG_commit = 169506;
    private static final String TAG_String_identify = "renterIdCard_";
    private static final String TAG_String_dring = "renterLicense_";
    private Dialog dialog;
    private String takePictureSavePath;
    private Uri imageUri;
    private TimePickerDialog mDialogYearMonthDay;
    private String picTag = "";

    /**
     * -1空闲，1上次身份证，2上传驾驶证
     */
    private int upType = -1;
    private View identificationView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (identificationView == null) {
            identificationView = inflater.inflate(R.layout.aty_identify, null);
            try {
                initview();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (NetHelper.checkNetwork(mContext)) {
                MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
                return identificationView;
            }
            // dialogShow();不再显示
            // requestGetData();不再显示
            MobclickAgent.onEvent(mContext, "open_deposit");
        }
        return identificationView;
    }

    private void requestGetData() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey() == null ? ""
                : YYConstans.getUserInfoBean().getReturnContent().getSkey());
        YYRunner.getData(TAG_getdata, YYRunner.Method_POST,
                YYUrl.GETIDENTIFYPIC, params, this);
    }

    private void initview() throws ParseException {
        // TODO Auto-generated method stub

        iv_left_raw = (ImageView) identificationView.findViewById(R.id.iv_left_raw);
        iv_left_raw.setVisibility(View.VISIBLE);
        barTitle = (TextView) identificationView.findViewById(R.id.tv_title);
        barTitle.setText("身份认证");

        authName = (EditText) identificationView.findViewById(R.id.identfy_name_ed);
        authCardNO = (EditText) identificationView.findViewById(R.id.identfy_no_ed);
        authDate = (TextView) identificationView.findViewById(R.id.identfy_date_ed);

        rl_auth_sfz = (RelativeLayout) identificationView.findViewById(R.id.identfy_sfz_layout);
        rl_auth_jsz = (RelativeLayout) identificationView.findViewById(R.id.identfy_jsz_layout);

        iv_auth_sfz = (ImageView) identificationView.findViewById(R.id.identfy_sfz_iv);
        iv_auth_jsz = (ImageView) identificationView.findViewById(R.id.identfy_jsz_iv);

        iv_remove_sfz = (ImageView) identificationView.findViewById(R.id.identfy_sfz_remove);
        iv_remove_jsz = (ImageView) identificationView.findViewById(R.id.identfy_jsz_remove);

        authxieyi = (TextView) identificationView.findViewById(R.id.auth_iamge_authxieyi);
        tv_commit = (TextView) identificationView.findViewById(R.id.auth_submit);

        iv_auth_sfz.setOnClickListener(this);
        iv_auth_jsz.setOnClickListener(this);
        iv_remove_sfz.setOnClickListener(this);
        iv_remove_jsz.setOnClickListener(this);

        iv_left_raw.setOnClickListener(this);
        authxieyi.setOnClickListener(this);
        tv_commit.setOnClickListener(this);
        authDate.setOnClickListener(this);
        long years50 = 200L * 365 * 1000 * 60 * 60 * 24L;
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Long second = format.parse("19000101000000").getTime();
        mDialogYearMonthDay = new TimePickerDialog.Builder()
                .setType(Type.YEAR_MONTH_DAY)
                .setCallBack(this)
                .setTitleStringId("选择日期")
                .setYearText("")
                .setMonthText("")
                .setDayText("")
                .setCyclic(false)
                .setMinMillseconds(second)
//                .setMinMillseconds(System.currentTimeMillis() - years50)
                .setMaxMillseconds(System.currentTimeMillis())
                .setCurrentMillseconds(System.currentTimeMillis())
                .setThemeColor(getResources().getColor(R.color.bgcolor))
                .setWheelItemTextNormalColor(getResources().getColor(R.color.text_a6a6a6))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.text_3d3f42))
                .setWheelItemTextSize(13)
                .build();
        int withRL = (MyUtils.getScreenWidth(mContext) - MyUtils.dip2px(mContext, 55)) / 2;
        int heightRL = (int) (withRL * 2.0 / 3);
        RelativeLayout.LayoutParams layoutParams01 = new RelativeLayout.LayoutParams(withRL, heightRL);
        layoutParams01.setMargins(MyUtils.dip2px(mContext, 15), MyUtils.dip2px(mContext, 13), 0, 0);
        rl_auth_sfz.setLayoutParams(layoutParams01);
        RelativeLayout.LayoutParams layoutParams02 = new RelativeLayout.LayoutParams(withRL, heightRL);
        layoutParams02.setMargins(0, MyUtils.dip2px(mContext, 13), MyUtils.dip2px(mContext, 15), 0);
        layoutParams02.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        rl_auth_jsz.setLayoutParams(layoutParams02);

        iv_remove_sfz.setVisibility(View.GONE);
        iv_remove_jsz.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.iv_left_raw:
                getActivity().finish();
                break;
            case R.id.identfy_date_ed:
                mDialogYearMonthDay.show(getActivity().getSupportFragmentManager(), "year_month_day");
                break;
            case R.id.identfy_sfz_iv:
                if (v.getTag() == null) {
                    picTag = TAG_String_identify;
                    showBottomDialog(1);
                }
                break;
            case R.id.identfy_jsz_iv:
                if (v.getTag() == null) {
                    picTag = TAG_String_identify;
                    showBottomDialog(2);
                }
                break;

            case R.id.identfy_sfz_remove:
                iv_auth_sfz.setTag(null);
                iv_auth_sfz.setImageBitmap(null);
                iv_remove_sfz.setVisibility(View.GONE);
                break;
            case R.id.identfy_jsz_remove:
                iv_auth_jsz.setTag(null);
                iv_auth_jsz.setImageBitmap(null);
                iv_remove_jsz.setVisibility(View.GONE);
                break;

            case R.id.auth_iamge_authxieyi:
                startActivity(new Intent(mContext, WebAty.class).putExtra("title",
                        "租客认证协议").putExtra("url",
                        YYUrl.GETDOCUMENT + "?lng=" + YYApplication.Longitude + "&lat=" + YYApplication.Latitude + "&flag=renterAudit"));
                getActivity().overridePendingTransition(R.anim.activity_up,
                        R.anim.activity_push_no_anim);
                break;
            case R.id.auth_submit:
//                if (!authBox.isChecked()) {
//                    MyUtils.showToast(mContext, "请同意《租客认证协议》");
//                    return;
//                }
                if ((authName.getText() + "").isEmpty()) {
                    MyUtils.showToast(mContext, "请填写您的姓名");
                } else if ((authCardNO.getText() + "").isEmpty()) {
                    MyUtils.showToast(mContext, "请填写您的身份证号");
                }else if(authCardNO.getText().toString().length()<18){
                    MyUtils.showToast(mContext, "请输入18位身份号");
                }else if ((authDate.getText() + "").isEmpty()) {
                    MyUtils.showToast(mContext, "请填写您的领证日期");
                } else if (iv_auth_sfz.getTag() == null) {
                    MyUtils.showToast(mContext, "请上传您的身份证照片");
                } else if (iv_auth_jsz.getTag() == null) {
                    MyUtils.showToast(mContext, "请上传您的驾驶证照片");
                } else {
                    dialogShow();
                    upType = 0;
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
                    params.put("name", authName.getText() + "");
                    params.put("idCardNo", authCardNO.getText() + "");
                    params.put("firstIssueDate", authDate.getText() + "");
                    params.put("idCardPhoto", iv_auth_sfz.getTag().toString());
                    params.put("drivingLicense", iv_auth_jsz.getTag().toString());
                    YYRunner.getData(TAG_commit, YYRunner.Method_POST, YYUrl.GETMODIFYAUDITINFO,
                            params, this);
                }
                break;
            default:
                break;
        }
    }

    public void showBottomDialog(final int type) {
        if (dialog == null) {
            dialog = new Dialog(mContext, R.style.ActionSheet);
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View mDlgCallView = inflater.inflate(R.layout.dlg_useraty, null);
            final int cFullFillWidth = 10000;
            mDlgCallView.setMinimumWidth(cFullFillWidth);

            TextView tv_camera_txt = (TextView) mDlgCallView
                    .findViewById(R.id.tv_camera_txt);
            TextView tv_album_txt = (TextView) mDlgCallView
                    .findViewById(R.id.tv_album_txt);
            tv_album_txt.setVisibility(View.GONE);
            TextView tv_exit_txt = (TextView) mDlgCallView
                    .findViewById(R.id.tv_exit_txt);
            tv_exit_txt.setText("选择已有照片");
            TextView cancel_txt = (TextView) mDlgCallView
                    .findViewById(R.id.cancel_txt);

            tv_camera_txt.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (NetHelper.checkNetwork(mContext)) {
                        MyUtils.showToast(mContext,
                                "网络异常，请检查网络连接或重试");
                        return;
                    }
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
                        File makeFile = new File(saveRootPath
                                + "/yiyi/image/imageCach/", "xiaoma_"
                                + timeStamp + ".jpeg");
                        takePictureSavePath = makeFile.getAbsolutePath();
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(makeFile));
                        IdentificationFrg.this
                                .startActivityForResult(intent, 2);
                    } else {
                        MyUtils.showToast(mContext, "！存储设备部不可用");
                    }
                    dialog.dismiss();
                }
            });
            tv_exit_txt.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (NetHelper.checkNetwork(mContext)) {
                        MyUtils.showToast(mContext
                                ,
                                "网络异常，请检查网络连接或重试");
                        return;
                    }
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    IdentificationFrg.this.startActivityForResult(intent, 1);
                    dialog.dismiss();
                }
            });
            cancel_txt.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    picTag = "";
                    dialog.dismiss();
                }
            });
            Window w = dialog.getWindow();
            WindowManager.LayoutParams lp = w.getAttributes();
            lp.x = 0;
            final int cMakeBottom = -1000;
            lp.y = cMakeBottom;
            lp.gravity = Gravity.BOTTOM;
            dialog.onWindowAttributesChanged(lp);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            dialog.setContentView(mDlgCallView);
        }
        upType = type;
        dialog.show();
    }

    @Override
    public void onError(int tag, String error) {
        // TODO Auto-generated method stub
        MyUtils.showToast(mContext, "数据传输错误请重试");
        progresssDialog.dismiss();

        switch (tag) {
            case 2001:
//                up_sfz.setSelected(false);
//                up_sfz.setText("点击上传身份证");
//                up_sfz_p.setProgress(0);
                break;
            case 2002:
                // 上传驾驶证
//                up_jsz.setSelected(false);
//                up_jsz.setText("点击上传驾驶证（正副本）");
//                up_jsz_p.setProgress(0);
                break;
        }

    }

    @SuppressLint("NewApi")
    @Override
    public void onComplete(int tag, String json) {
        // TODO Auto-generated method stub
        progresssDialog.dismiss();
        switch (tag) {
            case TAG_getdata:
                json2GetData(json);
                break;
            case TAG_uploadimg:
                break;
            case TAG_commit:
                handlerCommit(json);
                break;
            case 2001:
                YYBaseResBean sfzBean = GsonTransformUtil.fromJson(json, YYBaseResBean.class);
                if (sfzBean != null && sfzBean.getErrno() == 0) {
//                    up_sfz.setSelected(true);
//                    // up_jsz.setSelected(false);
//                    up_sfz.setText("已完成");
//                    up_sfz_p.setProgress(100);
//
//                    up_jsz.setSelected(true);
//                    // up_jsz.setSelected(false);
//                    up_jsz.setText("已完成");
//                    up_jsz_p.setProgress(100);
                } else {
//                    up_sfz.setTag(null);
//                    up_sfz.setSelected(false);
//                    // up_jsz.setSelected(false);
//                    up_sfz.setText("点击上传身份证");
//                    up_sfz_p.setProgress(0);
//
//                    // 上传驾驶证
//                    // up_sfz.setSelected(false);
//                    up_jsz.setTag(null);
//                    up_jsz.setSelected(false);
//                    up_jsz.setText("点击上传驾驶证（正副本）");
//                    up_jsz_p.setProgress(0);
                }
                break;
            default:
                break;
        }

    }

    private void handlerCommit(String json) {
        YYBaseResBean baseResBean = (YYBaseResBean) GsonTransformUtil.fromJson(json,
                YYBaseResBean.class);
        if (baseResBean != null && baseResBean.getErrno() == 0) {
            activity = "Identification";
            MyUtils.showToast(mContext, "提交成功");
            getActivity().finish();
        } else if (baseResBean != null) {
            MyUtils.showToast(mContext, baseResBean.getError());
        }
    }


    @SuppressLint("NewApi")
    private void json2GetData(String json) {
        if (!json.isEmpty())
            try {
                JSONObject jsonObject = new JSONObject(json);
                if (jsonObject.has("errno")) {
                    String idcarPhoto = jsonObject.getString("idcarPhoto");
                    String drivelicensePhoto = jsonObject
                            .getString("drivelicensePhoto");
                    if (!idcarPhoto.isEmpty()) {
                        showIdentifyPic(null, idcarPhoto);
                    }
                    if (!drivelicensePhoto.isEmpty()) {
                        showDrivingPic(null, drivelicensePhoto);
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }

    /**
     * 头像裁剪 图片方法实现
     *
     * @param uriFrom
     */
    public Uri startPhotoZoom(Uri uriFrom) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uriFrom, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 4);
        intent.putExtra("aspectY", 3);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 400);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
        return uriFrom;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                // 如果是直接从相册获取
                case 1:
                    if (data != null) {
                        imageUri = data.getData();
//                        if (Build.VERSION.SDK_INT >= 19) {
                        imageUri = Uri.fromFile(new File(PhotoUtil.getPath(
                                mContext, imageUri)));
//                        }
                        uploadImage(imageUri.getEncodedPath());
//                        imageUri = startPhotoZoom(imageUri);
                    }

                    break;
                // 如果是调用相机拍照时
                case 2:
                    if (takePictureSavePath != null) {
                        imageUri = Uri.fromFile(new File(takePictureSavePath));
                        uploadImage(imageUri.getEncodedPath());
//                        imageUri = startPhotoZoom(imageUri);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void showIdentifyPic(Bitmap bitmap, String uri) {
//        rcv_identifycard.setScaleType(ScaleType.CENTER_CROP);
//        if (bitmap != null)
//            rcv_identifycard.setImageBitmap(bitmap);
//        else {
//            ImageLoader.getInstance().displayImage(uri, rcv_identifycard,
//                    YYOptions.Option_CARDETAIL);
//        }
    }

    private void showDrivingPic(Bitmap bitmap, String uri) {
//        rcv_drivingcard.setScaleType(ScaleType.CENTER_CROP);
//        if (bitmap != null)
//            rcv_drivingcard.setImageBitmap(bitmap);
//        else {
//            ImageLoader.getInstance().displayImage(uri, rcv_drivingcard,
//                    YYOptions.Option_CARDETAIL);
//        }
    }


    @Override
    public void onLoading(long count, long current) {
        // TODO Auto-generated method stub


    }

    private void uploadImage(String path) {
        if (path != null) {
            dialogShow();
            String uploadName = "sfyz/service_operation/authentication/" + YYConstans.getUserInfoBean().getReturnContent().getUser().getUserId() + "/" + System.currentTimeMillis() + ".jpg";
            final String uploadUrl = YYApplication.OSSBaseUrl + uploadName;
            PutObjectRequest putObjectRequest = new PutObjectRequest(YYApplication.bucketName, uploadName, path);
            putObjectRequest.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
                @Override
                public void onProgress(PutObjectRequest putObjectRequest, final long l, final long l1) {
                    baseHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            int va0 = (int) ((l * 1.0 / l1) * 100);
                            switch (upType) {
                                case 1:
//                                    // 上传身份证
//                                    up_sfz.setSelected(true);
//                                    // up_jsz.setSelected(false);
//                                    up_sfz.setText(va0 + "%");
//                                    up_sfz_p.setProgress(va0);
                                    break;
                                case 2:
//                                    // 上传驾驶证
//                                    // up_sfz.setSelected(false);
//                                    up_jsz.setSelected(true);
//                                    up_jsz.setText(va0 + "%");
//                                    up_jsz_p.setProgress(va0);
                                    break;

                                default:
                                    break;
                            }
                        }
                    }, 5);


                }
            });

            OSSAsyncTask asyncTask = YYApplication.getOss().asyncPutObject(putObjectRequest, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                @Override
                public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
                    Log.d("TAG_Identification","Success:"+uploadUrl);
                    baseHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dismmisDialog();
                            switch (upType) {
                                case 1:
//                                    // 上传身份证
//                                    up_sfz.setTag(uploadUrl);
//                                    up_sfz.setSelected(true);
//                                    // up_jsz.setSelected(false);
//                                    up_sfz.setText("已上传成功");
//                                    up_sfz_p.setProgress(100);
//                                    upLoadUserImg();
                                    iv_remove_sfz.setVisibility(View.VISIBLE);
                                    iv_auth_sfz.setTag(uploadUrl);

                                    if (!TextUtils.isEmpty(uploadUrl) && uploadUrl.contains("oss")) {
                                        ImageLoader.getInstance().displayImage(uploadUrl, iv_auth_sfz, YYOptions.OPTION_DEF);
                                    } else {
                                        ImageLoader.getInstance().displayImage(uploadUrl, iv_auth_sfz,
                                                YYOptions.OPTION_DEF);
                                    }


                                    break;
                                case 2:
//                                    // 上传驾驶证
//                                    // up_sfz.setSelected(false);
//                                    up_jsz.setTag(uploadUrl);
//                                    up_jsz.setSelected(true);
//                                    up_jsz.setText("已上传成功");
//                                    up_jsz_p.setProgress(100);
//                                    upLoadUserImg();
                                    iv_remove_jsz.setVisibility(View.VISIBLE);
                                    iv_auth_jsz.setTag(uploadUrl);

//                                    if (!TextUtils.isEmpty(uploadUrl) && uploadUrl.contains("oss")) {
//                                        ImageLoader.getInstance().displayImage(uploadUrl.replace("oss", "img") + "@300w_200h_1e_1c_6-2ci.jpg", iv_auth_jsz, YYOptions.OPTION_DEF);
//                                    } else {
                                        ImageLoader.getInstance().displayImage(uploadUrl, iv_auth_jsz,
                                                YYOptions.OPTION_DEF);
//                                    }

                                    break;

                                default:
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
                            switch (upType) {
                                case 1:
//                                    // 上传身份证
//                                    up_sfz.setTag(null);
//                                    up_sfz.setSelected(false);
//                                    // up_jsz.setSelected(false);
//                                    up_sfz.setText("点击上传身份证");
//                                    up_sfz_p.setProgress(0);

                                    iv_remove_sfz.setVisibility(View.GONE);
                                    iv_auth_sfz.setTag(null);
                                    iv_auth_sfz.setImageBitmap(null);

                                    break;
                                case 2:
//                                    // 上传驾驶证
//                                    // up_sfz.setSelected(false);
//                                    up_jsz.setTag(null);
//                                    up_jsz.setSelected(false);
//                                    up_jsz.setText("点击上传驾驶证（正副本）");
//                                    up_jsz_p.setProgress(0);
                                    iv_remove_jsz.setVisibility(View.GONE);
                                    iv_auth_jsz.setTag(null);
                                    iv_auth_jsz.setImageBitmap(null);
                                    break;

                                default:
                                    break;
                            }
                        }
                    }, 50);
                }
            });

        }
    }


    private void upLoadUserImg() {

//        String sfzStr = (String) up_sfz.getTag();
//        String jszStr = (String) up_jsz.getTag();
//
//        if (!TextUtils.isEmpty(sfzStr) && !TextUtils.isEmpty(jszStr)) {
//            Map<String, String> params = new HashMap<String, String>();
//            params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
//            params.put("flag", "1");
//            params.put("idCard", sfzStr);
//            params.put("drivingLicence", jszStr);
//            YYRunner.getData(2001, YYRunner.Method_POST, YYUrl.UPLOADUSERIMG,
//                    params, this);
//        }
    }


    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        String text = getDateToString(millseconds, "yyyy-MM-dd");
        authDate.setText(text);
    }

    public String getDateToString(long time, String fomatestr) {
        SimpleDateFormat sf = new SimpleDateFormat(fomatestr);
        Date d = new Date(time);
        return sf.format(d);
    }
}
