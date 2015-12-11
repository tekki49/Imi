package com.imi.rest.exception;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Generic Exception mapper to handle all errors
 *
 */
@ControllerAdvice
public class ExceptionMapper {
    private static int DEFAULT_ERROR_CODE = 20000;

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ApiError handleException(HttpServletResponse response, Exception ex) {
        ApiError apiError = null;
        if (ex instanceof InboundRestException) {
            InboundRestException apiException = (InboundRestException) ex;
            HttpStatus status = apiException.getStatus() != null ? apiException.getStatus() : HttpStatus.INTERNAL_SERVER_ERROR;
            int errorCode = apiException.getCode() != 0 ? apiException.getCode() : DEFAULT_ERROR_CODE;
            apiError = ApiError.createApiError(status, errorCode, apiException.getMessage(), apiException.getDetailedMessage());
        }else {
            apiError = ApiError.createApiError(HttpStatus.INTERNAL_SERVER_ERROR, DEFAULT_ERROR_CODE, "Error happened", ex.getMessage());
        }
        response.setStatus(apiError.getStatus().value());
        return apiError;
    }
}
