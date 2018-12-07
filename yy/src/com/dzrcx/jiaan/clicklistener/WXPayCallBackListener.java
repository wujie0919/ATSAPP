package com.dzrcx.jiaan.clicklistener;

/**
 * 微信支付回调
 * 
 * @author zhangyu
 * 
 */
public interface WXPayCallBackListener {

	void OnBackListener(int status, String message);

}
