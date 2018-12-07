package com.dzrcx.jiaan.Bean;

/**
 * Created by Administrator on 2016/3/22.
 */
public class FeelBackContentBean extends YYBaseResBean {
    private String content;//内容
    private int id;//主键
    private int type;//1取消订单完成内容 ；2支付完成评价内容

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
