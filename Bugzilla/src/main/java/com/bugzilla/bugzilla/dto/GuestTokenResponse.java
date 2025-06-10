package com.bugzilla.bugzilla.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GuestTokenResponse {
    private String token;
}