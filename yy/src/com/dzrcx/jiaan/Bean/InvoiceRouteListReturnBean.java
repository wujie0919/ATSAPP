package com.dzrcx.jiaan.Bean;

import java.util.List;

public class InvoiceRouteListReturnBean {
    private double invoiceAll;
    private int nextPage;
    private List<InvoiceRouteListBeanIn> orderList;

    public double getInvoiceAll() {
        return invoiceAll;
    }

    public int getNextPage() {
        return nextPage;
    }

    public List<InvoiceRouteListBeanIn> getOrderList() {
        return orderList;
    }

    public void setInvoiceAll(double invoiceAll) {
        this.invoiceAll = invoiceAll;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public void setOrderList(List<InvoiceRouteListBeanIn> orderList) {
        this.orderList = orderList;
    }
}
