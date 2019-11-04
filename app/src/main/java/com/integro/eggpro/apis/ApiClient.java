package com.integro.eggpro.apis;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL ="http://malavalli.in/ibeggx/index.php/Android/";

    public static Retrofit retrofit2=null;

    public static final Retrofit getClient2(){
        if (retrofit2==null) {
            Gson gson =new GsonBuilder().setLenient().create();
            retrofit2 = new Retrofit.Builder()
                    .baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit2;
    }
}
