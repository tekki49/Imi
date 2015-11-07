package com.imi.rest.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationResponseList {
	
	private Meta meta;
    private List<ApplicationResponse> objects;
    private int count;

    @JsonIgnore
    public Meta getMeta() {
        return meta;
    }

    @JsonProperty("meta")
    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<ApplicationResponse> getObjects() {
		return objects;
	}

	public void setObjects(List<ApplicationResponse> objects) {
		this.objects = objects;
	}

	@JsonProperty("incoming_phone_numbers")
    public void setIncoming_phone_numbers(List<ApplicationResponse> objects) {
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
