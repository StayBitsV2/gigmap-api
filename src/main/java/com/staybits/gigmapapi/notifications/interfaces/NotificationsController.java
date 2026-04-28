package com.staybits.gigmapapi.notifications.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import com.staybits.gigmapapi.notifications.application.internal.outboundservices.CloudMessagingService;
import com.staybits.gigmapapi.notifications.domain.model.queries.GetAllNotificationsByUserIdQuery;
import com.staybits.gigmapapi.notifications.domain.services.NotificationQueryService;
import com.staybits.gigmapapi.notifications.interfaces.resources.NotificationResource;
import com.staybits.gigmapapi.notifications.interfaces.transformers.NotificationResourceFromEntityAssembler;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping(value = "/api/v1/notifications", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Notifications", description = "Operations related to Notifications")
public class NotificationsController {
    private final CloudMessagingService cloudMessagingService;
    private final NotificationQueryService notificationQueryService;

    public NotificationsController(CloudMessagingService cloudMessagingService,
            NotificationQueryService notificationQueryService) {
        this.cloudMessagingService = cloudMessagingService;
        this.notificationQueryService = notificationQueryService;
    }

    @PostMapping
    @Operation(summary = "Create a new Notification", description = "Create a new Notification", responses = {
            @ApiResponse(responseCode = "201", description = "Notification created successfully")
    })
    public ResponseEntity<String> sendNotification(@RequestParam String token,
            @RequestParam String title,
            @RequestParam String body) {
        cloudMessagingService.sendNotification(token, title, body);
        return new ResponseEntity<>("Notification created!", HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get all notifications by user", description = "Retrieve all notifications by user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notifications found"),
            @ApiResponse(responseCode = "404", description = "No notifications found")
    })
    public ResponseEntity<List<NotificationResource>> getAllNotificationsByUserId(@PathVariable Long userId) {
        var notifications = this.notificationQueryService.handle(new GetAllNotificationsByUserIdQuery(userId));

        if (notifications.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var notificationsResources = notifications
                .stream()
                .map(NotificationResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(notificationsResources);
    }
}