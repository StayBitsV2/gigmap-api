package com.staybits.gigmapapi.analytics.domain.model.aggregates;

import com.staybits.gigmapapi.analytics.domain.model.valueobjects.AnalyticsEventType;
import com.staybits.gigmapapi.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "analytics_events")
public class AnalyticsEvent extends AuditableAbstractAggregateRoot<AnalyticsEvent> {

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private AnalyticsEventType eventType;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(columnDefinition = "TEXT")
    private String metadata;

    public AnalyticsEvent() {
        super();
    }

    public AnalyticsEvent(AnalyticsEventType eventType, Long userId, String metadata) {
        this.eventType = eventType;
        this.userId = userId;
        this.metadata = metadata;
    }
}
