package com.robin.ws.webserviceexample.models.response;

public enum ErrorMessages {
    MISSING_REQUIRED_FIELD("Missing required field"),
    RECORD_ALREADY_EXISTS("Record already exists"),
    NO_MOVIE_FOUND("Could not find any movies with provided title"),
    NO_USER_FOUND("User with provided id is not found"),
    AUTHENTICATION_FAILED("Authentication failed"),
    GOOGLE_CALENDAR_EVENTS_FETCH("Could not fetch calendar events"),
    GOOGLE_CALENDAR_EVENTS_ADD("Could not add event"),
    EMAIL_ADDRESS_NOT_VERIFIED("Email address could not be verified");


    private String errorMessage;

    ErrorMessages(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
