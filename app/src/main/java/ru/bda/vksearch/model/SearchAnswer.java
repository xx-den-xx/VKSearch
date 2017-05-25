package ru.bda.vksearch.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * email: denbelobaba@gmail.com
 *
 * @author Belobaba Denis
 */

public class SearchAnswer {
    @SerializedName("response")
    @Expose
    private SearchResponse response;

    public SearchResponse getResponse() {
        return response;
    }

    public void setResponse(SearchResponse response) {
        this.response = response;
    }
}
