package com.integro.eggpro.apis;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.integro.eggpro.model.Apartments;
import com.integro.eggpro.model.CustomCalender;
import com.integro.eggpro.model.DeliveryStatus;
import com.integro.eggpro.model.MyOrderList;
import com.integro.eggpro.model.Notifications;
import com.integro.eggpro.model.Order;
import com.integro.eggpro.model.Products;
import com.integro.eggpro.model.RechargeResponse;
import com.integro.eggpro.model.Recipes;
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
            @Field("mobile") String mobile,
            @Field("fcmTag") String fcmTag
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
    Call<RechargeResponse> createOrder(
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
    @POST("placeOrder")
    Call<Order> createOrder(
            @Field("uid") String uid,
            @Field("period") int period,
            @Field("frequecy") String frequecy,
            @Field("startDate") int startDate,
            @Field("orderType") String orderType,
            @Field("orderPrice") Double orderPrice,
            @Field("size") int size,
            @FieldMap Map<String, String> param
    );

    @FormUrlEncoded
    @POST("cashOnDelivery")
    Call<Integer> cashOnDelivery(
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
    @POST("rechargeOrder")
    Call<RechargeResponse> rechargeOrder(
            @Field("orderId")int orderId,
            @Field("period") int period,
            @Field("frequecy") int frequecy,
            @Field("startDate") int startDate,
            @Field("orderPrice") Double orderPrice,
            @Field("uid")String uid
    );

    @FormUrlEncoded
    @POST("getCurrentBalance")
    Call<Double> getCurrentBalance(
            @Field("uid") String uid);

    @FormUrlEncoded
    @POST("paymentComplete")
    Call<Integer> paymentComplete(
            @Field("id") int id,
            @Field("paymentId") String paymentId
    );

    @FormUrlEncoded
    @POST("getOrders")
    Call<ArrayList<MyOrderList>> getMyOrderList(
            @Field("uid") String uid);

    @FormUrlEncoded
    @POST("getWalletStatement")
    Call<ArrayList<WalletStatement>> getWalletStatementList(
            @Field("uid") String uid);

    @FormUrlEncoded
    @POST("getDeliveryTimeline")
    Call<ArrayList<DeliveryStatus>> getDeliveryStatus(
            @Field("uid") String uid,
            @Field("orderId")String orderId );

    @GET("getRecipes")
    Call<ArrayList<Recipes>> getRecipe();

    @FormUrlEncoded
    @POST("getRecipes")
    Call<ArrayList<Recipes>> getRecipe(@Field("uid") String uid);

    @FormUrlEncoded
    @POST("getNotification")
    Call<ArrayList<Notifications>> getNotification(@Field("uid") String uid);
}