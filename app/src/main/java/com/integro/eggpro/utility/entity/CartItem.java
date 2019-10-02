package com.integro.eggpro.utility.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "cartItem"
)
public class CartItem {

    @PrimaryKey
    private int id;

    private String productName;

    private int prodductQty;

    private int itemQty;

    private Double productListingPrice;

    private Double productSellingPrice;

    private int additionalDiscount;

    private String productImage;

    public CartItem(int id, String productName, int prodductQty, int itemQty, Double productListingPrice, Double productSellingPrice, int additionalDiscount, String productImage) {
        this.id = id;
        this.productName = productName;
        this.prodductQty = prodductQty;
        this.itemQty = itemQty;
        this.productListingPrice = productListingPrice;
        this.productSellingPrice = productSellingPrice;
        this.additionalDiscount = additionalDiscount;
        this.productImage = productImage;
    }

    public void setItemQty(int itemQty) {
        this.itemQty = itemQty;
    }

    public int getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public int getProdductQty() {
        return prodductQty;
    }

    public int getItemQty() {
        return itemQty;
    }

    public Double getProductListingPrice() {
        return productListingPrice;
    }

    public Double getProductSellingPrice() {
        return productSellingPrice;
    }

    public int getAdditionalDiscount() {
        return additionalDiscount;
    }

    public String getProductImage() {
        return productImage;
    }
}
