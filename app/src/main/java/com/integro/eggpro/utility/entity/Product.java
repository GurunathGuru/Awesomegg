package com.integro.eggpro.utility.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "products"
)
public class Product {

    @PrimaryKey
    private int id;

    private String productImage;

    private Double prodSellingPrice;

    private String prodName;

    private String prodDescription;

    private int prodQty;

    private Double prodListingPrice;

    private int additionalDiscount;

    private int prodStock;

    private int itemQty;

    public Product(int id, String productImage, Double prodSellingPrice, String prodName, String prodDescription, int prodQty, Double prodListingPrice, int additionalDiscount, int prodStock, int itemQty) {
        this.id = id;
        this.productImage = productImage;
        this.prodSellingPrice = prodSellingPrice;
        this.prodName = prodName;
        this.prodDescription = prodDescription;
        this.prodQty = prodQty;
        this.prodListingPrice = prodListingPrice;
        this.additionalDiscount = additionalDiscount;
        this.prodStock = prodStock;
        this.itemQty = itemQty;
    }

    public int getId() {
        return id;
    }

    public String getProductImage() {
        return productImage;
    }

    public Double getProdSellingPrice() {
        return prodSellingPrice;
    }

    public String getProdName() {
        return prodName;
    }

    public String getProdDescription() {
        return prodDescription;
    }

    public int getProdQty() {
        return prodQty;
    }

    public Double getProdListingPrice() {
        return prodListingPrice;
    }

    public int getAdditionalDiscount() {
        return additionalDiscount;
    }

    public int getProdStock() {
        return prodStock;
    }

    public int getItemQty() {
        return itemQty;
    }

    public void setItemQty(int itemQty) {
        this.itemQty = itemQty;
    }

}
