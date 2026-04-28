package com.staybits.gigmapapi.communities.domain.model.commands;

public record UpdateCommunityCommand(Long id, String name, String imageUrl, String description) {
    
}
