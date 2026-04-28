package com.staybits.gigmapapi.communities.domain.events;

import lombok.Getter;

@Getter
public class PostLikedEvent {
    private final Long postId;
    private final String likerUsername;
    private final Long authorId;

    public PostLikedEvent(Long postId, String likerUsername, Long authorId) {
        this.postId = postId;
        this.likerUsername = likerUsername;
        this.authorId = authorId;
    }
}
