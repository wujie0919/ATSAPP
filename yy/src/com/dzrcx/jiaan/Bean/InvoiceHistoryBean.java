package com.dzrcx.jiaan.Bean;

import java.util.ArrayList;

/**
 * Created by zhangyu on 16-1-5.
 */
public class InvoiceHistoryBean {
    private int nextPage;
    private ArrayList<InvoiceItem> invoiceHistoryList;

    public ArrayList<InvoiceItem> getInvoiceHistoryList() {
        return invoiceHistoryList;
    }

    public void setInvoiceHistoryList(ArrayList<InvoiceItem> invoiceHistoryList) {
        this.invoiceHistoryList = invoiceHistoryList;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }
}
