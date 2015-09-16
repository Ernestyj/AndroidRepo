package com.daggerstudy.itunesdemo.rest;

import com.daggerstudy.itunesdemo.model.ITunesResultSet;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * ITunesService
 *
 * @author Jian Yang
 * @date 9/16/2015
 */
public interface ITunesService {
    @GET("/search")
    void search(@Query("term")String term, @Query("entity")String entity, Callback<ITunesResultSet> callback);
}
