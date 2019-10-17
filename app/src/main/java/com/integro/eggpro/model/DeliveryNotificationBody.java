package com.integro.eggpro.model;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

public class DeliveryNotificationBody implements Serializable {
    private String message;
    private String orderId;

    private ItemList items;

    public DeliveryNotificationBody(String message, String orderId,String items) {
        this.message = message;
        this.orderId = orderId;
        this.items=new Gson().fromJson(items,ItemList.class );
    }

    public String getMessage() {
        return message;
    }

    public String getOrderId() {
        return orderId;
    }

    public ItemList getItems() {
        return items;
    }
}


