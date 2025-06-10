package com.bugzilla.bugzilla.service;

import com.bugzilla.bugzilla.dto.*;
import com.bugzilla.bugzilla.config.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class SupersetService {

    private final SupersetConfig config;
    private final RestTemplate restTemplate = new RestTemplate();

    public GuestTokenResponse getGuestToken(GuestTokenRequest request) {
        // Step 1: Login
        String loginUrl = config.getUrl() + "/api/v1/security/login";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> loginPayload = Map.of(
            "username", config.getUsername(),
            "password", config.getPassword(),
            "provider", "db",
            "refresh", true
        );

        ResponseEntity<Map> loginResp = restTemplate.postForEntity(loginUrl, new HttpEntity<>(loginPayload, headers), Map.class);

        String accessToken = (String) loginResp.getBody().get("access_token");

        // Step 2: Guest Token Request
        String guestTokenUrl = config.getUrl() + "/api/v1/security/guest_token/";

        headers.setBearerAuth(accessToken);

        Map<String, Object> guestPayload = Map.of(
            "user", Map.of(
                "username", "guest",
                "first_name", "Guest",
                "last_name", "User",
                "email", "guest@gmail.com"
            ),
            "resources", new Object[] {
                Map.of("type", "dashboard", "id", request.getDashboardId())
            },
            "rls", new Object[] {}
        );

        ResponseEntity<Map> guestTokenResp = restTemplate.postForEntity(guestTokenUrl, new HttpEntity<>(guestPayload, headers), Map.class);

        String token = (String) guestTokenResp.getBody().get("token");

        return new GuestTokenResponse(token);
    }
}
