package com.staybits.gigmapapi.notifications.application.acl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import com.staybits.gigmapapi.notifications.application.internal.outboundservices.CloudMessagingService;
import com.staybits.gigmapapi.notifications.domain.model.aggregates.DeviceToken;
import com.staybits.gigmapapi.notifications.domain.model.aggregates.Notification;
import com.staybits.gigmapapi.notifications.domain.model.aggregates.UserNotification;
import com.staybits.gigmapapi.notifications.infrastructure.persistence.jpa.repositories.UserNotificationRepository;
import com.staybits.gigmapapi.notifications.infrastructure.persistence.jpa.repositories.DeviceTokenRepository;
import com.staybits.gigmapapi.notifications.infrastructure.persistence.jpa.repositories.NotificationRepository;
import com.staybits.gigmapapi.notifications.interfaces.acl.NotificationsContextFacade;

@Service
public class NotificationsContextFacadeImpl implements NotificationsContextFacade {
    private final NotificationRepository notificationRepository;
    private final UserNotificationRepository userNotificationRepository;
    private final DeviceTokenRepository deviceTokenRepository;
    private final CloudMessagingService cloudMessagingService;
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(NotificationsContextFacadeImpl.class);

    public NotificationsContextFacadeImpl(
        NotificationRepository notificationRepository,
        UserNotificationRepository userNotificationRepository,
        DeviceTokenRepository deviceTokenRepository,
        CloudMessagingService cloudMessagingService
    ) {
        this.notificationRepository = notificationRepository;
        this.userNotificationRepository = userNotificationRepository;
        this.deviceTokenRepository = deviceTokenRepository;
        this.cloudMessagingService = cloudMessagingService;
    }

    @Override
    public void notifyAllUsersOfNewPost(Long postId, String postContent, String communityName, String username, Long userId) {
        String title = username + " en " + communityName;
        String body = postContent.length() > 100 ? postContent.substring(0, 100) + "..." : postContent;

        Notification newNotification = new Notification(title, body);

        try {
            this.notificationRepository.save(newNotification);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Notification", e);
        }

        List<DeviceToken> tokens = deviceTokenRepository.findAll();

        for (DeviceToken token : tokens) {
            if (token.getUserId().equals(userId)) {
                continue;
            }

            UserNotification newUserNotification = new UserNotification(token.getUserId(), newNotification);

            try {
                this.userNotificationRepository.save(newUserNotification);
            } catch (Exception e) {
                throw new RuntimeException("Failed to create UserNotification", e);
            }

            cloudMessagingService.sendNotification(token.getToken(), title, body);
        }
    }

    @Override
    public void sendConcertReminder(Long userId, Long concertId, String concertTitle, String venueName, LocalDateTime dateTime) {
        String titlePrefix = "Recordatorio de Concierto: ";
        
        // Check for duplicates
        if (userNotificationRepository.existsByUserIdAndNotification_ConcertIdAndNotification_TitleStartingWith(userId, concertId, titlePrefix)) {
            return;
        }

        String formattedTime = dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        String title = titlePrefix + concertTitle;
        String body = "¡No te pierdas el concierto en " + venueName + " hoy a las " + formattedTime + "!";

        Notification newNotification = new Notification(title, body, concertId);

        try {
            this.notificationRepository.save(newNotification);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Notification", e);
        }

        List<DeviceToken> tokens = deviceTokenRepository.findAllByUserId(userId);

        for (DeviceToken token : tokens) {
            UserNotification newUserNotification = new UserNotification(userId, newNotification);

            try {
                this.userNotificationRepository.save(newUserNotification);
            } catch (Exception e) {
                throw new RuntimeException("Failed to create UserNotification", e);
            }

            cloudMessagingService.sendNotification(token.getToken(), title, body);
        }
    }

    @Override
    public void notifyUserOfPostLike(Long authorId, String likerUsername, Long postId) {
        logger.info("Notifying user {} of like on post {} by {}", authorId, postId, likerUsername);
        String title = "Nueva interacción";
        String body = likerUsername + " le dio like a tu publicación";

        Notification newNotification = new Notification(title, body);

        try {
            this.notificationRepository.save(newNotification);
            logger.info("Saved notification for post like");
        } catch (Exception e) {
            logger.error("Failed to create Notification", e);
            throw new RuntimeException("Failed to create Notification", e);
        }

        List<DeviceToken> tokens = deviceTokenRepository.findAllByUserId(authorId);
        logger.info("Found {} device tokens for user {}", tokens.size(), authorId);

        for (DeviceToken token : tokens) {
            UserNotification newUserNotification = new UserNotification(authorId, newNotification);

            try {
                this.userNotificationRepository.save(newUserNotification);
            } catch (Exception e) {
                logger.error("Failed to create UserNotification", e);
                throw new RuntimeException("Failed to create UserNotification", e);
            }

            cloudMessagingService.sendNotification(token.getToken(), title, body);
            logger.info("Sent FCM notification to token {}", token.getToken());
        }
    }
}
