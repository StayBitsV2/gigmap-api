package com.staybits.gigmapapi.relatedevents.interfaces.rest.resources;

import com.staybits.gigmapapi.concerts.interfaces.rest.resources.VenueResource;

import java.time.LocalDateTime;
import java.util.List;

public record RelatedEventResource(
    Long id,
    Long concertId,
    String titulo,
    LocalDateTime datehour,
    String descripcion,
    String tipo,
    VenueResource venue,
    String status,
    Long organizadorId,
    List<Long> participantes
) {
}
