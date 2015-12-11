package com.imi.rest.exception;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ApiError {
	private final HttpStatus status;
	private final int code;
	private final String message;
	private final String detailedMessage;

	public ApiError(HttpStatus status, int code, String message, String detailedMessage) {
		this.status = status;
		this.code = code;
		this.message = message;
		this.detailedMessage = detailedMessage;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public String getDetailedMessage() {
		return detailedMessage;
	}

	public static ApiError createApiError(HttpStatus status, int code, String message, String detailedMessage) {
		return new ApiError(status, code, message, detailedMessage);
	}
}
