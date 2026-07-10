package com.staybits.gigmapapi.core.integration.tests;

import com.staybits.gigmapapi.authentication.domain.model.aggregates.User;
import com.staybits.gigmapapi.authentication.domain.model.valueobjects.Role;
import com.staybits.gigmapapi.authentication.infrastructure.persistence.jpa.repositories.UserRepository;
import com.staybits.gigmapapi.connections.interfaces.rest.ConnectionsController;
import com.staybits.gigmapapi.connections.interfaces.rest.resources.ConnectionRequestResource;
import com.staybits.gigmapapi.connections.interfaces.rest.resources.ConnectionResource;
import com.staybits.gigmapapi.connections.interfaces.rest.resources.CreateConnectionRequestResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ConnectionIntegrationTest {

    @Autowired
    private ConnectionsController connectionsController;

    @Autowired
    private UserRepository userRepository;

    private User createFan(String suffix) {
        User user = new User("fan-" + suffix + "@test.com", "fan-" + suffix, "Fan " + suffix, Role.FAN);
        return userRepository.save(user);
    }

    @Test
    void testCreateAndAcceptConnectionRequest() {
        User fan1 = createFan(UUID.randomUUID().toString().substring(0, 8));
        User fan2 = createFan(UUID.randomUUID().toString().substring(0, 8));

        ResponseEntity<ConnectionRequestResource> createResponse = connectionsController.createRequest(
                fan1.getId(), new CreateConnectionRequestResource(fan2.getId()));

        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());
        assertEquals("PENDING", createResponse.getBody().status());

        Long requestId = createResponse.getBody().id();

        ResponseEntity<ConnectionResource> acceptResponse = connectionsController.acceptRequest(requestId, fan2.getId());

        assertEquals(HttpStatus.OK, acceptResponse.getStatusCode());
        assertNotNull(acceptResponse.getBody());

        ResponseEntity<Boolean> checkResponse = connectionsController.areConnected(fan1.getId(), fan2.getId());
        assertTrue(checkResponse.getBody());
    }

    @Test
    void testRejectConnectionRequest() {
        User fan1 = createFan(UUID.randomUUID().toString().substring(0, 8));
        User fan2 = createFan(UUID.randomUUID().toString().substring(0, 8));

        ResponseEntity<ConnectionRequestResource> createResponse = connectionsController.createRequest(
                fan1.getId(), new CreateConnectionRequestResource(fan2.getId()));
        Long requestId = createResponse.getBody().id();

        ResponseEntity<Void> rejectResponse = connectionsController.rejectRequest(requestId);

        assertEquals(HttpStatus.OK, rejectResponse.getStatusCode());

        ResponseEntity<Boolean> checkResponse = connectionsController.areConnected(fan1.getId(), fan2.getId());
        assertFalse(checkResponse.getBody());
    }

    @Test
    void testCannotRequestSelf() {
        User fan = createFan(UUID.randomUUID().toString().substring(0, 8));

        assertThrows(IllegalArgumentException.class,
                () -> connectionsController.createRequest(fan.getId(), new CreateConnectionRequestResource(fan.getId())));
    }

    @Test
    void testCannotDuplicateRequest() {
        User fan1 = createFan(UUID.randomUUID().toString().substring(0, 8));
        User fan2 = createFan(UUID.randomUUID().toString().substring(0, 8));

        connectionsController.createRequest(fan1.getId(), new CreateConnectionRequestResource(fan2.getId()));

        assertThrows(IllegalArgumentException.class,
                () -> connectionsController.createRequest(fan1.getId(), new CreateConnectionRequestResource(fan2.getId())));
    }

    @Test
    void testCannotRequestIfAlreadyConnected() {
        User fan1 = createFan(UUID.randomUUID().toString().substring(0, 8));
        User fan2 = createFan(UUID.randomUUID().toString().substring(0, 8));

        ResponseEntity<ConnectionRequestResource> createResponse = connectionsController.createRequest(
                fan1.getId(), new CreateConnectionRequestResource(fan2.getId()));
        connectionsController.acceptRequest(createResponse.getBody().id(), fan2.getId());

        assertThrows(IllegalArgumentException.class,
                () -> connectionsController.createRequest(fan1.getId(), new CreateConnectionRequestResource(fan2.getId())));
    }

    @Test
    void testGetUserConnections() {
        User fan1 = createFan(UUID.randomUUID().toString().substring(0, 8));
        User fan2 = createFan(UUID.randomUUID().toString().substring(0, 8));

        ResponseEntity<ConnectionRequestResource> createResponse = connectionsController.createRequest(
                fan1.getId(), new CreateConnectionRequestResource(fan2.getId()));
        connectionsController.acceptRequest(createResponse.getBody().id(), fan2.getId());

        ResponseEntity<List<ConnectionResource>> fan1Connections = connectionsController.getUserConnections(fan1.getId());
        assertEquals(HttpStatus.OK, fan1Connections.getStatusCode());
        assertNotNull(fan1Connections.getBody());
        assertEquals(1, fan1Connections.getBody().size());
        assertEquals(fan2.getId(), fan1Connections.getBody().get(0).connectedUserId());

        ResponseEntity<List<ConnectionResource>> fan2Connections = connectionsController.getUserConnections(fan2.getId());
        assertEquals(HttpStatus.OK, fan2Connections.getStatusCode());
        assertEquals(1, fan2Connections.getBody().size());
        assertEquals(fan1.getId(), fan2Connections.getBody().get(0).connectedUserId());
    }

    @Test
    void testIncomingRequests() {
        User fan1 = createFan(UUID.randomUUID().toString().substring(0, 8));
        User fan2 = createFan(UUID.randomUUID().toString().substring(0, 8));

        connectionsController.createRequest(fan1.getId(), new CreateConnectionRequestResource(fan2.getId()));

        ResponseEntity<List<ConnectionRequestResource>> incoming = connectionsController.getIncomingRequests(fan2.getId());
        assertEquals(HttpStatus.OK, incoming.getStatusCode());
        assertNotNull(incoming.getBody());
        assertEquals(1, incoming.getBody().size());
        assertEquals(fan1.getId(), incoming.getBody().get(0).requesterId());
    }
}
