package com.staybits.gigmapapi.concerts.domain.model.commands;

public record AddAttendeeCommand(
    Long concertId,
    Long userId
) {
}
