package com.androidstudy.retrofitokhttp.rest;

import com.androidstudy.retrofitokhttp.entity.GitResult;
import com.androidstudy.retrofitokhttp.entity.Item;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * https://api.github.com/search/users?q=tom
 *
 * Created by DCLab on 11/21/2015.
 */
public interface GitApi {

    @Headers("User-Agent: Retrofit2.0Tutorial-App")
    @GET("/search/users")
    Call<GitResult> getUsersNamedTom(@Query("q") String name);

    @POST("/user/create")
    Call<Item> createUser(@Body String name, @Body String email);

    @PUT("/user/{id}/update")
    Call<Item> updateUser(@Path("id") String id, @Body Item user);

}
