package com.dzrcx.jiaan.Bean;

import java.io.Serializable;

/**
 * 接口返回基础类
 *
 * @author zhangyu
 */
public class YYBaseResBean implements Serializable {

    private int errno = -1;//errno==0 表示数据返回成功
    private String error;

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
