package com.dzrcx.jiaan.Order;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.dzrcx.jiaan.YYApplication;
import com.dzrcx.jiaan.base.YYBaseFragment;
import com.dzrcx.jiaan.clicklistener.RequestInterface;
import com.dzrcx.jiaan.tools.GsonTransformUtil;
import com.dzrcx.jiaan.tools.MyUtils;
import com.dzrcx.jiaan.tools.YYRunner;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangyu on 16-9-27.
 */
public class CheakCarFrag extends YYBaseFragment implements View.OnClickListener, RequestInterface {


    private View contentView;

    private ImageView iv_left_raw;
    private TextView titleView,tv_right;
    private TextView checkToast;

    private RelativeLayout itemLayout0, itemLayout1, itemLayout2, itemLayout3, itemLayout4, itemLayout5;
    private ImageView imamge0, imamge1, imamge2, imamge3, imamge4, imamge5;
    private ImageView remove0, remove1, remove2, remove3, remove4, remove5;

    private EditText edText;
    private TextView submit;

    private String takePictureSavePath;
    private Uri imageUri;


    private String orderID;
    /**
     * 1—开车前
     * 2—还车后
     */
    private String photoScene;
    private Intent intent;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.aty_checkcar, null);
            initView();
        }
        return contentView;
    }


    private void initView() {
        iv_left_raw = (ImageView) contentView.findViewById(R.id.iv_left_raw);
        iv_left_raw.setVisibility(View.VISIBLE);
        titleView = (TextView) contentView.findViewById(R.id.tv_title);
        titleView.setText("车辆检验");
        checkToast = (TextView) contentView.findViewById(R.id.checkcar_toast);
        tv_right = (TextView) contentView.findViewById(R.id.tv_right);
        tv_right.setVisibility(View.VISIBLE);
        itemLayout0 = (RelativeLayout) contentView.findViewById(R.id.checkcar_item_0);
        itemLayout1 = (RelativeLayout) contentView.findViewById(R.id.checkcar_item_1);
        itemLayout2 = (RelativeLayout) contentView.findViewById(R.id.checkcar_item_2);
        itemLayout3 = (RelativeLayout) contentView.findViewById(R.id.checkcar_item_3);
        itemLayout4 = (RelativeLayout) contentView.findViewById(R.id.checkcar_item_4);
        itemLayout5 = (RelativeLayout) contentView.findViewById(R.id.checkcar_item_5);

        imamge0 = (ImageView) contentView.findViewById(R.id.checkcar_item_image_0);
        imamge1 = (ImageView) contentView.findViewById(R.id.checkcar_item_image_1);
        imamge2 = (ImageView) contentView.findViewById(R.id.checkcar_item_image_2);
        imamge3 = (ImageView) contentView.findViewById(R.id.checkcar_item_image_3);
        imamge4 = (ImageView) contentView.findViewById(R.id.checkcar_item_image_4);
        imamge5 = (ImageView) contentView.findViewById(R.id.checkcar_item_image_5);

        remove0 = (ImageView) contentView.findViewById(R.id.checkcar_item_remove_0);
        remove1 = (ImageView) contentView.findViewById(R.id.checkcar_item_remove_1);
        remove2 = (ImageView) contentView.findViewById(R.id.checkcar_item_remove_2);
        remove3 = (ImageView) contentView.findViewById(R.id.checkcar_item_remove_3);
        remove4 = (ImageView) contentView.findViewById(R.id.checkcar_item_remove_4);
        remove5 = (ImageView) contentView.findViewById(R.id.checkcar_item_remove_5);

        edText = (EditText) contentView.findViewById(R.id.checkcar_item_ed);
        submit = (TextView) contentView.findViewById(R.id.checkcar_submit);

        iv_left_raw.setOnClickListener(this);
        submit.setOnClickListener(this);


        imamge0.setOnClickListener(this);
        imamge1.setOnClickListener(this);
        imamge2.setOnClickListener(this);
        imamge3.setOnClickListener(this);
        imamge4.setOnClickListener(this);
        imamge5.setOnClickListener(this);
        remove0.setOnClickListener(this);
        remove1.setOnClickListener(this);
        remove2.setOnClickListener(this);
        remove3.setOnClickListener(this);
        remove4.setOnClickListener(this);
        remove5.setOnClickListener(this);

        tv_right.setText("跳过");
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        intent = getActivity().getIntent();
        if (intent.getExtras() != null) {
            orderID = getActivity().getIntent().getExtras().getString("orderID");
            photoScene = getActivity().getIntent().getExtras().getString("photoScene");
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_left_raw:
                getActivity().finish();
                break;
            case R.id.tv_right:
                getActivity().finish();
                break;
            case R.id.checkcar_submit:
                submit();
                break;
            case R.id.checkcar_item_image_0:
                toCamera(0);
                break;
            case R.id.checkcar_item_image_1:
                toCamera(1);
                break;
            case R.id.checkcar_item_image_2:
                toCamera(2);
                break;
            case R.id.checkcar_item_image_3:
                toCamera(3);
                break;
            case R.id.checkcar_item_image_4:
                toCamera(4);
                break;
            case R.id.checkcar_item_image_5:
                toCamera(5);
                break;
            case R.id.checkcar_item_remove_0:
                removeImage(0);
                break;
            case R.id.checkcar_item_remove_1:
                removeImage(1);
                break;
            case R.id.checkcar_item_remove_2:
                removeImage(2);
                break;
            case R.id.checkcar_item_remove_3:
                removeImage(3);
                break;
            case R.id.checkcar_item_remove_4:
                removeImage(4);
                break;
            case R.id.checkcar_item_remove_5:
                removeImage(5);
                break;

        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 0:
                    if (takePictureSavePath != null) {
                        imageUri = Uri.fromFile(new File(takePictureSavePath));
                        uploadImage(imageUri.getEncodedPath(), 0);
                    }
                    break;
                case 1:
                    if (takePictureSavePath != null) {
                        imageUri = Uri.fromFile(new File(takePictureSavePath));
                        uploadImage(imageUri.getEncodedPath(), 1);
                    }
                    break;
                case 2:
                    if (takePictureSavePath != null) {
                        imageUri = Uri.fromFile(new File(takePictureSavePath));
                        uploadImage(imageUri.getEncodedPath(), 2);
                    }
                    break;
                case 3:
                    if (takePictureSavePath != null) {
                        imageUri = Uri.fromFile(new File(takePictureSavePath));
                        uploadImage(imageUri.getEncodedPath(), 3);
                    }
                    break;
                case 4:
                    if (takePictureSavePath != null) {
                        imageUri = Uri.fromFile(new File(takePictureSavePath));
                        uploadImage(imageUri.getEncodedPath(), 4);
                    }
                    break;
                case 5:
                    if (takePictureSavePath != null) {
                        imageUri = Uri.fromFile(new File(takePictureSavePath));
                        uploadImage(imageUri.getEncodedPath(), 5);
                    }
                    break;
            }
        }


    }

    @Override
    public void onError(int tag, String error) {
        dismmisDialog();
    }

    @Override
    public void onComplete(int tag, String json) {
        dismmisDialog();
        switch (tag) {
            case 1001:
                YYBaseResBean sfzBean = GsonTransformUtil.fromJson(json, YYBaseResBean.class);
                if (sfzBean != null && sfzBean.getErrno() == 0) {
                    MyUtils.showToast(mContext, sfzBean.getError());
                    getActivity().finish();
                } else if (sfzBean != null) {
                    MyUtils.showToast(mContext, sfzBean.getError());
                }
                break;
        }
    }

    @Override
    public void onLoading(long count, long current) {

    }

    private void removeImage(int tag) {
        switch (tag) {
            case 0:
                imamge0.setTag(null);
                imamge0.setImageBitmap(null);
                remove0.setVisibility(View.GONE);
                break;
            case 1:
                imamge1.setTag(null);
                imamge1.setImageBitmap(null);
                remove1.setVisibility(View.GONE);
                break;
            case 2:
                imamge2.setTag(null);
                imamge2.setImageBitmap(null);
                remove2.setVisibility(View.GONE);
                break;
            case 3:
                imamge3.setTag(null);
                imamge3.setImageBitmap(null);
                remove3.setVisibility(View.GONE);
                break;
            case 4:
                imamge4.setTag(null);
                imamge4.setImageBitmap(null);
                remove4.setVisibility(View.GONE);
                break;
            case 5:
                imamge5.setTag(null);
                imamge5.setImageBitmap(null);
                remove5.setVisibility(View.GONE);
                break;

            default:
                break;
        }
    }


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
            File makeFile = new File(saveRootPath
                    + "/yiyi/image/imageCach/", "xiaoma_"
                    + timeStamp + ".jpeg");
            takePictureSavePath = makeFile.getAbsolutePath();
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(makeFile));
            startActivityForResult(intent, tag);
        } else {
            MyUtils.showToast(mContext, "！存储设备部不可用");
        }
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
            switch (tag) {
                case 0:
                    imamge0.setTag(uploadUrl);
                    remove0.setVisibility(View.VISIBLE);
//                                    if (!TextUtils.isEmpty(uploadUrl) && uploadUrl.contains("oss")) {
//                                        ImageLoader.getInstance().displayImage(uploadUrl.replace("oss", "img") + "@120w_120h_1e_1c_6-2ci.jpg", imamge0, YYOptions.OPTION_DEF);
//                                    } else {
                    ImageLoader.getInstance().displayImage(uploadUrl, imamge0,
                            YYOptions.OPTION_DEF);
//                                    }
                    break;
                case 1:
                    imamge1.setTag(uploadUrl);
                    remove1.setVisibility(View.VISIBLE);
//                                    if (!TextUtils.isEmpty(uploadUrl) && uploadUrl.contains("oss")) {
//                                        ImageLoader.getInstance().displayImage(uploadUrl.replace("oss", "img") + "@120w_120h_1e_1c_6-2ci.jpg", imamge1, YYOptions.OPTION_DEF);
//                                    } else {
                    ImageLoader.getInstance().displayImage(uploadUrl, imamge1,
                            YYOptions.OPTION_DEF);
//                                    }
                    break;
                case 2:
                    imamge2.setTag(uploadUrl);
                    remove2.setVisibility(View.VISIBLE);
//                                    if (!TextUtils.isEmpty(uploadUrl) && uploadUrl.contains("oss")) {
//                                        ImageLoader.getInstance().displayImage(uploadUrl.replace("oss", "img") + "@120w_120h_1e_1c_6-2ci.jpg", imamge2, YYOptions.OPTION_DEF);
//                                    } else {
                    ImageLoader.getInstance().displayImage(uploadUrl, imamge2,
                            YYOptions.OPTION_DEF);
//                                    }
                    break;
                case 3:
                    imamge3.setTag(uploadUrl);
                    remove3.setVisibility(View.VISIBLE);
//                                    if (!TextUtils.isEmpty(uploadUrl) && uploadUrl.contains("oss")) {
//                                        ImageLoader.getInstance().displayImage(uploadUrl.replace("oss", "img") + "@120w_120h_1e_1c_6-2ci.jpg", imamge3, YYOptions.OPTION_DEF);
//                                    } else {
                    ImageLoader.getInstance().displayImage(uploadUrl, imamge3,
                            YYOptions.OPTION_DEF);
//                                    }
                    break;
                case 4:
                    imamge4.setTag(uploadUrl);
                    remove4.setVisibility(View.VISIBLE);
//                                    if (!TextUtils.isEmpty(uploadUrl) && uploadUrl.contains("oss")) {
//                                        ImageLoader.getInstance().displayImage(uploadUrl.replace("oss", "img") + "@120w_120h_1e_1c_6-2ci.jpg", imamge4, YYOptions.OPTION_DEF);
//                                    } else {
                    ImageLoader.getInstance().displayImage(uploadUrl, imamge4,
                            YYOptions.OPTION_DEF);
//                                    }
                    break;
                case 5:
                    imamge5.setTag(uploadUrl);
                    remove5.setVisibility(View.VISIBLE);
//                                    if (!TextUtils.isEmpty(uploadUrl) && uploadUrl.contains("oss")) {
//                                        ImageLoader.getInstance().displayImage(uploadUrl.replace("oss", "img") + "@120w_120h_1e_1c_6-2ci.jpg", imamge5, YYOptions.OPTION_DEF);
//                                    } else {
                    ImageLoader.getInstance().displayImage(uploadUrl, imamge5,
                            YYOptions.OPTION_DEF);
//                                    }
                    break;

                default:
                    break;
            }
            OSSAsyncTask asyncTask = YYApplication.getOss().asyncPutObject(putObjectRequest, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                @Override
                public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
                    baseHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dismmisDialog();
                            switch (tag) {
                                case 0:
                                    imamge0.setTag(uploadUrl);
                                    remove0.setVisibility(View.VISIBLE);
//                                    if (!TextUtils.isEmpty(uploadUrl) && uploadUrl.contains("oss")) {
//                                        ImageLoader.getInstance().displayImage(uploadUrl.replace("oss", "img") + "@120w_120h_1e_1c_6-2ci.jpg", imamge0, YYOptions.OPTION_DEF);
//                                    } else {
                                        ImageLoader.getInstance().displayImage(uploadUrl, imamge0,
                                                YYOptions.OPTION_DEF);
//                                    }
                                    break;
                                case 1:
                                    imamge1.setTag(uploadUrl);
                                    remove1.setVisibility(View.VISIBLE);
//                                    if (!TextUtils.isEmpty(uploadUrl) && uploadUrl.contains("oss")) {
//                                        ImageLoader.getInstance().displayImage(uploadUrl.replace("oss", "img") + "@120w_120h_1e_1c_6-2ci.jpg", imamge1, YYOptions.OPTION_DEF);
//                                    } else {
                                        ImageLoader.getInstance().displayImage(uploadUrl, imamge1,
                                                YYOptions.OPTION_DEF);
//                                    }
                                    break;
                                case 2:
                                    imamge2.setTag(uploadUrl);
                                    remove2.setVisibility(View.VISIBLE);
//                                    if (!TextUtils.isEmpty(uploadUrl) && uploadUrl.contains("oss")) {
//                                        ImageLoader.getInstance().displayImage(uploadUrl.replace("oss", "img") + "@120w_120h_1e_1c_6-2ci.jpg", imamge2, YYOptions.OPTION_DEF);
//                                    } else {
                                        ImageLoader.getInstance().displayImage(uploadUrl, imamge2,
                                                YYOptions.OPTION_DEF);
//                                    }
                                    break;
                                case 3:
                                    imamge3.setTag(uploadUrl);
                                    remove3.setVisibility(View.VISIBLE);
//                                    if (!TextUtils.isEmpty(uploadUrl) && uploadUrl.contains("oss")) {
//                                        ImageLoader.getInstance().displayImage(uploadUrl.replace("oss", "img") + "@120w_120h_1e_1c_6-2ci.jpg", imamge3, YYOptions.OPTION_DEF);
//                                    } else {
                                        ImageLoader.getInstance().displayImage(uploadUrl, imamge3,
                                                YYOptions.OPTION_DEF);
//                                    }
                                    break;
                                case 4:
                                    imamge4.setTag(uploadUrl);
                                    remove4.setVisibility(View.VISIBLE);
//                                    if (!TextUtils.isEmpty(uploadUrl) && uploadUrl.contains("oss")) {
//                                        ImageLoader.getInstance().displayImage(uploadUrl.replace("oss", "img") + "@120w_120h_1e_1c_6-2ci.jpg", imamge4, YYOptions.OPTION_DEF);
//                                    } else {
                                        ImageLoader.getInstance().displayImage(uploadUrl, imamge4,
                                                YYOptions.OPTION_DEF);
//                                    }
                                    break;
                                case 5:
                                    imamge5.setTag(uploadUrl);
                                    remove5.setVisibility(View.VISIBLE);
//                                    if (!TextUtils.isEmpty(uploadUrl) && uploadUrl.contains("oss")) {
//                                        ImageLoader.getInstance().displayImage(uploadUrl.replace("oss", "img") + "@120w_120h_1e_1c_6-2ci.jpg", imamge5, YYOptions.OPTION_DEF);
//                                    } else {
                                        ImageLoader.getInstance().displayImage(uploadUrl, imamge5,
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
                            removeImage(tag);
                        }
                    }, 50);
                }
            });

        }
    }


    private void submit() {

        StringBuffer buffer = new StringBuffer();
        if (imamge0.getTag() != null) {
            buffer.append(imamge0.getTag()).append(";");
        }
        if (imamge1.getTag() != null) {
            buffer.append(imamge1.getTag()).append(";");
        }
        if (imamge2.getTag() != null) {
            buffer.append(imamge2.getTag()).append(";");
        }
        if (imamge3.getTag() != null) {
            buffer.append(imamge3.getTag()).append(";");
        }
        if (imamge4.getTag() != null) {
            buffer.append(imamge4.getTag()).append(";");
        }
        if (imamge5.getTag() != null) {
            buffer.append(imamge5.getTag());
        }

        String message = edText.getText().toString();

        if (TextUtils.isEmpty(buffer.toString())) {
            MyUtils.showToast(mContext, "请上传拍照照片");
            return;
        }
//        if (TextUtils.isEmpty(message)) {
//            MyUtils.showToast(mContext, "请上传问题描述");
//            return;
//        }

        dialogShow();
        Map<String, String> params = new HashMap<String, String>();
        params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
        params.put("userId", YYConstans.getUserInfoBean().getReturnContent().getUser().getUserId() + "");
        params.put("orderId", orderID + "");
        params.put("photoScene", photoScene + "");
        params.put("photo", buffer.toString() + "");
        params.put("comment", message + "");
        YYRunner.getData(1001, YYRunner.Method_POST, YYUrl.UPLOADORDERPHOTO,
                params, this);
    }


}
