package com.springboot.taxservice.model;
public class TaxServiceError {
    private final int status;
    private final String message;

    public TaxServiceError(int status, String message){
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}
