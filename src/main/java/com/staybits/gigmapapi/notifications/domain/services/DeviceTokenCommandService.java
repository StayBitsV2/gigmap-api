package com.staybits.gigmapapi.notifications.domain.services;

import com.staybits.gigmapapi.notifications.domain.model.aggregates.DeviceToken;
import com.staybits.gigmapapi.notifications.domain.model.commands.CreateDeviceTokenCommand;

public interface DeviceTokenCommandService {
    DeviceToken handle(CreateDeviceTokenCommand command);
}
