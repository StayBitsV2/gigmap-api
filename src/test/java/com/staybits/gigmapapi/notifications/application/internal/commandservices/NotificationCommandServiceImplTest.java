package com.staybits.gigmapapi.notifications.application.internal.commandservices;

import com.staybits.gigmapapi.notifications.domain.model.aggregates.Notification;
import com.staybits.gigmapapi.notifications.domain.model.commands.CreateNotificationCommand;
import com.staybits.gigmapapi.notifications.infrastructure.persistence.jpa.repositories.NotificationRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationCommandServiceImplTest {

    @Mock
    NotificationRepository notificationRepository;

    @InjectMocks
    NotificationCommandServiceImpl notificationCommandServiceImpl;

    @Test
    void Test_CreateNotification() {

        CreateNotificationCommand command =
                new CreateNotificationCommand(
                        "Nuevo mensaje",
                        "Descripcion de prueba"

                );

        when(notificationRepository.save(any(Notification.class)))
                .thenAnswer(i -> i.getArgument(0));

        var result =
                notificationCommandServiceImpl.handle(command);

        assertEquals("Nuevo mensaje", result.getTitle());
    }


}