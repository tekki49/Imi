package com.imi.rest.exception;

public class InvalidCustomerNameException extends ImiException {

    private static final long serialVersionUID = 1L;

    public InvalidCustomerNameException(String customerName, String provider) {
        super("Customer  " + customerName + " for Provider " + provider
                + " is not registered.");
    }

}
