package com.integro.eggpro.model;

import androidx.navigation.NavType;

import java.io.Serializable;

public class Order implements Serializable {

    private String uid;

    private String orderType;

    private String period;

    private String orderId;

    private String paymentId;

    private String frequecy;

    private String orderPrice;

    private String id;

    private String startDate;

    public Order(String uid, String orderType, String period, String orderId, String paymentId, String frequecy, String orderPrice, String id, String startDate) {
        this.uid = uid;
        this.orderType = orderType;
        this.period = period;
        this.orderId = orderId;
        this.paymentId = paymentId;
        this.frequecy = frequecy;
        this.orderPrice = orderPrice;
        this.id = id;
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

    public String getStartDate() {
        return startDate;
    }

    @Override
    public String toString() {
        return "Order{" +
                "uid='" + uid + '\'' +
                ", orderType='" + orderType + '\'' +
                ", period='" + period + '\'' +
                ", orderId='" + orderId + '\'' +
                ", paymentId='" + paymentId + '\'' +
                ", frequecy='" + frequecy + '\'' +
                ", orderPrice='" + orderPrice + '\'' +
                ", id='" + id + '\'' +
                ", startDate='" + startDate + '\'' +
                '}';
    }
}
