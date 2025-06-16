package com.config.osticket.controller;


import com.config.osticket.dto.GuestTokenRequest;
import com.config.osticket.dto.GuestTokenResponse;
import com.config.osticket.service.SupersetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/osticket")
public class SupersetController {

    private final SupersetService service;

    public SupersetController(SupersetService service) {
        this.service = service;
    }

    @PostMapping("/guest-token")
    public GuestTokenResponse getGuestToken(@RequestBody GuestTokenRequest request) {
        return service.getGuestToken(request);
    }
}
