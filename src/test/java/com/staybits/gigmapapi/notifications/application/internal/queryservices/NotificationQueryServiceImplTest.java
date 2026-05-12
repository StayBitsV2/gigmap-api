package com.staybits.gigmapapi.notifications.application.internal.queryservices;

import com.staybits.gigmapapi.notifications.domain.model.aggregates.UserNotification;
import com.staybits.gigmapapi.notifications.domain.model.queries.GetAllNotificationsByUserIdQuery;
import com.staybits.gigmapapi.notifications.infrastructure.persistence.jpa.repositories.UserNotificationRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationQueryServiceImplTest {

    @Mock
    UserNotificationRepository userNotificationRepository;

    @InjectMocks
    NotificationQueryServiceImpl notificationQueryServiceImpl;

    @Test
    void Test_GetNotificationsByUserId() {

        UserNotification notification1 = new UserNotification();
        UserNotification notification2 = new UserNotification();

        List<UserNotification> notifications =
                List.of(notification1, notification2);

        when(userNotificationRepository
                .findByUserIdOrderByCreatedAtDesc(1L))
                .thenReturn(notifications);

        var result =
                notificationQueryServiceImpl.handle(
                        new GetAllNotificationsByUserIdQuery(1L)
                );

        assertEquals(2, result.size());
    }
}