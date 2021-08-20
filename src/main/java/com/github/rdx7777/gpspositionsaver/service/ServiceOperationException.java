package com.github.rdx7777.gpspositionsaver.service;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ServiceOperationException extends Exception {

    private final HttpStatus status;

    public ServiceOperationException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }

    public ServiceOperationException(String message, Throwable cause) {
        super(message, cause);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
