package com.staybits.gigmapapi.notifications.domain.services;

import java.util.List;

import com.staybits.gigmapapi.notifications.domain.model.aggregates.UserNotification;
import com.staybits.gigmapapi.notifications.domain.model.queries.GetAllNotificationsByUserIdQuery;

public interface NotificationQueryService {
    List<UserNotification> handle(GetAllNotificationsByUserIdQuery query);
}
