package com.dzrcx.jiaan.Bean;

import java.util.ArrayList;

public class ADPhotoListBean extends YYBaseResBean {

	private ArrayList<ADPhotosBean> returnContent;

	public ArrayList<ADPhotosBean> getPhotos() {
		return returnContent;
	}

	public void setPhotos(ArrayList<ADPhotosBean> photos) {
		this.returnContent = photos;
	}

}
