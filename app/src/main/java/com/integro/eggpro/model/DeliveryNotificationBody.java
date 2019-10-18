package com.integro.eggpro.model;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

public class DeliveryNotificationBody implements Serializable {
    private String message;
    private String orderId;

    public DeliveryNotificationBody(String message, String orderId) {
        this.message = message;
        this.orderId = orderId;
    }

    public String getMessage() {
        return message;
    }

    public String getOrderId() {
        return orderId;
    }
}


