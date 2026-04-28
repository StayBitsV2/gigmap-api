package com.staybits.gigmapapi.concerts.interfaces.rest.resources;

import java.time.LocalDateTime;

public record CreateConcertResource(
    String title,
    String description,
    String imageUrl,
    LocalDateTime date,
    VenueResource venue,
    String genre,
    String status,
    PlatformResource platform,
    Long userId
) {
    public CreateConcertResource {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title cannot be null or blank");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("description cannot be null or blank");
        }
        if (imageUrl == null || imageUrl.isBlank()) {
            throw new IllegalArgumentException("imageUrl cannot be null or blank");
        }
        if (date == null) {
            throw new IllegalArgumentException("date cannot be null");
        }
        if (venue == null || venue.name() == null || venue.name().isBlank() || venue.address() == null || venue.address().isBlank()) {
            throw new IllegalArgumentException("venue cannot be null and must have name and address");
        }
        if (genre == null || genre.isBlank()) {
            throw new IllegalArgumentException("genre cannot be null or blank");
        }
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("status cannot be null or blank");
        }
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("userId must be greater than 0");
        }
        if (platform == null || platform.platformName() == null || platform.platformName().isBlank()) {
            throw new IllegalArgumentException("platform cannot be null and must have platformName");
        }
    }
}
