package com.microservices.authservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data Transfer Object for simple message responses.
 */
@Data
@AllArgsConstructor
public class MessageResponse {
    /**
     * Response message.
     */
    private String message;
}