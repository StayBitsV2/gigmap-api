package com.staybits.gigmapapi.concerts.interfaces.rest.resources;

import java.math.BigDecimal;

public record VenueResource(
    String name,
    String address,
    BigDecimal latitude,
    BigDecimal longitude,
    Integer capacity
) {
}
