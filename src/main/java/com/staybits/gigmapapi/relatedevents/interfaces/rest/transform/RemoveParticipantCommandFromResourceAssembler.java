package com.staybits.gigmapapi.relatedevents.interfaces.rest.transform;

import com.staybits.gigmapapi.relatedevents.domain.model.commands.RemoveParticipantCommand;
import com.staybits.gigmapapi.relatedevents.interfaces.rest.resources.ParticipantResource;

public class RemoveParticipantCommandFromResourceAssembler {

    public static RemoveParticipantCommand toCommandFromResource(ParticipantResource resource) {
        if (resource == null) return null;
        return new RemoveParticipantCommand(resource.relatedEventId(), resource.userId());
    }
}
