package com.integro.eggpro.apis;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL2="http://malavalli.in/ibeggx/index.php/Api/";

    public static Retrofit retrofit=null;
    public static Retrofit retrofit2=null;

    public static final Retrofit getClient2(){
        if (retrofit2==null) {
            retrofit2 = new Retrofit.Builder()
                    .baseUrl(BASE_URL2).addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit2;
    }
}
