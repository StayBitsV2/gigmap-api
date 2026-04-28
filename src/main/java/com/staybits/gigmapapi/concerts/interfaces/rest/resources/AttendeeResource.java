package com.staybits.gigmapapi.concerts.interfaces.rest.resources;

public record AttendeeResource(
    Long concertId,
    Long userId
) {
    public AttendeeResource {
        if (concertId == null || concertId <= 0) {
            throw new IllegalArgumentException("concertId must be greater than 0");
        }
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("userId must be greater than 0");
        }
    }
}
