package com.androidstudy.retrofitokhttp.rest;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;

import java.io.IOException;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by DCLab on 11/21/2015.
 */
public class GitApiRestClient {

    private static GitApi gitApi;

    private static final String baseUrl = "https://api.github.com" ;

    public static GitApi getClient(){
        if (gitApi == null){
            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.interceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Response response = chain.proceed(chain.request());
                    return response;
                }
            });
            Retrofit client = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            gitApi = client.create(GitApi.class);
        }
        return gitApi;
    }


}
