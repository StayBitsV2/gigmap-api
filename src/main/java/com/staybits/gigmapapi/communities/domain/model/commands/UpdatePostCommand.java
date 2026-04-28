package com.staybits.gigmapapi.communities.domain.model.commands;

public record UpdatePostCommand(Long id, String content, String imageUrl) {
    
}
