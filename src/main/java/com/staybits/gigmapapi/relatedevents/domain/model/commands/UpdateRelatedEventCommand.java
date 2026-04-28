package com.staybits.gigmapapi.relatedevents.domain.model.commands;

import com.staybits.gigmapapi.concerts.domain.model.entities.Venue;
import com.staybits.gigmapapi.concerts.domain.model.valueobjects.ConcertStatus;

import java.time.LocalDateTime;

public record UpdateRelatedEventCommand(
    Long id,
    String titulo,
    LocalDateTime datehour,
    String descripcion,
    Venue venue,
    ConcertStatus status
) {
}
