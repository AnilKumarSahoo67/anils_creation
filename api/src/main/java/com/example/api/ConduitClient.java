package com.example.api;

import com.example.api.services.ConduitApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConduitClient {
    private static final String BASE_URL = "http://conduit.productionready.io/api/";
    private static Retrofit retrofit = null;
    private static ConduitClient mInstance;

    private ConduitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public static synchronized ConduitClient getInstance(){
        if (mInstance==null){
            mInstance=new ConduitClient();
        }
        return mInstance;
    }
    public ConduitApi getApi(){
        return retrofit.create(ConduitApi.class);
    }
}