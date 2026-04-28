package com.staybits.gigmapapi.notifications.domain.model.aggregates;

import java.time.LocalDateTime;

import com.staybits.gigmapapi.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Entity
@Getter
public class UserNotification extends AuditableAbstractAggregateRoot<UserNotification> {

    @Column(nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_id", nullable = false)
    private Notification notification;

    private LocalDateTime readAt;
    private LocalDateTime deliveredAt;

    public UserNotification() {}

    public UserNotification(Long userId, Notification notification) {
        this.userId = userId;
        this.notification = notification;
    }
}