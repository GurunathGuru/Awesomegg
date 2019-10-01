package com.integro.eggpro.apis;

import com.integro.eggpro.model.Apartments;
import com.integro.eggpro.model.CustomCalender;
import com.integro.eggpro.model.Products;
import com.integro.eggpro.model.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @GET("getProducts")
    Call<ArrayList<Products>> getAddItemList();

    @GET("getAppartments")
    Call<ArrayList<Apartments>> getApartmentList();

    @FormUrlEncoded
    @POST("isRegistered")
    Call<User> isRegistered(
            @Field("uid") String uid,
            @Field("mobile") String mobile
    );

    @FormUrlEncoded
    @POST("registerUser")
    Call<String> getUserDetailsList(
            @Field("aId") String aId,
            @Field("uid") String uid,
            @Field("name") String name,
            @Field("email") String email,
            @Field("flatNo") String flatNo,
            @Field("mobile") String mobile,
            @Field("floorNo") String floorNo
    );

    @FormUrlEncoded
    @POST("getServicableDates")
    Call<ArrayList<CustomCalender>> getCalender(@Field("apartmentName") String apartmentName);
}
