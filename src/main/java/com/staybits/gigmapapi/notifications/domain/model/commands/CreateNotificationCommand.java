package com.staybits.gigmapapi.notifications.domain.model.commands;

public record CreateNotificationCommand(String title, String body) {
    public CreateNotificationCommand {
        if (title == null || title.isBlank())
            throw new IllegalArgumentException("Notification title cannot be null or blank");

        if (body == null || body.isBlank())
            throw new IllegalArgumentException("Notification body cannot be null or blank");
    }
}