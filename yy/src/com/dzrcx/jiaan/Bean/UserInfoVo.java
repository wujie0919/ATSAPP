package com.dzrcx.jiaan.Bean;

import java.io.Serializable;

/**
 * Created by zhangyu on 16-7-14.
 */
public class UserInfoVo implements Serializable {


    private String skey;

    private YYUser user;

    public String getSkey() {
        return skey == null ? "" : skey;
    }

    public void setSkey(String skey) {
        this.skey = skey;
    }

    public YYUser getUser() {
        return user == null ? new YYUser() : user;
    }

    public void setUser(YYUser user) {
        this.user = user;
    }


}
