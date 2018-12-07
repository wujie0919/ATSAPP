package com.dzrcx.jiaan.Bean;

import java.io.Serializable;

/**
 * 微信支付订单数据类型
 * 
 * @author zhangyu
 * 
 */
public class WXPayBean implements Serializable {

	private String appid;// ", (String) map_xml_body.get("appid"));
	private String noncestr;// ", (String) map_xml_body.get("noncestr"));
	private String packageStr;// ", (String) map_xml_body.get("package"));
	private String partnerid;// ", (String) map_xml_body.get("partnerid"));
	private String prepayid;// ", (String) map_xml_body.get("prepayid"));
	private String sign;// ", (String) map_xml_body.get("sign"));
	private String timestamp;// ", (String) map_xml_body.get("timestamp"));

	/**
	 * 
	 */
	public WXPayBean() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("WeiXinPayInfo [appid=");
		builder.append(appid);
		builder.append(", noncestr=");
		builder.append(noncestr);
		builder.append(", packageStr=");
		builder.append(packageStr);
		builder.append(", partnerid=");
		builder.append(partnerid);
		builder.append(", prepayid=");
		builder.append(prepayid);
		builder.append(", sign=");
		builder.append(sign);
		builder.append(", timestamp=");
		builder.append(timestamp);
		builder.append("]");
		return builder.toString();
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getNoncestr() {
		return noncestr;
	}

	public void setNoncestr(String noncestr) {
		this.noncestr = noncestr;
	}

	public String getPackageStr() {
		return packageStr;
	}

	public void setPackageStr(String packageStr) {
		this.packageStr = packageStr;
	}

	public String getPartnerid() {
		return partnerid;
	}

	public void setPartnerid(String partnerid) {
		this.partnerid = partnerid;
	}

	public String getPrepayid() {
		return prepayid;
	}

	public void setPrepayid(String prepayid) {
		this.prepayid = prepayid;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

}
