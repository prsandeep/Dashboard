package com.bugzilla.bugzilla.controller;

import com.bugzilla.bugzilla.service.*;
import com.bugzilla.bugzilla.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/superset")
@RequiredArgsConstructor// adjust CORS as needed
public class SupersetController {

    private final SupersetService service;

    @PostMapping("/guest-token")
    public GuestTokenResponse getGuestToken(@RequestBody GuestTokenRequest request) {
        return service.getGuestToken(request);
    }
}