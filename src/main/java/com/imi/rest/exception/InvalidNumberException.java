package com.imi.rest.exception;

public class InvalidNumberException extends ImiException {

    private static final long serialVersionUID = 1L;

    public InvalidNumberException(String number, String provider) {
        super("Number " + number + " for Provider " + provider
                + " is invalid.");
    }

}
