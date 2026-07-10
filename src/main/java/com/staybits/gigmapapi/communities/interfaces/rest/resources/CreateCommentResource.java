package com.staybits.gigmapapi.communities.interfaces.rest.resources;

public record CreateCommentResource(Long threadId, Long userId, String content) {}
