package com.integro.eggpro.model;

import java.io.Serializable;
import java.util.ArrayList;

public class MyOrderList implements Serializable {

    private String uid;

    private String orderType;

    private String period;

    private String orderId;

    private String paymentId;

    private String frequecy;

    private String orderPrice;

    private String id;

    private ArrayList<Items> items;

    private String startDate;

    public MyOrderList(String uid, String orderType, String period, String orderId, String paymentId, String frequecy, String orderPrice, String id, ArrayList<Items> items, String startDate) {
        this.uid = uid;
        this.orderType = orderType;
        this.period = period;
        this.orderId = orderId;
        this.paymentId = paymentId;
        this.frequecy = frequecy;
        this.orderPrice = orderPrice;
        this.id = id;
        this.items = items;
        this.startDate = startDate;
    }

    public String getUid() {
        return uid;
    }

    public String getOrderType() {
        return orderType;
    }

    public String getPeriod() {
        return period;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getFrequecy() {
        return frequecy;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public String getId() {
        return id;
    }

    public ArrayList<Items> getItems() {
        return items;
    }

    public String getStartDate() {
        return startDate;
    }
}
