package com.staybits.gigmapapi.communities.interfaces.rest.resources;

public record CreateReactionResource(Long threadId, Long commentId, Long userId, String emoji) {}
