package com.staybits.gigmapapi.communities.domain.model.commands;

public record CreateThreadCommand(String title, String content, String imageUrl, Long communityId, Long userId) {}
