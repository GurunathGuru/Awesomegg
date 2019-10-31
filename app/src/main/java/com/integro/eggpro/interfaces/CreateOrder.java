package com.integro.eggpro.interfaces;

import com.integro.eggpro.model.RechargeResponse;

public interface CreateOrder {
    void onOrderCreated(RechargeResponse response);
}
