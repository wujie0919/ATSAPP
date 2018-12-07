package com.dzrcx.jiaan.Bean;

import java.io.Serializable;

public class OrderBean implements Serializable {
	private String chargeTime;
	private String renterName;
	private String model;
	private String orderState;
	private String totalPrice;
	private String parkLocAddress;
	private String rectPrice;
	private String orderId;
	private String make;
	private String plate;
	private String carThumb;
	private String pickcarTime;

	public String getChargeTime() {
		return this.chargeTime;
	}

	public OrderBean() {
		super();
	}

	public OrderBean(String chargeTime, String renterName, String model,
			String orderState, String totalPrice, String parkLocAddress,
			String rectPrice, String orderId, String make, String plate,
			String carThumb, String pickcarTime) {
		super();
		this.chargeTime = chargeTime;
		this.renterName = renterName;
		this.model = model;
		this.orderState = orderState;
		this.totalPrice = totalPrice;
		this.parkLocAddress = parkLocAddress;
		this.rectPrice = rectPrice;
		this.orderId = orderId;
		this.make = make;
		this.plate = plate;
		this.carThumb = carThumb;
		this.pickcarTime = pickcarTime;
	}

	public void setChargeTime(String chargeTime) {
		this.chargeTime = chargeTime;
	}

	public String getRenterName() {
		return this.renterName;
	}

	public void setRenterName(String renterName) {
		this.renterName = renterName;
	}

	public String getModel() {
		return this.model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getOrderState() {
		return this.orderState;
	}

	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}

	public String getTotalPrice() {
		return this.totalPrice;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getParkLocAddress() {
		return this.parkLocAddress;
	}

	public void setParkLocAddress(String parkLocAddress) {
		this.parkLocAddress = parkLocAddress;
	}

	public String getRectPrice() {
		return this.rectPrice;
	}

	public void setRectPrice(String rectPrice) {
		this.rectPrice = rectPrice;
	}

	public String getOrderId() {
		return this.orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getMake() {
		return this.make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getPlate() {
		return this.plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public String getCarThumb() {
		return this.carThumb;
	}

	public void setCarThumb(String carThumb) {
		this.carThumb = carThumb;
	}

	public String getPickcarTime() {
		return this.pickcarTime;
	}

	public void setPickcarTime(String pickcarTime) {
		this.pickcarTime = pickcarTime;
	}

}