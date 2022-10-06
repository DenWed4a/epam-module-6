package com.epam.esm.exception;

public class ServiceSourceNotFoundException extends RuntimeException{
    private Integer sourceId;



    public ServiceSourceNotFoundException(String message) {
        super(message);
    }

    public ServiceSourceNotFoundException(Integer sourceId){
        this.sourceId = sourceId;
    }

    public ServiceSourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public Integer getSourceId() {
        return sourceId;
    }
}
