package com.staybits.gigmapapi.connections.domain.model.commands;

public record CreateConnectionRequestCommand(Long requesterId, Long targetId) {
}
