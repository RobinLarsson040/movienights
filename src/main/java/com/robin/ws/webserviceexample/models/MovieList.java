package com.robin.ws.webserviceexample.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.robin.ws.webserviceexample.entity.MovieEntity;


import java.util.ArrayList;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieList {

    @JsonProperty("Search")
    private List<MovieEntity> search = new ArrayList();
    @JsonProperty("totalResults")
    private String totalResults;
    @JsonProperty("Response")
    private String response;


    public MovieList() {
    }

    public List<MovieEntity> getSearch() {
        return search;
    }

    public void setSearch(List<MovieEntity> search) {
        this.search = search;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
