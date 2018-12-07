package com.dzrcx.jiaan.Bean;

/**
 * 帮助中心车辆型号列表
 */
public class HelpCenterBrandVo {
    private int cId;
    private String cName;//型号
    private int pId;
    private String pName;//牌子
    private String brandHelpUrl;//URL

    public String getBrandHelpUrl() {
        return brandHelpUrl;
    }

    public void setBrandHelpUrl(String brandHelpUrl) {
        this.brandHelpUrl = brandHelpUrl;
    }

    public int getcId() {
        return cId;
    }

    public void setcId(int cId) {
        this.cId = cId;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }
}
