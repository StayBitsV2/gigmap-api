package com.staybits.gigmapapi.notifications.interfaces.transformers;

import com.staybits.gigmapapi.notifications.domain.model.aggregates.DeviceToken;
import com.staybits.gigmapapi.notifications.interfaces.resources.DeviceTokenResource;

public class DeviceTokenResourceFromEntityAssembler {

    public static DeviceTokenResource toResourceFromEntity(DeviceToken entity) {
        return new DeviceTokenResource(
            entity.getUserId(),
            entity.getToken()
        );
    }
}
