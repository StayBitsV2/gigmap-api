package com.staybits.gigmapapi.notifications.application.internal.commandservices;

import org.springframework.stereotype.Service;

import com.staybits.gigmapapi.notifications.domain.model.aggregates.Notification;
import com.staybits.gigmapapi.notifications.domain.model.commands.CreateNotificationCommand;
import com.staybits.gigmapapi.notifications.domain.services.NotificationCommandService;
import com.staybits.gigmapapi.notifications.infrastructure.persistence.jpa.repositories.NotificationRepository;

@Service
public class NotificationCommandServiceImpl implements NotificationCommandService{
    private final NotificationRepository notificationRepository;

    public NotificationCommandServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Notification handle(CreateNotificationCommand command) {
        var newNotification = new Notification(command);

        try {
            this.notificationRepository.save(newNotification);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Notification", e);
        }

        return newNotification;
    }
}