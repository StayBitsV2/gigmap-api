package com.staybits.gigmapapi.analytics.interfaces.rest.resources;

import com.staybits.gigmapapi.analytics.domain.model.valueobjects.AnalyticsEventType;
import java.time.LocalDateTime;

public record AnalyticsEventResource(Long id, AnalyticsEventType eventType, Long userId, String metadata, LocalDateTime createdAt) {
}
