package com.imi.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PlivioPurchaseResponse {

	private String apiId;
	private String message;
	private NumberStatus numberStatus;
	private String status;
	public String getApiId() {
		return apiId;
	}
	@JsonProperty("api_id") 
	public void setApiId(String apiId) {
		this.apiId = apiId;
	}
	public String getMessage() {
		return message;
	}
	@JsonProperty("message")
	public void setMessage(String message) {
		this.message = message;
	}
	public NumberStatus getNumberStatus() {
		return numberStatus;
	}
	@JsonProperty("numbers")
	public void setNumberStatus(NumberStatus numberStatus) {
		this.numberStatus = numberStatus;
	}
	public String getStatus() {
		return status;
	}
	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}
}
