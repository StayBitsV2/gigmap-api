package com.staybits.gigmapapi.communities.interfaces.rest.resources;

public record ReactionResource(Long id, String emoji, Long userId, Long threadId, Long commentId) {}
