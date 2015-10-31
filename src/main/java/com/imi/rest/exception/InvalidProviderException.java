package com.imi.rest.exception;

public class InvalidProviderException extends ImiException {

    private static final long serialVersionUID = 1L;

    public InvalidProviderException(String provider) {
        super("Provider " + provider + " is invalid.");
    }

    public InvalidProviderException(int providerId) {
        super("Provider Id " + providerId + " is invliad");
    }

}
