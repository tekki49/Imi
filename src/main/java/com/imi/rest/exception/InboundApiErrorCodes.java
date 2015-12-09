package com.imi.rest.exception;

import org.springframework.http.HttpStatus;

public enum InboundApiErrorCodes {

    INVALID_PROVIDER_EXCEPTION(HttpStatus.NOT_FOUND, 2000,"Provider details are not correct","Provider details are incorrect or Provider account is suspended because or low funds"),
    INVALID_PROVIDER_ACTION_EXCEPTION(HttpStatus.NOT_FOUND, 2015,"The action cannot be performed for the provider","Provider do not support current action"),
    TWILIO_BALANCE_CHECK_EXCEPTION(HttpStatus.BAD_REQUEST,2001,"Twilio balance check is not possible","Twilio doesnt have api for checking the balance "),
    TWILIO_API_ACCESS_EXCEPTION(HttpStatus.GATEWAY_TIMEOUT,2002,"There is a problem while connecting to the Twilio Api","Provider details might be inccorect or there is problem in establishing connection to Twilio API"),
    TWILIO_SUB_ACCOUNT_CREATION_EXCEPTION(HttpStatus.BAD_REQUEST,2003,"There is a problem with creating a new sub account with Twilio","Provider details might be inccorect or there is problem in establishing connection to Twilio API"),
    TWILIO_SUB_ACCOUNT_DETAILS_EXCEPTION(HttpStatus.BAD_REQUEST,2004,"There is a problem with obtaining sub account details from Twilio","Provider details might be inccorect or there is problem in establishing connection to Twilio API"),
    TWILIO_SUBACCOUNT_USER_ASSOCIATION_EXCEPTION(HttpStatus.BAD_REQUEST,2005,"There is no assocaited SUb Account for the user","No Sub account is associated with the user"),
    TWILIO_ADRESS_CREATION_EXCEPTION(HttpStatus.BAD_REQUEST,2011,"Unable to create address ","Unable to create new address to the sub account"),
    NUMBER_USER_ASSOCIATION_EXCEPTION(HttpStatus.BAD_REQUEST,2006,"The Number requested is not associated with the user","Number requested does not belong to the user"),
    NUMBER_PROVIDER_ASSOCIATION_EXCEPTION(HttpStatus.BAD_REQUEST,2006,"The Number requested is not associated with the master account","Number requested does not belong to the Master account of provider"),
    UNKNOWN_PROVIDER_RESPONSE_EXCEPTION(HttpStatus.BAD_REQUEST,2007,"Unknown response from the service provider","Unknown response which is not detailed in the service provider api reference"),
    INVALID_COUNTRY_CODE_EXCEPTION(HttpStatus.BAD_REQUEST,2008,"Unkown Country code","Country code is invalid or not provided"),
    INVALID_API_PARAMETERS_EXCEPTION(HttpStatus.BAD_REQUEST,2009,"Invalid details while making a call to service provider api","Some of the details are invalid or some required details are missing"),
    INVALID_NUMBER_PURCHASE_EXCEPTION(HttpStatus.BAD_GATEWAY,2010,"Purchase number failed","Number intended for purchase may not be available"),
    PLIVO_APPLICATION_CREATION_EXCEPTION(HttpStatus.BAD_REQUEST,2012,"Plivo application cannot be created","Application details may not be fully present or proper"),
    PLIVO_APPLICATION_UPDATE_EXCEPTION(HttpStatus.BAD_REQUEST,2013,"Plivo application cannot be updated","Application details may not be fully present or proper"),
    PLIVO_APPLICATION_DETAILS_EXCEPTION(HttpStatus.BAD_REQUEST,2014,"Plivo application details cannot be obtained","Application may not be created with Plivo account"),
    ;
    
    private final HttpStatus httpStatusCode;
    private final int code;
    private final String message;
    private final String detailedMessage;

    private InboundApiErrorCodes(HttpStatus httpStatusCode, int code, String message,
            String detailedMessage) {
        this.httpStatusCode = httpStatusCode;
        this.code = code;
        this.message = message;
        this.detailedMessage = detailedMessage;
    }

    public HttpStatus getHttpStatusCode() {
        return httpStatusCode;
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

}
