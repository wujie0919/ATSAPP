package com.dzrcx.jiaan.Constans;

import android.graphics.Bitmap;

import com.dzrcx.jiaan.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class YYOptions {
    /**
     * 默认itemoption
     */
    public static DisplayImageOptions OPTION_DEF = new DisplayImageOptions.Builder()
            .cacheInMemory(true).cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565).build();
    /**
     * 首页找车itemoption
     */
    public static DisplayImageOptions Option_CARITEM = new DisplayImageOptions.Builder()
            .cacheInMemory(true).cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .showImageForEmptyUri(R.drawable.search_default)
            .showImageOnLoading(R.drawable.search_default)
            .showImageOnFail(R.drawable.search_default).build();
    /**
     * 启动页默认头像
     */
    public static DisplayImageOptions Option_StartPageUserPhoto = new DisplayImageOptions.Builder()
            .cacheInMemory(true).cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .showImageForEmptyUri(R.drawable.start_userphoto_nor)
            .showImageOnLoading(R.drawable.start_userphoto_nor)
            .showImageOnFail(R.drawable.start_userphoto_nor).build();
    /**
     * 车辆详情
     */
    public static DisplayImageOptions Option_CARDETAIL = new DisplayImageOptions.Builder()
            .cacheInMemory(true).cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .showImageForEmptyUri(R.drawable.load_fail)
            .showImageOnLoading(R.drawable.load_cardetail_loading)
            .showImageOnFail(R.drawable.load_fail).build();
    /**
     * 用户头像
     */
    public static DisplayImageOptions Option_USERPHOTO = new DisplayImageOptions.Builder()
            .cacheInMemory(true).cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .showImageForEmptyUri(R.drawable.defualt_usericon_2)
            .showImageOnLoading(R.drawable.defualt_usericon_2)
            .showImageOnFail(R.drawable.defualt_usericon_2).build();
    /**
     * 用户头像
     */
    public static DisplayImageOptions Option_USERPHOTO2 = new DisplayImageOptions.Builder()
            .cacheInMemory(true).cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .showImageForEmptyUri(R.drawable.personal)
            .showImageOnLoading(R.drawable.personal)
            .showImageOnFail(R.drawable.personal).build();
}
