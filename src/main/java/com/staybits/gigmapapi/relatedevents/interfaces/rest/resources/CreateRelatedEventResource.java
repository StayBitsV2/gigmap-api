package com.staybits.gigmapapi.relatedevents.interfaces.rest.resources;

import com.staybits.gigmapapi.concerts.interfaces.rest.resources.VenueResource;

import java.time.LocalDateTime;

public record CreateRelatedEventResource(
    Long concertId,
    String titulo,
    LocalDateTime datehour,
    String descripcion,
    String tipo,
    VenueResource venue,
    String status,
    Long organizadorId
) {
    public CreateRelatedEventResource {
        if (concertId == null || concertId <= 0) {
            throw new IllegalArgumentException("concertId must be greater than 0");
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
        if (tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException("tipo cannot be null or blank");
        }
        if (venue == null || venue.name() == null || venue.name().isBlank() || venue.address() == null || venue.address().isBlank()) {
            throw new IllegalArgumentException("venue cannot be null and must have name and address");
        }
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("status cannot be null or blank");
        }
        if (organizadorId == null || organizadorId <= 0) {
            throw new IllegalArgumentException("organizadorId must be greater than 0");
        }
    }
}
