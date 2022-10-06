package com.epam.esm.exception;

public class ControllerError {


        private final String errorMessage;
        private final Integer errorCode;

        public ControllerError(String errorMessage, Integer errorCode) {
            this.errorMessage = errorMessage;
            this.errorCode = errorCode;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public Integer getErrorCode() {
            return errorCode;
        }
    }

