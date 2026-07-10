package com.staybits.gigmapapi.communities.interfaces.rest.resources;

public record CreateReportResource(Long threadId, Long commentId, Long reporterId, String reason) {}
