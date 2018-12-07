package com.dzrcx.jiaan.yyinterface;

public interface DrawerSlideInterface {

	/*
	 * float slideOffset 在0~1之间的数值
	 */
	public void onDrawerSlide(float slideOffset);

	/**
	 * 全部展示
	 */
	public void onDrawerShow();

}
