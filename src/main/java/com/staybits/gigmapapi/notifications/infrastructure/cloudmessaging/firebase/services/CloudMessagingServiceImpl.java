package com.staybits.gigmapapi.notifications.infrastructure.cloudmessaging.firebase.services;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.staybits.gigmapapi.notifications.infrastructure.cloudmessaging.firebase.FirebaseCloudMessagingService;

import org.springframework.stereotype.Service;

@Service
public class CloudMessagingServiceImpl implements FirebaseCloudMessagingService {
    @Override
    public void sendNotification(String token, String title, String body) {
        try {
            Notification notification = Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build();

            Message message = Message.builder()
                    .setToken(token)
                    .setNotification(notification)
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("✅ Notificación enviada con éxito: " + response);
        } catch (Exception e) {
            System.err.println("❌ Error enviando notificación: " + e.getMessage());
        }
    }
}
