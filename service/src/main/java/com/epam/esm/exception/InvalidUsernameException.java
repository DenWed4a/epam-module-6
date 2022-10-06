package com.epam.esm.exception;

public class InvalidUsernameException extends RuntimeException{
    private String username;
    public InvalidUsernameException() {
        super();
    }

    public InvalidUsernameException(String message) {
        super(message);
    }

    public InvalidUsernameException(String message, Throwable cause) {
        super(message, cause);
    }
}
