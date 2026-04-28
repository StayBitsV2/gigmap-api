package com.staybits.gigmapapi.communities.domain.events;

public class PostCreatedEvent {
    private final Long postId;
    private final Long userId;
    private final String postContent;
    private final String communityName;
    private final String username;

    public PostCreatedEvent(Long postId, String postContent, String communityName, String username, Long userId) {
        this.postId = postId;
        this.postContent = postContent;
        this.communityName = communityName;
        this.username = username;
        this.userId = userId;
    }

    public Long getPostId() {
        return postId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getPostContent() {
        return postContent;
    }

    public String getCommunityName() {
        return communityName;
    }

    public String getUsername() {
        return username;
    }
}