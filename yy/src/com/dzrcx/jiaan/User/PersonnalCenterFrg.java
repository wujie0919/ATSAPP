package com.dzrcx.jiaan.User;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import com.dzrcx.jiaan.Bean.EnterPriseBean;
import com.dzrcx.jiaan.Bean.UserInfoBean;
import com.dzrcx.jiaan.Bean.YYBaseResBean;
import com.dzrcx.jiaan.Constans.YYConstans;
import com.dzrcx.jiaan.Constans.YYOptions;
import com.dzrcx.jiaan.Constans.YYUrl;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.YYApplication;
import com.dzrcx.jiaan.base.YYBaseFragment;
import com.dzrcx.jiaan.clicklistener.RequestInterface;
import com.dzrcx.jiaan.tools.GsonTransformUtil;
import com.dzrcx.jiaan.tools.MyUtils;
import com.dzrcx.jiaan.tools.NetHelper;
import com.dzrcx.jiaan.tools.PhotoUtil;
import com.dzrcx.jiaan.tools.SharedPreferenceTool;
import com.dzrcx.jiaan.tools.StringUtils;
import com.dzrcx.jiaan.tools.YYRunner;
import com.dzrcx.jiaan.widget.CustomRoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonnalCenterFrg extends YYBaseFragment implements View.OnClickListener, RequestInterface {
    private LayoutInflater inflater;
    private View personnalCenterView;
    private TextView rightView;
    private ImageView iv_left_raw;
    private CustomRoundImageView imageView;
    private TextView userName;
    private View userLevel;
    private TextView user_label0, user_label1, user_label2, transf, logout;
    private RelativeLayout autoLayout;

    private Dialog dialog;
    private String takePictureSavePath;
    private Uri imageUri;

    private static final int TAG_uploadimg = 169503;
    private static final int TAG_getuserinfo = 169504;

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
        }

    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        if (personnalCenterView == null) {
            personnalCenterView = LayoutInflater.from(mContext).inflate(
                    R.layout.frg_personnal_center, null);
            /**
             * title
             */
            iv_left_raw = (ImageView) personnalCenterView.findViewById(R.id.iv_left_raw);
            iv_left_raw.setOnClickListener(this);
            iv_left_raw.setVisibility(View.VISIBLE);

            imageView = (CustomRoundImageView) personnalCenterView.findViewById(R.id.center_userimage);
            userName = (TextView) personnalCenterView.findViewById(R.id.center_username);
            userLevel = personnalCenterView.findViewById(R.id.center_user_level);
            imageView.setOnClickListener(this);


            user_label0 = (TextView) personnalCenterView.findViewById(R.id.person_label0);
            user_label1 = (TextView) personnalCenterView.findViewById(R.id.person_label1);
            user_label2 = (TextView) personnalCenterView.findViewById(R.id.person_label2);
            logout = (TextView) personnalCenterView.findViewById(R.id.tv_logout);

            logout.setOnClickListener(this);

            autoLayout = (RelativeLayout) personnalCenterView.findViewById(R.id.person_layout0);

            transf = (TextView) personnalCenterView.findViewById(R.id.person_transf);
            transf.setOnClickListener(this);

        }
        return personnalCenterView;
    }


    @Override
    public void onResume() {
        super.onResume();
        getUserInfo();
        updateUserInfo();
    }

    private void updateUserInfo() {
        UserInfoBean userInfoBean = YYConstans.getUserInfoBean();
        if (userInfoBean == null)
            return;
        if (userInfoBean.getReturnContent().getSkey().isEmpty()) {
            userName.setText(null);
            userLevel.setBackgroundResource(0);
            ImageLoader.getInstance().displayImage(
                    "", imageView, YYOptions.Option_USERPHOTO);
        } else {
            ImageLoader.getInstance().displayImage(
                    userInfoBean.getReturnContent().getUser().getThumb(),
                    imageView, YYOptions.Option_USERPHOTO);
            userName.setText(userInfoBean.getReturnContent().getUser().getName() == null ? "未设置"
                    : StringUtils.formatPhone(userInfoBean
                    .getReturnContent().getUser().getName()));
        }

        switch (userInfoBean.getReturnContent().getUser().getLevel()) {
            case 10:
                userLevel.setBackgroundResource(R.drawable.vip);
                userLevel.setVisibility(View.VISIBLE);
                break;
            default:
                userLevel.setBackgroundResource(0);
                userLevel.setVisibility(View.GONE);
                break;
        }
        switch (userInfoBean.getReturnContent().getUser().getUserState()) {
            case 1:
                user_label0.setText("已认证");
                transf.setText("");
                autoLayout.setOnClickListener(null);
                break;
            case -1:
                user_label0.setText(null);
                transf.setText(" 账户迁移 >");
                user_label0.setHint("未认证");
                autoLayout.setOnClickListener(this);
                break;
            default:
                user_label0.setText(null);
                user_label0.setHint("认证中");
                autoLayout.setOnClickListener(this);
                break;
        }

        user_label1.setText(userInfoBean.getReturnContent().getUser().getName() == null ? "--"
                : StringUtils.formatPhone(userInfoBean
                .getReturnContent().getUser().getName()));

        user_label2.setText(StringUtils.formatPhone(userInfoBean
                .getReturnContent().getUser().getMobile()));


    }

    private View getBusinessView(List<EnterPriseBean> enterPriseBeans, int i) {
        View view = inflater.inflate(R.layout.childview_enterprise, null);
        View dashLine = view.findViewById(R.id.dash_line);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            dashLine.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        if (i == 0) {
            dashLine.setVisibility(View.GONE);
        }
        TextView tv_businessname = (TextView) view.findViewById(R.id.tv_businessname);
        TextView tv_number = (TextView) view.findViewById(R.id.tv_number);
        tv_businessname.setText(enterPriseBeans.get(i).getName());
        tv_number.setText(enterPriseBeans.get(i).getDeptLeft() + "");
        return view;
    }


    private void getUserInfo() {
        Map<String, String> wxparams = new HashMap<String, String>();
        wxparams.put("skey",
                YYConstans.getUserInfoBean().getReturnContent().getSkey() == null ? ""
                        : YYConstans.getUserInfoBean().getReturnContent().getSkey());
        wxparams.put("lng", YYApplication.Longitude + "");
        wxparams.put("lat", YYApplication.Latitude + "");
        YYRunner.getData(TAG_getuserinfo, YYRunner.Method_POST, YYUrl.GETUSERINFO,
                wxparams, this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_left_raw:
                getActivity().finish();
                break;
            case R.id.tv_right:
                startActivity(BreakRulesAty.class, null);
                break;
            case R.id.center_userimage:
                showBottomDialog();
                break;
            case R.id.person_layout0:
                switch (YYConstans.getUserInfoBean().getReturnContent().getUser()
                        .getUserState()) {
                    case -1:
                        startActivity(IdentificationAty.class, null);
                        break;
                    case 0:
//                                    showChooseDialog("");
                        showMessageDialog("您提交的证件照片正在审核中，请耐心等待！");
                        break;
                    case 1:
                        break;

                    default:
                        break;
                }
                break;
            case R.id.tv_logout:
                SharedPreferenceTool.setPrefString(mContext,
                        SharedPreferenceTool.KEY_USERINFO, "");
                ImageLoader.getInstance().displayImage("", imageView,
                        YYOptions.Option_USERPHOTO);
                YYConstans.setUserInfoBean(null);
                updateUserInfo();
                startActivity(LoginAty.class, null);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            getActivity().finish();
                        } catch (Exception e) {
                        }
                    }
                }, 500);
                break;
            case R.id.person_transf:
                startActivity(TransferOneAty.class, null);
                break;
        }
    }

    @Override
    public void onError(int tag, String error) {
        progresssDialog.dismiss();
    }

    @Override
    public void onComplete(int tag, String json) {

        progresssDialog.dismiss();
        switch (tag) {
            case TAG_uploadimg:
                json2Upload(json);
                break;
            case 0:
                break;
            case TAG_getuserinfo:
                UserInfoBean userInfoBean = (UserInfoBean) GsonTransformUtil
                        .fromJson(json, UserInfoBean.class);
                if (userInfoBean != null && userInfoBean.getErrno() == 0) {
                    YYConstans.setUserInfoBean(userInfoBean);
                    updateUserInfo();
                }
                break;
            case 2001:
                YYBaseResBean sfzBean = GsonTransformUtil.fromJson(json, YYBaseResBean.class);
                if (sfzBean != null && sfzBean.getErrno() == 0) {
                    MyUtils.showToast(mContext, sfzBean.getError());
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void onLoading(long count, long current) {

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
                        if (Build.VERSION.SDK_INT >= 19) {
                            imageUri = Uri.fromFile(new File(PhotoUtil.getPath(
                                    mContext, imageUri)));
                        }
                        imageUri = startPhotoZoom(imageUri);
                    }

                    break;
                // 如果是调用相机拍照时
                case 2:
                    if (takePictureSavePath != null) {
                        imageUri = Uri.fromFile(new File(takePictureSavePath));
                        imageUri = startPhotoZoom(imageUri);
                    }
                    break;
                // 取得裁剪后的图片
                case 3:
                    if (data != null) {
                        Bitmap bitmap = data.getParcelableExtra("data");
                        if (bitmap != null) {
                            //imageView.setImageBitmap(bitmap);
                            uploadImage(bitmap);
                        }
                    }
                    break;
                default:
                    break;
            }

        }

    }

    @SuppressLint("NewApi")
    private void json2Upload(String json) {
        if (!json.isEmpty())
            try {
                JSONObject jsonObject = new JSONObject(json);
                String stateMsg = jsonObject.getString("stateMsg");
                String stateCode = jsonObject.getString("stateCode");
                if (!"0".equals(stateCode)) {
                    MyUtils.showToast(mContext,
                            jsonObject.has("stateMsg") ? stateMsg : "服务器错误");
                } else {
                    JSONObject resourceInfo = jsonObject
                            .getJSONObject("resourceInfo");

                    String fileUrl = resourceInfo.getString("fileUrl");

                    YYConstans.getUserInfoBean().getReturnContent().getUser().setThumb(fileUrl);

                    ImageLoader.getInstance().displayImage(fileUrl, imageView,
                            YYOptions.Option_USERPHOTO);
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
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
        return uriFrom;

    }

    public void showBottomDialog() {
        if (dialog == null) {
            dialog = new Dialog(getActivity(), R.style.ActionSheet);
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View mDlgCallView = inflater.inflate(R.layout.dlg_useraty, null);
            final int cFullFillWidth = 10000;
            mDlgCallView.setMinimumWidth(cFullFillWidth);

            TextView tv_camera_txt = (TextView) mDlgCallView
                    .findViewById(R.id.tv_camera_txt);
            TextView tv_album_txt = (TextView) mDlgCallView
                    .findViewById(R.id.tv_album_txt);
            TextView tv_exit_txt = (TextView) mDlgCallView
                    .findViewById(R.id.tv_exit_txt);
            TextView cancel_txt = (TextView) mDlgCallView
                    .findViewById(R.id.cancel_txt);

            tv_camera_txt.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (NetHelper.checkNetwork(mContext)) {
                        showNoNetDlg();
                        MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
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
                        PersonnalCenterFrg.this.startActivityForResult(intent, 2);
                    } else {
                        MyUtils.showToast(mContext, "！存储设备部不可用");
                    }
                    dialog.dismiss();
                }
            });
            tv_album_txt.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (NetHelper.checkNetwork(mContext)) {
                        showNoNetDlg();
                        MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
                        return;
                    }
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    PersonnalCenterFrg.this.startActivityForResult(intent, 1);
                    dialog.dismiss();

                }
            });
            tv_album_txt.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
            tv_exit_txt.setVisibility(View.GONE);
            cancel_txt.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
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
        dialog.show();
    }


    private void uploadImage(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            dialogShow();

            ByteArrayOutputStream output = new ByteArrayOutputStream();//初始化一个流对象
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);//把bitmap100%高质量压缩 到 output对象里

            bitmap.recycle();//自由选择是否进行回收

            byte[] result = output.toByteArray();//转换成功了
            try {
                output.close();
            } catch (Exception e) {
                e.printStackTrace();
            }


            final String uploadName = "sfyz/service_operation/userImage/" + YYConstans.getUserInfoBean().getReturnContent().getUser().getUserId() + "/" + System.currentTimeMillis() + ".jpg";
            final String uploadUrl = YYApplication.OSSBaseUrl + uploadName;
            PutObjectRequest putObjectRequest = new PutObjectRequest(YYApplication.bucketName, uploadName, result);
            putObjectRequest.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
                @Override
                public void onProgress(PutObjectRequest putObjectRequest, long l, long l1) {
                    Log.d("TAG_PersonnalCenter","Progress");
                }
            });

            OSSAsyncTask asyncTask = YYApplication.getOss().asyncPutObject(putObjectRequest, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                @Override
                public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
                    Log.d("TAG_PersonnalCenter","Success");
                    baseHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dismmisDialog();
                            YYConstans.getUserInfoBean().getReturnContent().getUser().setThumb(uploadUrl);
                            Log.d("TAG_PersonnalCenter","Success"+uploadUrl);
                            ImageLoader.getInstance().displayImage(uploadUrl, imageView,
                                    YYOptions.Option_USERPHOTO);
                            upLoadUserImg(2001, 2, uploadUrl);
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

    private void upLoadUserImg(int tag, int type, String img) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
        params.put("flag", type + "");
        params.put("SPhoto", img);
        YYRunner.getData(tag, YYRunner.Method_POST, YYUrl.UPLOADUSERIMG,
                params, this);
    }

}
