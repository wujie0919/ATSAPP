package com.dzrcx.jiaan.widget;

import com.dzrcx.jiaan.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

public class YYProgresssDialog extends Dialog {

	private Context mContext;
	private TextView mTextView;

	private View mView;

	/***
	 * 代码中定义的提示语句
	 */
	private String messageStr;

	/**
	 * 布局中设置的默认提示语句
	 */
	private String defMessage;

	// R.style.customProgressDialog//默认主题
	public YYProgresssDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mContext = context;
	}

	public YYProgresssDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.mContext = context;
	}

	public YYProgresssDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
		this.mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
		// TODO Auto-generated method stub
		super.setContentView(view, params);
	}

	private View initView() {
		LayoutInflater mInflater = LayoutInflater.from(mContext);
		mView = mInflater.inflate(R.layout.customprogressdialog, null);
		mTextView = (TextView) mView.findViewById(R.id.progress_circle_message);
		defMessage = mTextView.getText().toString();
		return mView;

	}

	@Override
	public boolean isShowing() {
		// TODO Auto-generated method stub

		return super.isShowing();
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		setContentView(mView == null ? initView() : mView);
		super.show();
	}

	/**
	 * 显示中临时更改提示语句
	 * 
	 * @param message
	 */
	public void show(String message) {
		setContentView(mView == null ? initView() : mView);
		if (!TextUtils.isEmpty(message)) {
			mTextView.setText(message);
		} else if (!TextUtils.isEmpty(messageStr)) {
			mTextView.setText(messageStr);
		} else {
			mTextView.setText(defMessage);
		}
		show();
	}

	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
	}

	@Override
	public void cancel() {
		// TODO Auto-generated method stub
		super.cancel();
	}

	private String getMessageStr() {
		return messageStr;
	}

	public void setMessageStr(String messageStr) {
		this.messageStr = messageStr;
	}

}
