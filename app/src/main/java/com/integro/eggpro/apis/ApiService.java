package com.integro.eggpro.apis;

import com.integro.eggpro.model.Apartments;
import com.integro.eggpro.model.CustomCalender;
import com.integro.eggpro.model.MyOrderList;
import com.integro.eggpro.model.Order;
import com.integro.eggpro.model.Products;
import com.integro.eggpro.model.User;
import com.integro.eggpro.model.WalletStatement;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
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

    @FormUrlEncoded
    @POST("placeOrder")
    Call<Order> createOrder(
            @Field("uid") String uid,
            @Field("period") int period,
            @Field("frequecy") int frequecy,
            @Field("startDate") int startDate,
            @Field("orderType") String orderType,
            @Field("orderPrice") Double orderPrice,
            @Field("size") int size,
            @FieldMap Map<String, String> param
    );

    @FormUrlEncoded
    @POST("getCurrentBalance")
    Call<Double> getCurrentBalance(
            @Field("uid") String uid);

    @FormUrlEncoded
    @POST("paymentComplete")
    Call<Integer> paymentComplete(
            @Field("uid") String uid,
            @Field("orderId") String orderId,
            @Field("id") int id,
            @Field("paymentId") String paymentId,
            @Field("amount") Double amount
    );

    @FormUrlEncoded
    @POST("getOrders")
    Call<ArrayList<MyOrderList>> getMyOrderList(
            @Field("uid") String uid);

    @FormUrlEncoded
    @POST("getWalletStatement")
    Call<ArrayList<WalletStatement>> getWalletStatementList(
            @Field("uid") String uid);
}