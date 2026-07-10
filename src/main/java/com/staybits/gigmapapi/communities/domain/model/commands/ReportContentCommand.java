package com.staybits.gigmapapi.communities.domain.model.commands;

public record ReportContentCommand(Long threadId, Long commentId, Long reporterId, String reason) {}
