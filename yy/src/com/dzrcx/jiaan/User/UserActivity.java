//package com.dzrcx.jiaan.User;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//import net.tsz.afinal.http.AjaxParams;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.text.TextUtils;
//import android.view.Gravity;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.dzrcx.jiaan.R;
//import com.dzrcx.jiaan.R.layout;
//import com.dzrcx.jiaan.YYApplication;
//import com.dzrcx.jiaan.Bean.UserInfoBean;
//import com.dzrcx.jiaan.Constans.YYConstans;
//import com.dzrcx.jiaan.Constans.YYOptions;
//import com.dzrcx.jiaan.Constans.YYUrl;
//import com.dzrcx.jiaan.base.YYBaseActivity;
//import com.dzrcx.jiaan.clicklistener.RequestInterface;
//import com.dzrcx.jiaan.tools.GsonTransformUtil;
//import com.dzrcx.jiaan.tools.MyUtils;
//import com.dzrcx.jiaan.tools.NetHelper;
//import com.dzrcx.jiaan.tools.PhotoUtil;
//import com.dzrcx.jiaan.tools.SharedPreferenceTool;
//import com.dzrcx.jiaan.tools.YYRunner;
//import com.dzrcx.jiaan.widget.CircularImageView;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.umeng.analytics.MobclickAgent;
//
//public class UserActivity extends YYBaseActivity implements
//		View.OnClickListener, RequestInterface {
//
//	private CircularImageView mImage_photo;
//	private TextView tv_name_txt;
//	private TextView mTv_modifyphoto, tv_content, tv_updata_txt, tv_cancel_txt;
//	private View modifyphoto_icon, updataView;
//	private Button mAuthInfo;
//
//	private LinearLayout user_login_layout;
//	private RelativeLayout feedback_layout, update_layout;
//	private long exitTime = 0;
//	private Dialog dialog;
//	private String takePictureSavePath;
//	private Uri imageUri;
//	private static final int TAG_uploadimg = 169503;
//	private static final int TAG_getuserinfo = 169504;
//	private LayoutInflater inflater;
//	private Dialog updataDialog;
//
//	@SuppressLint("NewApi")
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.user_activity);
//		((YYApplication) getApplication()).addActivity(this);
//		initView();
//		MobclickAgent.onEvent(this, "open_i");
//	}
//
//	private void initView() {
//		inflater = LayoutInflater.from(this);
//		updataView = inflater.inflate(layout.dlg_updata, null);
//		tv_content = (TextView) updataView.findViewById(R.id.tv_content);
//		tv_updata_txt = (TextView) updataView.findViewById(R.id.tv_updata_txt);
//		tv_cancel_txt = (TextView) updataView.findViewById(R.id.tv_cancel_txt);
//		tv_name_txt = (TextView) findViewById(R.id.tv_name_txt);
//		mTv_modifyphoto = (TextView) findViewById(R.id.modifyphoto);
//		modifyphoto_icon = findViewById(R.id.modifyphoto_icon);
//		mImage_photo = (CircularImageView) findViewById(R.id.user_photo);
//		user_login_layout = (LinearLayout) findViewById(R.id.user_login_layout);
//		feedback_layout = (RelativeLayout) findViewById(R.id.feedback_layout);
//		update_layout = (RelativeLayout) findViewById(R.id.update_layout);
//		mAuthInfo = (Button) findViewById(R.id.authInfo);
//		MyUtils.setViewsOnClick(this, mAuthInfo, update_layout,
//				feedback_layout, user_login_layout, tv_content, tv_updata_txt,
//				tv_cancel_txt);
//	}
//
//	@SuppressLint("NewApi")
//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		String skey = YYConstans.getUserInfoBean().getSkey();
//		if (skey.isEmpty()) {
//			mAuthInfo.setVisibility(View.INVISIBLE);
//			modifyphoto_icon.setVisibility(View.GONE);
//			mTv_modifyphoto.setVisibility(View.INVISIBLE);
//			tv_name_txt.setText("点击登录");
//		} else {
//			ImageLoader.getInstance().displayImage(
//					YYConstans.getUserInfoBean().getUser().getThumb(),
//					mImage_photo, YYOptions.Option_USERPHOTO);
//			mTv_modifyphoto.setVisibility(View.VISIBLE);
//			tv_name_txt.setText(YYConstans.getUserInfoBean().getUser()
//					.getName() == null ? "未设置" : YYConstans.getUserInfoBean()
//					.getUser().getName());
//
//			Map<String, String> wxparams = new HashMap<String, String>();
//			wxparams.put("skey",
//					YYConstans.getUserInfoBean().getSkey() == null ? ""
//							: YYConstans.getUserInfoBean().getSkey());
//
//			YYRunner.getData(TAG_getuserinfo, YYRunner.Method_POST,
//					YYUrl.GETUSERINFO, wxparams, this);
//
//		}
//		if (NetHelper.checkNetwork(UserActivity.this)) {
//			MyUtils.showToast(this, "暂无网络，请检查网络连接");
//			return;
//		}
//
//	}
//
//	private void setAuthorState(UserInfoBean userInfoBean) {
//		// -1—未通过审核，0—提交审核未处理，1—审核通过
//		switch (userInfoBean.getUser().getUserState()) {
//		case -1:
//			mAuthInfo.setText("资料认证");
//			mAuthInfo.setBackgroundResource(R.drawable.button_bg);
//			mAuthInfo.setEnabled(true);
//			mAuthInfo.setVisibility(View.VISIBLE);
//			break;
//		case 0:
//			mAuthInfo.setText("资料认证中");
//			mAuthInfo.setBackgroundResource(R.drawable.authinfo_bg);
//			mAuthInfo.setEnabled(false);
//			mAuthInfo.setVisibility(View.VISIBLE);
//			break;
//		case 1:
//			mAuthInfo.setVisibility(View.GONE);
//			break;
//		default:
//			break;
//		}
//	}
//
//	@SuppressLint("NewApi")
//	@Override
//	public void onClick(View arg0) {
//		// TODO Auto-generated method stub
//		switch (arg0.getId()) {
//		case R.id.user_login_layout:
//			if (YYConstans.getUserInfoBean().getSkey().isEmpty()) {
//				Intent intent1 = new Intent(this, LoginAty.class);
//				startActivity(intent1);
//			} else {
//				showBottomDialog();
//			}
//			break;
//		case R.id.authInfo:
//			startActivity(new Intent(this, IdentificationAty.class));
//			break;
//		case R.id.feedback_layout:
//			Intent intent1 = new Intent(this, FeelBackAty.class);
//			startActivity(intent1);
//			break;
//		case R.id.update_layout:
//			showUpdataDialog();
//			break;
//
//		case R.id.tv_updata_txt:
//			MyUtils.showToast(this, "更新");
//			break;
//		case R.id.tv_cancel_txt:
//			updataDialog.dismiss();
//			break;
//
//		default:
//			break;
//		}
//	}
//
//	private void showUpdataDialog() {
//		if (updataDialog == null) {
//			updataDialog = new Dialog(this, R.style.MyDialog);
//			updataDialog.setContentView(updataView);
//		}
//		Window dlgWindow = updataDialog.getWindow();
//		WindowManager.LayoutParams lp = dlgWindow.getAttributes();
//		lp.gravity = Gravity.CENTER;
//		lp.width = MyUtils.getScreenWidth(this) / 5 * 4;
//		dlgWindow.setAttributes(lp);
//		updataDialog.show();
//	}
//
//	public void showBottomDialog() {
//		if (dialog == null) {
//			dialog = new Dialog(this, R.style.ActionSheet);
//			LayoutInflater inflater = (LayoutInflater) this
//					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//			View mDlgCallView = inflater.inflate(R.layout.dlg_useraty, null);
//			final int cFullFillWidth = 10000;
//			mDlgCallView.setMinimumWidth(cFullFillWidth);
//
//			TextView tv_camera_txt = (TextView) mDlgCallView
//					.findViewById(R.id.tv_camera_txt);
//			TextView tv_album_txt = (TextView) mDlgCallView
//					.findViewById(R.id.tv_album_txt);
//			TextView tv_exit_txt = (TextView) mDlgCallView
//					.findViewById(R.id.tv_exit_txt);
//			TextView cancel_txt = (TextView) mDlgCallView
//					.findViewById(R.id.cancel_txt);
//
//			tv_camera_txt.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					if (NetHelper.checkNetwork(UserActivity.this)) {
//						MyUtils.showToast(UserActivity.this, "暂无网络，请检查网络连接");
//						return;
//					}
//					String saveRootPath = YYApplication.sdCardRootPath;
//					if (!TextUtils.isEmpty(saveRootPath)) {
//						Intent intent = new Intent(
//								MediaStore.ACTION_IMAGE_CAPTURE);
//						// 下面这句指定调用相机拍照后的照片存储的路径
//						String timeStamp = new SimpleDateFormat(
//								"yyyyMMdd_HHmmss").format(new Date());
//						File dirFil = new File(saveRootPath
//								+ "/yiyi/image/imageCach/");
//						if (!dirFil.exists()) {
//							dirFil.mkdirs();
//						}
//						File makeFile = new File(saveRootPath
//								+ "/yiyi/image/imageCach/", "xiaoma_"
//								+ timeStamp + ".jpeg");
//						takePictureSavePath = makeFile.getAbsolutePath();
//						intent.putExtra(MediaStore.EXTRA_OUTPUT,
//								Uri.fromFile(makeFile));
//						UserActivity.this.startActivityForResult(intent, 2);
//					} else {
//						MyUtils.showToast(UserActivity.this, "！存储设备部不可用");
//					}
//					dialog.dismiss();
//				}
//			});
//			tv_album_txt.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					if (NetHelper.checkNetwork(UserActivity.this)) {
//						MyUtils.showToast(UserActivity.this, "暂无网络，请检查网络连接");
//						return;
//					}
//					Intent intent = new Intent(
//							Intent.ACTION_PICK,
//							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//					UserActivity.this.startActivityForResult(intent, 1);
//					dialog.dismiss();
//
//				}
//			});
//			tv_exit_txt.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					SharedPreferenceTool.setPrefString(UserActivity.this,
//							SharedPreferenceTool.KEY_USERINFO, "");
//					ImageLoader.getInstance().displayImage("", mImage_photo,
//							YYOptions.Option_USERPHOTO);
//					YYConstans.setUserInfoBean(null);
//					UserActivity.this.onResume();
//					dialog.dismiss();
//				}
//			});
//			cancel_txt.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					dialog.dismiss();
//				}
//			});
//			Window w = dialog.getWindow();
//			WindowManager.LayoutParams lp = w.getAttributes();
//			lp.x = 0;
//			final int cMakeBottom = -1000;
//			lp.y = cMakeBottom;
//			lp.gravity = Gravity.BOTTOM;
//			dialog.onWindowAttributesChanged(lp);
//			dialog.setCanceledOnTouchOutside(true);
//			dialog.setCancelable(true);
//			dialog.setContentView(mDlgCallView);
//		}
//		dialog.show();
//	}
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// TODO Auto-generated method stub
//		super.onActivityResult(requestCode, resultCode, data);
//		if (resultCode == Activity.RESULT_OK) {
//			switch (requestCode) {
//			// 如果是直接从相册获取
//			case 1:
//				if (data != null) {
//					imageUri = data.getData();
//					if (Build.VERSION.SDK_INT >= 19) {
//						imageUri = Uri.fromFile(new File(PhotoUtil.getPath(
//								this, imageUri)));
//					}
//					imageUri = startPhotoZoom(imageUri);
//				}
//
//				break;
//			// 如果是调用相机拍照时
//			case 2:
//				if (takePictureSavePath != null) {
//					imageUri = Uri.fromFile(new File(takePictureSavePath));
//					imageUri = startPhotoZoom(imageUri);
//				}
//				break;
//			// 取得裁剪后的图片
//			case 3:
//				if (data != null) {
//					Bitmap bitmap = data.getParcelableExtra("data");
//					if (bitmap != null) {
//						mImage_photo.setImageBitmap(bitmap);
//						uploadImg(bitmap);
//					}
//				}
//				break;
//			default:
//				break;
//			}
//
//		}
//
//	}
//
//	private void uploadImg(Bitmap bitmap) {
//		dialogShow();
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//		final byte bytes[] = baos.toByteArray();
//		AjaxParams params = new AjaxParams();
//		params.put("file", new ByteArrayInputStream(bytes));
//		params.put("businessobj", "userFacePhoto_"
//				+ YYConstans.getUserInfoBean().getUser().getUserId());
//		params.put("appid", "195");
//		params.put("filename", "businessobj");
//		params.put("mimeType", "image/jpeg");
//		params.put("skey", YYConstans.getUserInfoBean().getSkey() == null ? ""
//				: YYConstans.getUserInfoBean().getSkey());
//		YYRunner.uploadImg(TAG_uploadimg, YYUrl.GETUPLOADIMG, params,
//				UserActivity.this);
//	}
//
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		// TODO Auto-generated method stub
//		// if (keyCode == KeyEvent.KEYCODE_BACK
//		// && event.getAction() == KeyEvent.ACTION_DOWN) {
//		// if ((System.currentTimeMillis() - exitTime) > 2000) {
//		// Toast.makeText(UserActivity.this,
//		// getResources().getString(R.string.app_exit_tip),
//		// Toast.LENGTH_SHORT).show();
//		// exitTime = System.currentTimeMillis();
//		// } else {
//		// ((YYApplication) getApplication()).exit();
//		// }
//		//
//		// return true;
//		// }
//
//		return super.onKeyDown(keyCode, event);
//	}
//
//	@Override
//	public void onError(String error) {
//		// TODO Auto-generated method stub
//		MyUtils.showToast(this, "数据传输错误请重试");
//		progresssDialog.dismiss();
//	}
//
//	@Override
//	public void onComplete(int tag, String json) {
//		// TODO Auto-generated method stub
//		progresssDialog.dismiss();
//		switch (tag) {
//		case TAG_uploadimg:
//			json2Upload(json);
//			break;
//		case TAG_getuserinfo:
//			UserInfoBean userInfoBean = (UserInfoBean) GsonTransformUtil
//					.fromJson(json, UserInfoBean.class);
//			if (userInfoBean != null && userInfoBean.getErrno() == 0) {
//				SharedPreferenceTool.setPrefString(this,
//						SharedPreferenceTool.KEY_USERINFO, json);
//				upUser(userInfoBean);
//			}
//			break;
//		default:
//			break;
//		}
//
//	}
//
//	private void upUser(UserInfoBean userInfoBean) {
//		if (!"normal".equals(userInfoBean.getUser().getRole())) {
//			mTv_modifyphoto.setText("账户余额： "
//					+ userInfoBean.getUser().getBalance() + "元");
//			modifyphoto_icon.setVisibility(View.VISIBLE);
//		} else {
//			mTv_modifyphoto.setText(null);
//			modifyphoto_icon.setVisibility(View.GONE);
//		}
//		setAuthorState(userInfoBean);
//
//	}
//
//	@SuppressLint("NewApi")
//	private void json2Upload(String json) {
//		if (!json.isEmpty())
//			try {
//				JSONObject jsonObject = new JSONObject(json);
//				String stateMsg = jsonObject.getString("stateMsg");
//				String stateCode = jsonObject.getString("stateCode");
//				if (!"0".equals(stateCode)) {
//					MyUtils.showToast(UserActivity.this,
//							jsonObject.has("stateMsg") ? stateMsg : "服务器错误");
//				} else {
//					JSONObject resourceInfo = jsonObject
//							.getJSONObject("resourceInfo");
//
//					String fileUrl = resourceInfo.getString("fileUrl");
//
//					YYConstans.getUserInfoBean().getUser().setThumb(fileUrl);
//
//					ImageLoader.getInstance().displayImage(fileUrl,
//							mImage_photo, YYOptions.Option_USERPHOTO);
//				}
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//	}
//
//	/**
//	 * 头像裁剪 图片方法实现
//	 *
//	 * @param uriFrom
//	 */
//	public Uri startPhotoZoom(Uri uriFrom) {
//		Intent intent = new Intent("com.android.camera.action.CROP");
//		intent.setDataAndType(uriFrom, "image/*");
//		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
//		intent.putExtra("crop", "true");
//		// aspectX aspectY 是宽高的比例
//		intent.putExtra("aspectX", 1);
//		intent.putExtra("aspectY", 1);
//		// outputX outputY 是裁剪图片宽高
//		intent.putExtra("outputX", 150);
//		intent.putExtra("outputY", 150);
//		intent.putExtra("return-data", true);
//		startActivityForResult(intent, 3);
//		return uriFrom;
//
//	}
//
//	@Override
//	public void onLoading(long count, long current) {
//		// TODO Auto-generated method stub
//
//	}
//
//}
