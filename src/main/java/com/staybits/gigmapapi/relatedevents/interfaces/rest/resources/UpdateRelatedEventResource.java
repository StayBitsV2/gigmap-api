package com.staybits.gigmapapi.relatedevents.interfaces.rest.resources;

import com.staybits.gigmapapi.concerts.interfaces.rest.resources.VenueResource;

import java.time.LocalDateTime;

public record UpdateRelatedEventResource(
    Long id,
    String titulo,
    LocalDateTime datehour,
    String descripcion,
    VenueResource venue,
    String status
) {
    public UpdateRelatedEventResource {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("id must be greater than 0");
        }
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("titulo cannot be null or blank");
        }
        if (datehour == null) {
            throw new IllegalArgumentException("datehour cannot be null");
        }
        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("descripcion cannot be null or blank");
        }
        if (venue == null || venue.name() == null || venue.name().isBlank() || venue.address() == null || venue.address().isBlank()) {
            throw new IllegalArgumentException("venue cannot be null and must have name and address");
        }
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("status cannot be null or blank");
        }
    }
}
