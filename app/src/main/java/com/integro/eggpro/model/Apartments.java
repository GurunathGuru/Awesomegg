package com.integro.eggpro.model;

import java.io.Serializable;

public class Apartments implements Serializable {


    private String apartmentName;

    private String apartmentAddress;

    public String getApartmentName() {
        return apartmentName;
    }

    public void setApartmentName(String apartmentName) {
        this.apartmentName = apartmentName;
    }

    public String getApartmentAddress() {
        return apartmentAddress;
    }

    public void setApartmentAddress(String apartmentAddress) {
        this.apartmentAddress = apartmentAddress;
    }

    public Apartments(String apartmentName, String apartmentAddress) {
        this.apartmentName = apartmentName;
        this.apartmentAddress = apartmentAddress;
    }



}
