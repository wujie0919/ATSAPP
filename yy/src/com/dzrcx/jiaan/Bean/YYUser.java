package com.dzrcx.jiaan.Bean;

public class YYUser {

    private double balance;
    private String mobile;
    private String name;
    private String role;
    private String thumb;
    private int userId;
    private int userState;
    private String idCard;
    /**
     * 0-无等级
     * <p/>
     * 1-黄金
     * <p/>
     * 2-白金
     * <p/>
     * 3-钻石
     * <p/>
     * 10--超级用户 vip
     */
    private int level;

    /**
     * 邀请码
     */
    private String inviteCode;
    /**
     * 是否被邀请过
     */
    private int invited;//0—未被邀请过;1—	已邀请过


    private int entUser;//是否存在企业账户

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    //private ArrayList<EnterPriseBean> enterpriseList;

//    public ArrayList<EnterPriseBean> getEnterpriseList() {
//        return enterpriseList;
//    }
//
//    public void setEnterpriseList(ArrayList<EnterPriseBean> enterpriseList) {
//        this.enterpriseList = enterpriseList;
//    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserState() {
        return userState;
    }

    public void setUserState(int userState) {
        this.userState = userState;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }


    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public int getInvited() {
        return invited;
    }

    public void setInvited(int invited) {
        this.invited = invited;
    }

    public int getEntUser() {
        return entUser;
    }

    public void setEntUser(int entUser) {
        this.entUser = entUser;
    }
}
