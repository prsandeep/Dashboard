package com.config.osticket.dto;



public class GuestTokenRequest {
    private String dashboardId;  // or Integer if appropriate

    public GuestTokenRequest() {
    }

    public String getDashboardId() {
        return dashboardId;
    }

    public GuestTokenRequest(String dashboardId) {
        this.dashboardId = dashboardId;
    }

    public void setDashboardId(String dashboardId) {
        this.dashboardId = dashboardId;
    }
// Lombok @Data generates getter/setter for dashboardId
}

