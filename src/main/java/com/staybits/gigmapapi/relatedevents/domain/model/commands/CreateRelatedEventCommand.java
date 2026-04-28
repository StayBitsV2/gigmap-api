package com.staybits.gigmapapi.relatedevents.domain.model.commands;

import com.staybits.gigmapapi.concerts.domain.model.entities.Venue;
import com.staybits.gigmapapi.concerts.domain.model.valueobjects.ConcertStatus;
import com.staybits.gigmapapi.relatedevents.domain.model.valueobjects.EventType;

import java.time.LocalDateTime;

public record CreateRelatedEventCommand(
    Long concertId,
    String titulo,
    LocalDateTime datehour,
    String descripcion,
    EventType tipo,
    Venue venue,
    ConcertStatus status,
    Long organizadorId
) {
}
