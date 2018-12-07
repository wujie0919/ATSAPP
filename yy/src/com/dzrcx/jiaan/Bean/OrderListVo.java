package com.dzrcx.jiaan.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangyu on 16-7-14.
 */
public class OrderListVo implements Serializable {

    private List<OrderListItemBean> orderList;
    private int pageNo;
    private int errno;
    private int totalPage;
    private double vailableInvoice;
    private int violationCount;

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public double getVailableInvoice() {
        return vailableInvoice;
    }

    public void setVailableInvoice(double vailableInvoice) {
        this.vailableInvoice = vailableInvoice;
    }

    public int getViolationCount() {
        return violationCount;
    }

    public void setViolationCount(int violationCount) {
        this.violationCount = violationCount;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<OrderListItemBean> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderListItemBean> orderList) {
        this.orderList = orderList;
    }
}
