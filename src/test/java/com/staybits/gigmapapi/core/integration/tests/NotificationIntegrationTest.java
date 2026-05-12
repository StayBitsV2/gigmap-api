package com.staybits.gigmapapi.core.integration.tests;

import com.staybits.gigmapapi.authentication.domain.model.aggregates.User;
import com.staybits.gigmapapi.authentication.domain.model.valueobjects.Role;
import com.staybits.gigmapapi.authentication.infrastructure.persistence.jpa.repositories.UserRepository;
import com.staybits.gigmapapi.notifications.application.internal.outboundservices.CloudMessagingService;
import com.staybits.gigmapapi.notifications.domain.model.aggregates.DeviceToken;
import com.staybits.gigmapapi.notifications.infrastructure.persistence.jpa.repositories.DeviceTokenRepository;
import com.staybits.gigmapapi.notifications.interfaces.DeviceTokensController;
import com.staybits.gigmapapi.notifications.interfaces.NotificationsController;
import com.staybits.gigmapapi.notifications.interfaces.resources.CreateDeviceTokenResource;
import com.staybits.gigmapapi.notifications.interfaces.resources.DeviceTokenResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
@Transactional
class NotificationIntegrationTest {

    @Autowired
    private DeviceTokensController deviceTokensController;

    @Autowired
    private NotificationsController notificationsController;

    @Autowired
    private DeviceTokenRepository deviceTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @SuppressWarnings("removal")
    @MockBean
    private CloudMessagingService cloudMessagingService;

    @Test
    void testCreateDeviceToken() {
        // Arrange
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        User user = userRepository
                .save(new User("user-" + uniqueSuffix + "@example.com", "user-" + uniqueSuffix, "Name", Role.FAN));
        String token = "token-" + uniqueSuffix;
        CreateDeviceTokenResource resource = new CreateDeviceTokenResource(user.getId(), token);

        // Act
        ResponseEntity<DeviceTokenResource> response = deviceTokensController.createDeviceToken(resource);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(token, response.getBody().token());

        // Verify in DB
        Optional<DeviceToken> deviceToken = deviceTokenRepository.findByToken(response.getBody().token());
        assertTrue(deviceToken.isPresent());
        assertEquals(token, deviceToken.get().getToken());
    }

    @Test
    void testSendNotification() {
        // Arrange
        doNothing().when(cloudMessagingService).sendNotification(anyString(), anyString(), anyString());

        // Act
        ResponseEntity<String> response = notificationsController.sendNotification("test-token", "Title", "Body");

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Notification created!", response.getBody());
    }
}
