package com.staybits.gigmapapi.notifications.application.internal.queryservices;

import java.util.List;

import org.springframework.stereotype.Service;

import com.staybits.gigmapapi.notifications.domain.model.aggregates.UserNotification;
import com.staybits.gigmapapi.notifications.domain.model.queries.GetAllNotificationsByUserIdQuery;
import com.staybits.gigmapapi.notifications.domain.services.NotificationQueryService;
import com.staybits.gigmapapi.notifications.infrastructure.persistence.jpa.repositories.UserNotificationRepository;

@Service
public class NotificationQueryServiceImpl implements NotificationQueryService {

    private final UserNotificationRepository userNotificationRepository;

    public NotificationQueryServiceImpl(UserNotificationRepository userNotificationRepository) {
        this.userNotificationRepository = userNotificationRepository;
    }

    @Override
    public List<UserNotification> handle(GetAllNotificationsByUserIdQuery query) {
        return this.userNotificationRepository.findByUserIdOrderByCreatedAtDesc(query.userId());
    }

}
