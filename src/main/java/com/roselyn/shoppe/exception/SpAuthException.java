package com.roselyn.shoppe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class SpAuthException extends RuntimeException {
    public SpAuthException(String message) {
        super(message);
    }
}
