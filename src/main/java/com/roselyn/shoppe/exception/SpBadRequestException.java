package com.roselyn.shoppe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SpBadRequestException extends RuntimeException {
    public SpBadRequestException(String message) {
        super(message);
    }
}
