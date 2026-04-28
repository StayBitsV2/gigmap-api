package com.staybits.gigmapapi.relatedevents.interfaces.rest.resources;

public record ParticipantResource(
    Long relatedEventId,
    Long userId
) {
    public ParticipantResource {
        if (relatedEventId == null || relatedEventId <= 0) {
            throw new IllegalArgumentException("relatedEventId must be greater than 0");
        }
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("userId must be greater than 0");
        }
    }
}
