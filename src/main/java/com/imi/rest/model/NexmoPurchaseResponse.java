package com.imi.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NexmoPurchaseResponse {

	private String errorcode;
	private String errorCodeLabel;

	public String getErrorcode() {
		return errorcode;
	}

	@JsonProperty("error-code")
	public void setErrorcode(String errorcode) {
		this.errorcode = errorcode;
	}

	public String getErrorCodeLabel() {
		return errorCodeLabel;
	}

	@JsonProperty("error-code-label")
	public void setErrorCodeLabel(String errorCodeLabel) {
		this.errorCodeLabel = errorCodeLabel;
	}

}
