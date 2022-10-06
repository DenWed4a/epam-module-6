package com.epam.esm.exception;

import org.springframework.validation.BindingResult;

public class CustomNotValidArgumentException extends RuntimeException {
    private BindingResult bindingResult;

    public CustomNotValidArgumentException() {
        super();
    }

    public CustomNotValidArgumentException(String message) {
        super(message);
    }

    public CustomNotValidArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomNotValidArgumentException(Throwable cause) {
        super(cause);
    }
    public CustomNotValidArgumentException(BindingResult bindingResult){
        super();
        this.bindingResult = bindingResult;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }
}
