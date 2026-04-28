package com.staybits.gigmapapi.concerts.domain.model.commands;

public record RemoveAttendeeCommand(
    Long concertId,
    Long userId
) {
}
