package com.staybits.gigmapapi.notifications.interfaces.resources;

import java.time.LocalDateTime;

public record NotificationResource(
        Long id,
        Long userId,
        String title,
        String body,
        LocalDateTime createdAt,
        LocalDateTime readAt
) {}