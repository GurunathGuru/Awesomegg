package com.integro.eggpro.model;

import java.io.Serializable;

public class Products implements Serializable {

    private int id;

    private String productImage;

    private Double prodSellingPrice;

    private String prodName;

    private String prodDescription;

    private int prodQty;

    private Double prodListingPrice;

    private int additionalDiscount;

    private int prodStock;


    public Products(int id ,String productImage, Double prodSellingPrice, String prodName, int prodQty) {
        this.id= id;
        this.productImage = productImage;
        this.prodSellingPrice = prodSellingPrice;
        this.prodName = prodName;
        this.prodQty = prodQty;
    }

    public Products(int id, Double prodSellingPrice, int newValue, String prodName, String productImage) {
        this.id= id;
        this.prodSellingPrice = prodSellingPrice;
        this.prodQty = newValue;
        this.prodName = prodName;
        this.productImage = productImage;
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

    public void setProdQty(int prodQty) {
        this.prodQty = prodQty;
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

    @Override
    public String toString() {
        return "Products{" +
                "id='" + id + '\'' +
                ", image='" + productImage + '\'' +
                ", prodSellingPrice='" + prodSellingPrice + '\'' +
                ", prodName='" + prodName + '\'' +
                ", prodDescription='" + prodDescription + '\'' +
                ", prodQty='" + prodQty + '\'' +
                ", prodListingPrice='" + prodListingPrice + '\'' +
                ", additionalDiscount='" + additionalDiscount + '\'' +
                ", prodStock='" + prodStock + '\'' +
                '}';
    }
}
