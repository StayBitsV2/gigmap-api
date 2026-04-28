package com.staybits.gigmapapi.relatedevents.domain.model.commands;

public record RemoveParticipantCommand(
    Long relatedEventId,
    Long userId
) {
}
