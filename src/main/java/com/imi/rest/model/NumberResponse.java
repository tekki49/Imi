package com.imi.rest.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NumberResponse {

    private Meta meta;
    private List<Number> objects;
    private int count;
    
    @JsonIgnore
    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<Number> getObjects() {
        return objects;
    }

    public void setObjects(List<Number> objects) {
        this.objects = objects;
    }

    @JsonProperty("available_phone_numbers")
    public void setAvailablePhoneNumbers(List<Number> objects) {
        this.objects = objects;
    }

    @JsonProperty("numbers")
    public void setNumbers(List<Number> objects) {
        this.objects = objects;
    }

    @JsonIgnore
    public int getCount() {
        return count;
    }

    @JsonProperty("count")
    public void setCount(int count) {
        this.count = count;
    }

}
