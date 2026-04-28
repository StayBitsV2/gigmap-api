package com.staybits.gigmapapi.notifications.domain.services;

import com.staybits.gigmapapi.notifications.domain.model.aggregates.Notification;
import com.staybits.gigmapapi.notifications.domain.model.commands.CreateNotificationCommand;

public interface NotificationCommandService {
    Notification handle(CreateNotificationCommand command);
}