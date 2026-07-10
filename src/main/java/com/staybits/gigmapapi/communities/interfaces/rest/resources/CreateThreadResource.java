package com.staybits.gigmapapi.communities.interfaces.rest.resources;

public record CreateThreadResource(String title, String content, String imageUrl, Long communityId, Long userId) {}
