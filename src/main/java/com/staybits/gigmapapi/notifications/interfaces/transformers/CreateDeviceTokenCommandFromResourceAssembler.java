package com.staybits.gigmapapi.notifications.interfaces.transformers;

import com.staybits.gigmapapi.notifications.domain.model.commands.CreateDeviceTokenCommand;
import com.staybits.gigmapapi.notifications.interfaces.resources.CreateDeviceTokenResource;

public class CreateDeviceTokenCommandFromResourceAssembler {

    public static CreateDeviceTokenCommand toCommandFromResource(CreateDeviceTokenResource resource) {
        return new CreateDeviceTokenCommand(
            resource.userId(),
            resource.token()
        );
    }
}