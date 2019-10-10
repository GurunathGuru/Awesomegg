package com.integro.eggpro.interfaces;

import com.integro.eggpro.model.Order;

public interface CreateOrder {
    void onOrderCreated(Order order);
}
