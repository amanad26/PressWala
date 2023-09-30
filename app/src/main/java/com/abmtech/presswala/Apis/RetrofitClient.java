package com.abmtech.presswala.Apis;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {


    public static Retrofit RETRFIT = null;


    public static ApiInterface getClient(Context context) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES);

        client.addInterceptor(interceptor);


        if (RETRFIT == null) {
            RETRFIT = new Retrofit.Builder()
                    .baseUrl(BaseUrls.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client.build())
                    .build();
        }

        ApiInterface apiInterface;
        apiInterface = RETRFIT.create(ApiInterface.class);
        RETRFIT = null;
        return apiInterface;

    }

    public static ApiInterface appBlockClient(Context context) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES);

        client.addInterceptor(interceptor);

        if (RETRFIT == null) {
            RETRFIT = new Retrofit.Builder()
                    .baseUrl(BaseUrls.APP_BLOCK_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client.build())
                    .build();
        }

        ApiInterface apiInterface;
        apiInterface = RETRFIT.create(ApiInterface.class);
        RETRFIT = null;
        return apiInterface;


    }


}
