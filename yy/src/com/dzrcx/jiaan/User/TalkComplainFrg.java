package com.dzrcx.jiaan.User;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.dzrcx.jiaan.Bean.ComplainTypeBean;
import com.dzrcx.jiaan.Bean.ComplainVo;
import com.dzrcx.jiaan.Bean.YYBaseResBean;
import com.dzrcx.jiaan.Constans.YYConstans;
import com.dzrcx.jiaan.Constans.YYUrl;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.R.layout;
import com.dzrcx.jiaan.YYApplication;
import com.dzrcx.jiaan.base.YYBaseFragment;
import com.dzrcx.jiaan.clicklistener.RequestInterface;
import com.dzrcx.jiaan.tools.GsonTransformUtil;
import com.dzrcx.jiaan.tools.MyUtils;
import com.dzrcx.jiaan.tools.NetHelper;
import com.dzrcx.jiaan.tools.PhotoUtil;
import com.dzrcx.jiaan.tools.SharedPreferenceTool;
import com.dzrcx.jiaan.tools.YYRunner;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TalkComplainFrg extends YYBaseFragment implements OnClickListener,
        RequestInterface {

    private View talkcomplainView;
    private ImageView iv_left_raw;
    private TextView tv_title;

    private TextView servicetype;
    private EditText editText;

    private GridView gridView;

    private TextView submit;

    private TalkComplainAdp talkComplainAdp;


    private Dialog dialog, complainDialog;
    private String takePictureSavePath;
    private Uri imageUri;

    private ServiceTypeClick typeClick;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        if (talkcomplainView == null) {
            talkcomplainView = LayoutInflater.from(mContext).inflate(
                    R.layout.frg_talkcomplain, null);
            iv_left_raw = (ImageView) talkcomplainView.findViewById(R.id.iv_left_raw);
            iv_left_raw.setVisibility(View.VISIBLE);
            tv_title = (TextView) talkcomplainView.findViewById(R.id.tv_title);
            tv_title.setText("吐槽星辰");

            servicetype = (TextView) talkcomplainView.findViewById(R.id.tv_talk_servicetype);
            editText = (EditText) talkcomplainView.findViewById(R.id.talk_ed);
            gridView = (GridView) talkcomplainView.findViewById(R.id.talk_grid);
            submit = (TextView) talkcomplainView.findViewById(R.id.talk_submit);

            talkComplainAdp = new TalkComplainAdp(this);
            talkComplainAdp.getImgs().clear();
            gridView.setAdapter(talkComplainAdp);

            iv_left_raw.setOnClickListener(this);
            servicetype.setOnClickListener(this);
            submit.setOnClickListener(this);
            showAllComplain();
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }
        return talkcomplainView;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {

            case R.id.iv_left_raw:
                getActivity().finish();

                break;

            case R.id.tv_talk_servicetype:
                startActivityForResult(ChooseSeciveTypeAty.class, null, 11);
//                showComplainDialog();
                break;
            case R.id.talk_submit:
                addFeedBack();
                break;
            default:
                break;
        }

    }


    @Override
    public void onError(int tag, String error) {
        // TODO Auto-generated method stub
        progresssDialog.dismiss();
        MyUtils.showToast(mContext, "数据传输错误，请重试");
    }

    @Override
    public void onComplete(int tag, String json) {
        // TODO Auto-generated method stub
        progresssDialog.dismiss();
        switch (tag) {
            case 1001:

                ComplainTypeBean spitslotBean = (ComplainTypeBean) GsonTransformUtil.fromJson(json, ComplainTypeBean.class);
                if (spitslotBean != null && spitslotBean.getErrno() == 0) {
                    SharedPreferenceTool.setPrefString(mContext, SharedPreferenceTool.KEY_SHOWALLCOMPLAINTYPE, json);
                }
                break;
            case 2001:
                YYBaseResBean baseResBean = GsonTransformUtil.fromJson(json, YYBaseResBean.class);
                if (baseResBean != null && baseResBean.getErrno() == 0) {
                    MyUtils.showToast(mContext, baseResBean.getError());
                    getActivity().finish();
                } else if (baseResBean != null) {
                    MyUtils.showToast(mContext, baseResBean.getError());
                }

                break;
        }


    }

    @Override
    public void onLoading(long count, long current) {
        // TODO Auto-generated method stub

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

                case 11:
                    if (data != null) {
                        ComplainVo vo = (ComplainVo) data.getSerializableExtra("ComplainVo");
                        if (vo != null) {
                            servicetype.setText(vo.getComplainName());
                            servicetype.setTag(vo);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }


    public void addImage() {
        showBottomDialog();
    }


    public void showBottomDialog() {
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
                        TalkComplainFrg.this
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
                    TalkComplainFrg.this.startActivityForResult(intent, 1);
                    dialog.dismiss();
                }
            });
            cancel_txt.setOnClickListener(new OnClickListener() {

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

    public void showComplainDialog() {
        if (complainDialog == null) {
            complainDialog = new Dialog(mContext, R.style.ActionSheet);
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View mDlgCallView = inflater.inflate(layout.dlg_base_list_cancel, null);
            final int cFullFillWidth = 10000;
            mDlgCallView.setMinimumWidth(cFullFillWidth);
            typeClick = new ServiceTypeClick();
            LinearLayout linearLayout = (LinearLayout) mDlgCallView.findViewById(R.id.dlg_base_list_addlayout);
            String str = SharedPreferenceTool.getPrefString(mContext, SharedPreferenceTool.KEY_SHOWALLCOMPLAINTYPE, "");
            ComplainTypeBean complainTypeBean = (ComplainTypeBean) GsonTransformUtil.fromJson(str, ComplainTypeBean.class);
            if (complainTypeBean != null && complainTypeBean.getErrno() == 0 && complainTypeBean.getReturnContent() != null) {
                int sum = complainTypeBean.getReturnContent().size();
                for (int x = 0; x < sum; x++) {
                    ComplainVo vo = complainTypeBean.getReturnContent().get(x);
                    View tempView = inflater.inflate(layout.dlg_base_list_cancel_item, null);
                    ((TextView) tempView.findViewById(R.id.dlg_base_list_item_textview)).setText(vo.getComplainName());
                    tempView.setTag(vo);
                    tempView.setOnClickListener(typeClick);
                    if (x == 0) {
                        if (x == sum - 1) {
                            tempView.setBackgroundResource(R.drawable.butoon_style_single_sle);
                            linearLayout.addView(tempView);
                        } else {
                            tempView.setBackgroundResource(R.drawable.butoon_style_top_sle);
                            linearLayout.addView(tempView);
                            View view = inflater.inflate(layout.line_view_h, null);
                            linearLayout.addView(view);
                        }
                    } else {
                        if (x == sum - 1) {
                            tempView.setBackgroundResource(R.drawable.butoon_style_bol_sle);
                            linearLayout.addView(tempView);
                        } else {
                            tempView.setBackgroundResource(R.drawable.butoon_style_middle_sle);
                            linearLayout.addView(tempView);
                            View view = inflater.inflate(layout.line_view_h, null);
                            linearLayout.addView(view);
                        }
                    }

                }

            }


            TextView cancel_txt = (TextView) mDlgCallView
                    .findViewById(R.id.dlg_base_list_cancel);

            cancel_txt.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    complainDialog.dismiss();
                }
            });
            Window w = complainDialog.getWindow();
            WindowManager.LayoutParams lp = w.getAttributes();
            lp.x = 0;
            final int cMakeBottom = -1000;
            lp.y = cMakeBottom;
            lp.gravity = Gravity.BOTTOM;
            complainDialog.onWindowAttributesChanged(lp);
            complainDialog.setCanceledOnTouchOutside(true);
            complainDialog.setCancelable(true);
            complainDialog.setContentView(mDlgCallView);
        }
        complainDialog.show();
    }


    private void uploadImage(String path) {
        if (path != null) {
            dialogShow();
            String uploadName = "sfyz/service_operation/talkcomplain/" + YYConstans.getUserInfoBean().getReturnContent().getUser().getUserId() + "/" + System.currentTimeMillis() + ".jpg";
            final String uploadUrl = YYApplication.OSSBaseUrl + uploadName;
            PutObjectRequest putObjectRequest = new PutObjectRequest(YYApplication.bucketName, uploadName, path);
            putObjectRequest.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
                @Override
                public void onProgress(PutObjectRequest putObjectRequest, long l, long l1) {
                    int va0 = (int) ((l * 1.0 / l1) * 100);
                }
            });

            OSSAsyncTask asyncTask = YYApplication.getOss().asyncPutObject(putObjectRequest, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                @Override
                public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
                    baseHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dismmisDialog();
                            talkComplainAdp.getImgs().add(uploadUrl);
                            talkComplainAdp.notifyDataSetChanged();
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


    private void showAllComplain() {

        Map<String, String> wxparams = new HashMap<String, String>();
        wxparams.put("skey",
                YYConstans.getUserInfoBean().getReturnContent().getSkey() == null ? ""
                        : YYConstans.getUserInfoBean().getReturnContent().getSkey());
        YYRunner.getData(1001, YYRunner.Method_POST, YYUrl.SHOWALLCOMPLAINTYPE,
                wxparams, this);


    }

    class ServiceTypeClick implements OnClickListener {

        @Override
        public void onClick(View v) {
            ComplainVo vo = (ComplainVo) v.getTag();

            if (vo != null) {
                servicetype.setText(vo.getComplainName());
                servicetype.setTag(vo);
                complainDialog.dismiss();
            }
        }
    }


    private void addFeedBack() {

        ComplainVo vo = (ComplainVo) servicetype.getTag();
        if (vo == null) {
            MyUtils.showToast(mContext, "请选择服务类型");
            return;
        }


        Map<String, String> backparams = new HashMap<String, String>();
        backparams.put("skey",
                YYConstans.getUserInfoBean().getReturnContent().getSkey() == null ? ""
                        : YYConstans.getUserInfoBean().getReturnContent().getSkey());
        backparams.put("type", vo.getId() + "");
        backparams.put("complainName", vo.getComplainName());
        backparams.put("content", editText.getText().toString());

        StringBuffer buffer = new StringBuffer();
        backparams.put("complainImg", talkComplainAdp.getImages());


        YYRunner.getData(2001, YYRunner.Method_POST, YYUrl.ADDFEEDBACKBYCOMPLAIN,
                backparams, this);


    }


}
