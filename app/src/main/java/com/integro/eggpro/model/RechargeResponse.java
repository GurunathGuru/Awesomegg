package com.integro.eggpro.model;

import java.io.Serializable;

public class RechargeResponse implements Serializable {

    private Double rechargePrice;

    private int period;

    private int orderId;

    private String  paymentId;

    private String ipgOrderId;

    private int id;

    private String rUid;

    private String startDate;

    private int frequency;

    private String status;

    public RechargeResponse(Double rechargePrice, int period, int orderId, String paymentId, String ipgOrderId, int id, String rUid, String startDate, int frequency, String status) {
        this.rechargePrice = rechargePrice;
        this.period = period;
        this.orderId = orderId;
        this.paymentId = paymentId;
        this.ipgOrderId = ipgOrderId;
        this.id = id;
        this.rUid = rUid;
        this.startDate = startDate;
        this.frequency = frequency;
        this.status = status;
    }

    public Double getRechargePrice() {
        return rechargePrice;
    }

    public void setRechargePrice(Double rechargePrice) {
        this.rechargePrice = rechargePrice;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getIpgOrderId() {
        return ipgOrderId;
    }

    public void setIpgOrderId(String ipgOrderId) {
        this.ipgOrderId = ipgOrderId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getrUid() {
        return rUid;
    }

    public void setrUid(String rUid) {
        this.rUid = rUid;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
