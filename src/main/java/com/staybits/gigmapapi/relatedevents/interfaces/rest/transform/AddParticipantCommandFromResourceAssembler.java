package com.staybits.gigmapapi.relatedevents.interfaces.rest.transform;

import com.staybits.gigmapapi.relatedevents.domain.model.commands.AddParticipantCommand;
import com.staybits.gigmapapi.relatedevents.interfaces.rest.resources.ParticipantResource;

public class AddParticipantCommandFromResourceAssembler {

    public static AddParticipantCommand toCommandFromResource(ParticipantResource resource) {
        if (resource == null) return null;
        return new AddParticipantCommand(resource.relatedEventId(), resource.userId());
    }
}
