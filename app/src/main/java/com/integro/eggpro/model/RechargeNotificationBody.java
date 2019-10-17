package com.integro.eggpro.model;

import java.io.Serializable;

public class RechargeNotificationBody implements Serializable {


    private String message;

    public RechargeNotificationBody(String orderID, String notificationType, String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
