package com.staybits.gigmapapi.communities.application.internal.eventhandlers;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.staybits.gigmapapi.communities.domain.events.PostLikedEvent;
import com.staybits.gigmapapi.notifications.interfaces.acl.NotificationsContextFacade;

@Service
public class PostLikedEventHandler {
    private final NotificationsContextFacade notificationsContextFacade;

    public PostLikedEventHandler(NotificationsContextFacade notificationsContextFacade) {
        this.notificationsContextFacade = notificationsContextFacade;
    }

    @EventListener(PostLikedEvent.class)
    public void handle(PostLikedEvent event) {
        this.notificationsContextFacade.notifyUserOfPostLike(event.getAuthorId(), event.getLikerUsername(),
                event.getPostId());
    }
}
