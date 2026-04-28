package com.staybits.gigmapapi.communities.application.internal.eventhandlers;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.staybits.gigmapapi.communities.domain.events.PostCreatedEvent;
import com.staybits.gigmapapi.notifications.interfaces.acl.NotificationsContextFacade;

@Service
public class PostCreatedEventHandler {
    private final NotificationsContextFacade notificationsContextFacade;

    public PostCreatedEventHandler(NotificationsContextFacade notificationsContextFacade) {
        this.notificationsContextFacade = notificationsContextFacade;
    }

    @EventListener(PostCreatedEvent.class)
    public void handle(PostCreatedEvent event) {
        this.notificationsContextFacade.notifyAllUsersOfNewPost(event.getPostId(), event.getPostContent(), event.getCommunityName(), event.getUsername(), event.getUserId());
    }
}