package com.staybits.gigmapapi.connections.interfaces.rest.resources;

public record ConnectionResource(Long id, Long connectedUserId, String connectedUsername, String connectedUserImage, String createdAt) {
}
