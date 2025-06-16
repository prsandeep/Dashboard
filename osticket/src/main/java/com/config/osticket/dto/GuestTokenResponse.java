package com.config.osticket.dto;




import lombok.AllArgsConstructor;
import lombok.Data;


public class GuestTokenResponse {
    private String token;

    public String getToken() {
        return token;
    }

    public GuestTokenResponse() {
    }

    public GuestTokenResponse(String token) {
        this.token = token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
