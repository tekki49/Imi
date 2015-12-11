package com.imi.rest.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlivoPurchaseResponse {

	private String apiId;
	private String message;
	private List<NumberStatus> numberStatus;
	private String status;
	private String app_id;
	private String error;

	public String getApiId() {
		return apiId;
	}

	@JsonProperty("api_id")
	public void setApiId(String apiId) {
		this.apiId = apiId;
	}

	@JsonIgnore
	public String getMessage() {
		return message;
	}

	@JsonProperty("message")
	public void setMessage(String message) {
		this.message = message;
	}

	public List<NumberStatus> getNumberStatus() {
		return numberStatus;
	}

	@JsonProperty("numbers")
	public void setNumberStatus(List<NumberStatus> numberStatus) {
		this.numberStatus = numberStatus;
	}

	public String getStatus() {
		return status;
	}

	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}

	@JsonIgnore
	public String getApp_id() {
		return app_id;
	}

	@JsonProperty("app_id")
	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
