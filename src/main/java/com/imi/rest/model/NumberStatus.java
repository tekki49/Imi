package com.imi.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NumberStatus {

    private String number;
    private String status;

    public String getNumber() {
        return number;
    }

    @JsonProperty("number")
    public void setNumber(String number) {
        this.number = number;
    }

    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }
}
