package com.staybits.gigmapapi.concerts.interfaces.rest.resources;

public record DeleteConcertResource(Long id) {
    public DeleteConcertResource {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("id must be greater than 0");
        }
    }
}
