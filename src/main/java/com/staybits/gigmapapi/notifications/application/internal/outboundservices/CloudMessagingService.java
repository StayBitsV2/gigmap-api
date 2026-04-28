package com.staybits.gigmapapi.notifications.application.internal.outboundservices;

public interface CloudMessagingService {
    public void sendNotification(String token, String title, String body);
}
