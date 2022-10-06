package com.epam.esm.exception;

public class ServiceBlankFieldException extends RuntimeException{



    public ServiceBlankFieldException(String message) {
        super(message);
    }


    public ServiceBlankFieldException(String message, Throwable cause) {
        super(message, cause);
    }


}
