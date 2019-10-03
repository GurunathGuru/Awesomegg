package com.integro.eggpro.helpers;

import com.integro.eggpro.utility.entity.CartItem;
import com.integro.eggpro.utility.entity.Product;

public class ParseObjects {
    public static CartItem toCartItem(Product product) {
        CartItem item = new CartItem(
                product.getId(),
                product.getProductImage(),
                product.getProdSellingPrice(),
                product.getProdName(),
                product.getProdDescription(),
                product.getProdQty(),
                product.getProdListingPrice(),
                product.getAdditionalDiscount(),
                product.getProdStock(),
                product.getItemQty()
        );
        return item;
    }

    public static Product toProduct(CartItem product) {
        Product item = new Product(
                product.getId(),
                product.getProductImage(),
                product.getProdSellingPrice(),
                product.getProdName(),
                product.getProdDescription(),
                product.getProdQty(),
                product.getProdListingPrice(),
                product.getAdditionalDiscount(),
                product.getProdStock(),
                product.getItemQty()
        );
        return item;
    }
}
