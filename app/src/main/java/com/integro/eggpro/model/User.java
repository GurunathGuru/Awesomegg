package com.integro.eggpro.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {

    @SerializedName("aId")
    private String apartmentId;
    private String userId;
    private String name;
    private String email;
    private String flatNo;
    private String mobile;
    private String floorNo;

    public User(String apartmentId, String userId, String name, String email, String flatNo, String mobile, String floorNo) {
        this.apartmentId = apartmentId;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.flatNo = flatNo;
        this.mobile = mobile;
        this.floorNo = floorNo;
    }

    public String getApartmentId() {
        return apartmentId;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getFlatNo() {
        return flatNo;
    }

    public String getMobile() {
        return mobile;
    }

    public String getFloorNo() {
        return floorNo;
    }

    public void setApartmentId(String apartmentId) {
        this.apartmentId = apartmentId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFlatNo(String flatNo) {
        this.flatNo = flatNo;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setFloorNo(String floorNo) {
        this.floorNo = floorNo;
    }
}
