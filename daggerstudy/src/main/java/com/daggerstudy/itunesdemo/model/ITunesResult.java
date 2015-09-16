package com.daggerstudy.itunesdemo.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * ITunesResult
 *
 * @author Jian Yang
 * @date 9/16/2015
 */
public class ITunesResult implements Serializable {
    @SerializedName("collectionName")
    private String mCollectionName;

    public String getCollectionName() {
        return mCollectionName;
    }

    public void setCollectionName(String collectionName) {
        mCollectionName = collectionName;
    }

    @Override
    public String toString() {
        return mCollectionName;
    }

}
