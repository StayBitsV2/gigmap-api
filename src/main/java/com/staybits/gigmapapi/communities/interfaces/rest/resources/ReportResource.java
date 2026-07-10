package com.staybits.gigmapapi.communities.interfaces.rest.resources;

public record ReportResource(Long id, String reason, Long threadId, Long commentId, Long reporterId, String status, String createdAt) {}
