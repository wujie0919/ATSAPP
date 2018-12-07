package com.dzrcx.jiaan.Bean;

import java.util.List;

public class CarListBean extends YYBaseResBean {
	private List<CarListItemBean> carList;
	private int nextPage;

	@Override
	public String toString() {
		return "CarListBean [carList=" + carList + ", nextPage=" + nextPage
				+ "]";
	}

	public List<CarListItemBean> getCarList() {
		return carList;
	}

	public void setCarList(List<CarListItemBean> carList) {
		this.carList = carList;
	}

	public int getNextPage() {
		return nextPage;
	}

	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}

}
