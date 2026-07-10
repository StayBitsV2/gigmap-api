package com.staybits.gigmapapi.communities.interfaces.rest.resources;

public record CommentResource(Long id, Long threadId, Long userId, String userName, String content, String createdAt) {}
