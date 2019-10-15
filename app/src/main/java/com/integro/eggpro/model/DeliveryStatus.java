package com.integro.eggpro.model;

import java.io.Serializable;

public class DeliveryStatus implements Serializable {

    private String itemQty;

    private String notes;

    private String productId;

    private String timeLineId;

    private String orderId;

    private String prodSellingPrice;

    private String prodStock;

    private String prodListingPrice;

    private String prodDescription;

    private String additionalDiscount;

    private String uid;

    private String prodQty;

    private String productImage;

    private String prodName;

    private String serveDate;

    private String id;

    private String status;

    public DeliveryStatus(String itemQty, String notes, String productId, String timeLineId, String orderId, String prodSellingPrice, String prodStock, String prodListingPrice, String prodDescription, String additionalDiscount, String uid, String prodQty, String productImage, String prodName, String serveDate, String id, String status) {
        this.itemQty = itemQty;
        this.notes = notes;
        this.productId = productId;
        this.timeLineId = timeLineId;
        this.orderId = orderId;
        this.prodSellingPrice = prodSellingPrice;
        this.prodStock = prodStock;
        this.prodListingPrice = prodListingPrice;
        this.prodDescription = prodDescription;
        this.additionalDiscount = additionalDiscount;
        this.uid = uid;
        this.prodQty = prodQty;
        this.productImage = productImage;
        this.prodName = prodName;
        this.serveDate = serveDate;
        this.id = id;
        this.status = status;
    }

    public String getItemQty() {
        return itemQty;
    }

    public String getNotes() {
        return notes;
    }

    public String getProductId() {
        return productId;
    }

    public String getTimeLineId() {
        return timeLineId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getProdSellingPrice() {
        return prodSellingPrice;
    }

    public String getProdStock() {
        return prodStock;
    }

    public String getProdListingPrice() {
        return prodListingPrice;
    }

    public String getProdDescription() {
        return prodDescription;
    }

    public String getAdditionalDiscount() {
        return additionalDiscount;
    }

    public String getUid() {
        return uid;
    }

    public String getProdQty() {
        return prodQty;
    }

    public String getProductImage() {
        return productImage;
    }

    public String getProdName() {
        return prodName;
    }

    public String getServeDate() {
        return serveDate;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }
}
