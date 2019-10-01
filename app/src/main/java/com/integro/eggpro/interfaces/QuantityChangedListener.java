package com.integro.eggpro.interfaces;

import com.integro.eggpro.model.Products;

public interface QuantityChangedListener {

    void onItermQuantityChanged(int oldValue, int newValue, Products products);

}
