package com.staybits.gigmapapi.connections.interfaces.rest.transform;

import com.staybits.gigmapapi.connections.domain.model.commands.CreateConnectionRequestCommand;
import com.staybits.gigmapapi.connections.interfaces.rest.resources.CreateConnectionRequestResource;

public class CreateConnectionRequestCommandFromResourceAssembler {
    public static CreateConnectionRequestCommand toCommandFromResource(Long requesterId, CreateConnectionRequestResource resource) {
        return new CreateConnectionRequestCommand(requesterId, resource.targetId());
    }
}
