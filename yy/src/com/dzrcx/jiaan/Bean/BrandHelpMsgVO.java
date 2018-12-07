package com.dzrcx.jiaan.Bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zhangyu on 16-6-14.
 */
public class BrandHelpMsgVO implements Serializable {

    /**
     * 品牌 比亚迪
     */
    private String parentName;

    /**
     * 车型 E6
     */
    private String name;
    /**
     * 语音提示音
     */
    private ArrayList<BrandHelpMsgSubVO> msgList;


    private String createMsg;
    private String endOrderMsg;
    private String useMsg;


    /**
     * 提示图片
     */
    private ArrayList<String> helpPhotos;


    private int brandId;
    private int parentId;

    public ArrayList<BrandHelpMsgSubVO> getMsgList() {
        return msgList;
    }

    public void setMsgList(ArrayList<BrandHelpMsgSubVO> msgList) {
        this.msgList = msgList;
    }

    public ArrayList<String> getHelpPhotos() {
        return helpPhotos;
    }

    public void setHelpPhotos(ArrayList<String> helpPhotos) {
        this.helpPhotos = helpPhotos;
    }

    public ArrayList<String> getHelpPhoto() {
        return helpPhotos;
    }

    public void setHelpPhoto(ArrayList<String> helpPhotos) {
        this.helpPhotos = helpPhotos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }


    public String getEndOrderMsg() {
        return endOrderMsg;
    }

    public void setEndOrderMsg(String endOrderMsg) {
        this.endOrderMsg = endOrderMsg;
    }


    public String getCreateMsg() {
        return createMsg;
    }

    public void setCreateMsg(String createMsg) {
        this.createMsg = createMsg;
    }

    public String getUseMsg() {
        return useMsg;
    }

    public void setUseMsg(String useMsg) {
        this.useMsg = useMsg;
    }
}
