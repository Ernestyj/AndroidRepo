package com.daggerstudy.itunesdemo.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * ITunesResultSet
 *
 * @author Jian Yang
 * @date 9/16/2015
 */
public class ITunesResultSet implements Serializable {
    @SerializedName("results")
    private List<ITunesResult> mResults;

    public List<ITunesResult> getResults() {
        return mResults;
    }

    public void setResults(List<ITunesResult> results) {
        mResults = results;
    }
}
