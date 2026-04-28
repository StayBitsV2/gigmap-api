package com.staybits.gigmapapi.concerts.domain.model.commands;

import com.staybits.gigmapapi.concerts.domain.model.entities.Venue;
import com.staybits.gigmapapi.concerts.domain.model.valueobjects.ConcertStatus;

import java.time.LocalDateTime;

public record UpdateConcertCommand(
    Long id,
    String title,
    LocalDateTime datehour,
    String description,
    String imageUrl,
    Venue venue,
    ConcertStatus status
) {
}
