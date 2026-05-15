package com.staybits.gigmapapi.concerts.interfaces.rest.resources;

import java.math.BigDecimal;

public record VenueResource(
    String name,
    String address,
    BigDecimal latitude,
    BigDecimal longitude,
    Integer capacity
) {
    public VenueResource {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Venue name cannot be null or blank");
        }
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Venue address cannot be null or blank");
        }
        if (latitude == null || latitude.compareTo(new BigDecimal("-90")) < 0 || latitude.compareTo(new BigDecimal("90")) > 0) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90");
        }
        if (longitude == null || longitude.compareTo(new BigDecimal("-180")) < 0 || longitude.compareTo(new BigDecimal("180")) > 0) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180");
        }
        if (capacity == null || capacity < 5000 || capacity > 80000) {
            throw new IllegalArgumentException("Capacity must be between 5000 and 80000");
        }
    }
}
