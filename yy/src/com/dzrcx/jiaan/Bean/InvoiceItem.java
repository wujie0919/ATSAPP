package com.dzrcx.jiaan.Bean;

/**
 * 充值记录 item
 * Created by zhangyu on 16-1-5.
 */
public class InvoiceItem {

    private double amount;
    /**
     * 0—待开票
     * 1—已开票
     */
    private int state;
    private long createTimes;
    private int invoiceId;
    private int type;//1普通发票 3增值发票
    private String invoiceNumber;//发票号码

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getCreateTimes() {
        return createTimes;
    }

    public void setCreateTimes(long createTimes) {
        this.createTimes = createTimes;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
