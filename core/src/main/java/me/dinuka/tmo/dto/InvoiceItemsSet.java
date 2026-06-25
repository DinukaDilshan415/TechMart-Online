package me.dinuka.tmo.dto;

import me.dinuka.tmo.entity.InvoiceItems;

import java.util.List;

public class InvoiceItemsSet {
    private String orderId;
    private int itemsCount;
    private List<InvoiceItems> invoiceItemses;

    public InvoiceItemsSet() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<InvoiceItems> getInvoiceItemses() {
        return invoiceItemses;
    }

    public void setInvoiceItemses(List<InvoiceItems> invoiceItemses) {
        this.invoiceItemses = invoiceItemses;
    }

    public int getItemsCount() {
        return itemsCount;
    }

    public void setItemsCount(int itemsCount) {
        this.itemsCount = itemsCount;
    }



}
