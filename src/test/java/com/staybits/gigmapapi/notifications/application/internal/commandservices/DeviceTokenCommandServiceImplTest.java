package com.staybits.gigmapapi.notifications.application.internal.commandservices;

import com.staybits.gigmapapi.notifications.domain.model.aggregates.DeviceToken;
import com.staybits.gigmapapi.notifications.domain.model.commands.CreateDeviceTokenCommand;
import com.staybits.gigmapapi.notifications.infrastructure.persistence.jpa.repositories.DeviceTokenRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeviceTokenCommandServiceImplTest {

    @Mock
    DeviceTokenRepository repository;

    @InjectMocks
    DeviceTokenCommandServiceImpl deviceTokenCommandServiceImpl;

    @Test
    void Test_CreateDeviceToken() {

        CreateDeviceTokenCommand command =
                new CreateDeviceTokenCommand(
                        1L,
                        "token123"
                );

        when(repository.findByToken("token123"))
                .thenReturn(Optional.empty());

        when(repository.save(any(DeviceToken.class)))
                .thenAnswer(i -> i.getArgument(0));

        var result =
                deviceTokenCommandServiceImpl.handle(command);

        assertEquals("token123", result.getToken());
    }

    @Test
    void Test_CreateDeviceToken_TokenAlreadyExists() {

        CreateDeviceTokenCommand command =
                new CreateDeviceTokenCommand(
                        1L,
                        "token123"
                );

        DeviceToken token =
                new DeviceToken(command);

        when(repository.findByToken("token123"))
                .thenReturn(Optional.of(token));

        assertThrows(
                IllegalArgumentException.class,
                () -> deviceTokenCommandServiceImpl.handle(command)
        );
    }
}