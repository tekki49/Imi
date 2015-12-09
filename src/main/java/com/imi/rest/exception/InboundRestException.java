package com.imi.rest.exception;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class InboundRestException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	private HttpStatus status;
	private int code;
	private String message;
	private String detailedMessage;

	public InboundRestException() {
		super();
	}

	public InboundRestException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InboundRestException(String message, Throwable cause) {
		super(message, cause);
	}

	public InboundRestException(String message) {
		super(message);
	}

	public InboundRestException(Throwable cause) {
		super(cause);
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDetailedMessage() {
        return detailedMessage;
    }

    public void setDetailedMessage(String detailedMessage) {
        this.detailedMessage = detailedMessage;
    }

    public static InboundRestException createApiException(HttpStatus status, int code,
			String message, String detailedMessage, Throwable rootCause) {
		InboundRestException exception = new InboundRestException(rootCause);
		exception.setStatus(status);
		exception.setCode(code);
		exception.setMessage(message);
		exception.setDetailedMessage(detailedMessage);
		return exception;
	}
	
	public static InboundRestException createApiException(InboundApiErrorCodes errorcode, String additionalMessage, Throwable rootCause) {
		InboundRestException exception = new InboundRestException(rootCause);
		exception.setStatus(errorcode.getHttpStatusCode());
		exception.setCode(errorcode.getCode());
		exception.setMessage(errorcode.getMessage());
		exception.setDetailedMessage(errorcode.getDetailedMessage());
		return exception;
	}
	
	public static InboundRestException createApiException(InboundApiErrorCodes errorcode, String additionalMessage) {
		InboundRestException exception = new InboundRestException();
		exception.setStatus(errorcode.getHttpStatusCode());
		exception.setCode(errorcode.getCode());
		exception.setMessage(errorcode.getMessage());
		exception.setDetailedMessage(additionalMessage);
		return exception;
	}

}
