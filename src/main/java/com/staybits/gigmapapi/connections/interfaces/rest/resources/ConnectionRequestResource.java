package com.staybits.gigmapapi.connections.interfaces.rest.resources;

public record ConnectionRequestResource(Long id, Long requesterId, Long targetId, String status, String createdAt) {
}
