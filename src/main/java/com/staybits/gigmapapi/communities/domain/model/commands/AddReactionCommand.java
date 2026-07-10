package com.staybits.gigmapapi.communities.domain.model.commands;

import com.staybits.gigmapapi.communities.domain.model.valueobjects.ReactionEmoji;

public record AddReactionCommand(Long threadId, Long commentId, Long userId, ReactionEmoji emoji) {}
