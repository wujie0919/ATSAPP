package com.dzrcx.jiaan.clicklistener;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.Order.OrderDetailAty;
import com.dzrcx.jiaan.base.YYBaseActivity;

public class ToOrderDetailClickListener implements OnClickListener {

	private Context mContext;

	public ToOrderDetailClickListener(Context mContext) {
		super();
		this.mContext = mContext;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (mContext == null) {
			return;
		}
		String orderId = (String) v.getTag(R.id.tag_second);
		if (!TextUtils.isEmpty(orderId)) {
			Intent toDetail = new Intent(mContext, OrderDetailAty.class);
			toDetail.putExtra("orderId", orderId);
			mContext.startActivity(toDetail);
			((YYBaseActivity) mContext).overridePendingTransition(
					R.anim.activity_up, R.anim.activity_push_no_anim);
		}

	}

}
