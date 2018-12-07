package com.dzrcx.jiaan.Bean;

import java.util.List;

public class OrderCompleteList extends YYBaseResBean {
	private List<OrderBean> orderList;

	public List<OrderBean> getOrderList() {
		return this.orderList;
	}

	public void setOrderList(List<OrderBean> orderList) {
		this.orderList = orderList;
	}

	public OrderCompleteList(List<OrderBean> orderList) {
		super();
		this.orderList = orderList;
	}

}
