package com.integro.eggpro.interfaces;

import com.integro.eggpro.model.Products;
import com.integro.eggpro.utility.entity.Product;

public interface QuantityChangedListener {

    void onItermQuantityChanged(int oldValue, int newValue, Product product);

}
