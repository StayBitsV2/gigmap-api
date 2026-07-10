package com.staybits.gigmapapi.analytics.interfaces.rest.transform;

import com.staybits.gigmapapi.analytics.domain.model.aggregates.AnalyticsEvent;
import com.staybits.gigmapapi.analytics.interfaces.rest.resources.AnalyticsEventResource;

public class AnalyticsEventResourceFromEntityAssembler {
    public static AnalyticsEventResource toResourceFromEntity(AnalyticsEvent entity) {
        return new AnalyticsEventResource(
            entity.getId(),
            entity.getEventType(),
            entity.getUserId(),
            entity.getMetadata(),
            entity.getCreatedAt()
        );
    }
}
