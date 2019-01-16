package com.robin.ws.webserviceexample.models.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BookEventRequest {
    @JsonProperty("startTime")
    private String startTime;
    @JsonProperty("summary")
    private String summary;

    public BookEventRequest() {
    }

    public BookEventRequest(String startTime, String summary) {
        this.startTime = startTime;
        this.summary = summary;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
