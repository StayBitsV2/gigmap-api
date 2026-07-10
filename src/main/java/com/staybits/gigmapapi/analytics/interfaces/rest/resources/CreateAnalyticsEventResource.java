package com.staybits.gigmapapi.analytics.interfaces.rest.resources;

public record CreateAnalyticsEventResource(String eventType, Long userId, String metadata) {
    public CreateAnalyticsEventResource {
        if (eventType == null || eventType.isBlank()) {
            throw new IllegalArgumentException("eventType is required");
        }
        if (userId == null) {
            throw new IllegalArgumentException("userId is required");
        }
    }
}
