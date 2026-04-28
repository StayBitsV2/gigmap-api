package com.staybits.gigmapapi.concerts.interfaces.rest.resources;


import java.time.LocalDateTime;
import java.util.List;

public record ConcertResource(
    Long id,
    String name,
    LocalDateTime date,
    String status,
    String description,
    String image,
    String genre,
    PlatformResource platform,
    VenueResource venue,
    List<Long> attendees
) {
}
