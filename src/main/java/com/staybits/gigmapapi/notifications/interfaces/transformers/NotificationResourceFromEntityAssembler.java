package com.staybits.gigmapapi.notifications.interfaces.transformers;

import com.staybits.gigmapapi.notifications.domain.model.aggregates.UserNotification;
import com.staybits.gigmapapi.notifications.interfaces.resources.NotificationResource;

public class NotificationResourceFromEntityAssembler {
        public static NotificationResource toResourceFromEntity(UserNotification entity) {
        return new NotificationResource(
                entity.getId(),
                entity.getUserId(),
                entity.getNotification().getTitle(),
                entity.getNotification().getBody(),
                entity.getCreatedAt(),
                entity.getReadAt()
        );
    }
}
