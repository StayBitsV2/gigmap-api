package com.staybits.gigmapapi.communities.domain.model.commands;

public record AddCommentCommand(Long threadId, Long userId, String content) {}
