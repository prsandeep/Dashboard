package com.microservices.authservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a user operation is invalid or restricted.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserOperationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Create a user operation exception with a specific message.
     *
     * @param message the error message
     */
    public UserOperationException(String message) {
        super(message);
    }
}