package com.staybits.gigmapapi.notifications.application.internal.commandservices;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.staybits.gigmapapi.notifications.domain.model.aggregates.DeviceToken;
import com.staybits.gigmapapi.notifications.domain.model.commands.CreateDeviceTokenCommand;
import com.staybits.gigmapapi.notifications.domain.services.DeviceTokenCommandService;
import com.staybits.gigmapapi.notifications.infrastructure.persistence.jpa.repositories.DeviceTokenRepository;

@Service
public class DeviceTokenCommandServiceImpl implements DeviceTokenCommandService{
    private final DeviceTokenRepository repository;

    public DeviceTokenCommandServiceImpl(DeviceTokenRepository repository) {
        this.repository = repository;
    }

    @Override
    public DeviceToken handle(CreateDeviceTokenCommand command) {
        Optional<DeviceToken> existing = repository.findByToken(command.token());

        if (existing.isPresent())
            throw new IllegalArgumentException("Failed to create DeviceToken: Token already exists.");

        DeviceToken newToken = new DeviceToken(command);

        return repository.save(newToken);
    }
}
