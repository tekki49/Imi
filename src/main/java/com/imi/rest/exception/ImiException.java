package com.imi.rest.exception;

public class ImiException extends Exception {

    private static final long serialVersionUID = 1L;
    private String cause;
    private String message;

    public ImiException() {
        super();
    }

    public ImiException(String cause,String message) {
        super(message);
        this.message = message;
        this.cause=cause;
    }
    
    public ImiException(String message) {
        super(message);
        this.message = message;
    }

    public ImiException(Throwable cause) {
        super(cause);
    }

    @Override
    public String toString() {
        return "{ cause :" + cause + ",message:" + message + "}";
    }

    @Override
    public String getMessage() {
        return message;
    }

}
