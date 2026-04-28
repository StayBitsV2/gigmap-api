package com.staybits.gigmapapi.relatedevents.domain.model.commands;

public record AddParticipantCommand(
    Long relatedEventId,
    Long userId
) {
}
