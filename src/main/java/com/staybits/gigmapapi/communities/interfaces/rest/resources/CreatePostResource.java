package com.staybits.gigmapapi.communities.interfaces.rest.resources;

public record CreatePostResource(String content, String imageUrl, Long communityId, Long userId) {
    
}
