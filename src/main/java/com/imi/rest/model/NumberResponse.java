package com.imi.rest.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class NumberResponse {

	private String api_id;
	private Meta meta;
	private List<Number> objects;

	public String getApi_id() {
		return api_id;
	}

	public void setApi_id(String api_id) {
		this.api_id = api_id;
	}

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
}
