package com.staybits.gigmapapi.notifications.interfaces.acl;

import java.time.LocalDateTime;

public interface NotificationsContextFacade {
    void notifyAllUsersOfNewPost(Long postId, String postContent, String communityName, String username, Long userId);
    void sendConcertReminder(Long userId, Long concertId, String concertTitle, String venueName, LocalDateTime dateTime);
    void notifyUserOfPostLike(Long authorId, String likerUsername, Long postId);
}