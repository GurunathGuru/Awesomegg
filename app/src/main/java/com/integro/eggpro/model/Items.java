package com.integro.eggpro.model;

import java.io.Serializable;

public class Items implements Serializable {
    private String id;
    private String uid;
    private String orderId;
    private String productId;
    private String itemQty;
    private String itemPrice;
    private String prodName;
    private String prodDescription;
    private String prodQty;
    private String prodListingPrice;
    private String prodSellingPrice;
    private String additionalDiscount;
    private String prodStock;
    private String productImage;

    public Items(String id, String uid, String orderId, String productId,
                 String itemQty, String itemPrice, String prodName, String prodDescription,
                 String prodQty, String prodListingPrice, String prodSellingPrice, String additionalDiscount,
                 String prodStock, String productImage) {
        this.id = id;
        this.uid = uid;
        this.orderId = orderId;
        this.productId = productId;
        this.itemQty = itemQty;
        this.itemPrice = itemPrice;
        this.prodName = prodName;
        this.prodDescription = prodDescription;
        this.prodQty = prodQty;
        this.prodListingPrice = prodListingPrice;
        this.prodSellingPrice = prodSellingPrice;
        this.additionalDiscount = additionalDiscount;
        this.prodStock = prodStock;
        this.productImage = productImage;
    }

    public String getId() {
        return id;
    }

    public String getUid() {
        return uid;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getProductId() {
        return productId;
    }

    public String getItemQty() {
        return itemQty;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public String getProdName() {
        return prodName;
    }

    public String getProdDescription() {
        return prodDescription;
    }

    public String getProdQty() {
        return prodQty;
    }

    public String getProdListingPrice() {
        return prodListingPrice;
    }

    public String getProdSellingPrice() {
        return prodSellingPrice;
    }

    public String getAdditionalDiscount() {
        return additionalDiscount;
    }

    public String getProdStock() {
        return prodStock;
    }

    public String getProductImage() {
        return productImage;
    }
}
