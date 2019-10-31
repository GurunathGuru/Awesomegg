package com.integro.eggpro.model;

import java.io.Serializable;

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


