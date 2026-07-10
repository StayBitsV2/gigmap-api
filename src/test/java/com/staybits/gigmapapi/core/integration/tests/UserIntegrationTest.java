package com.staybits.gigmapapi.core.integration.tests;

import com.staybits.gigmapapi.authentication.domain.model.aggregates.User;
import com.staybits.gigmapapi.authentication.domain.model.valueobjects.Role;
import com.staybits.gigmapapi.authentication.infrastructure.persistence.jpa.repositories.UserRepository;
import com.staybits.gigmapapi.authentication.interfaces.rest.UsersController;
import com.staybits.gigmapapi.authentication.interfaces.rest.resources.UpdateUserResource;
import com.staybits.gigmapapi.authentication.interfaces.rest.resources.UserResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserIntegrationTest {

    @Autowired
    private UsersController usersController;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testGetAllUsers() {
        // Arrange
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        User user = new User("user-" + uniqueSuffix + "@example.com", "username-" + uniqueSuffix, "Name", Role.FAN);
        userRepository.save(user);

        // Act
        ResponseEntity<List<UserResource>> response = usersController.getUsers(null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().size() >= 1);
        assertTrue(response.getBody().stream().anyMatch(u -> u.username().equals("username-" + uniqueSuffix)));
    }

    @Test
    void testGetUserById() {
        // Arrange
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        User user = new User("get-" + uniqueSuffix + "@example.com", "get-" + uniqueSuffix, "Get Name", Role.FAN);
        User savedUser = userRepository.save(user);

        // Act
        ResponseEntity<UserResource> response = usersController.getUserById(savedUser.getId());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(savedUser.getId(), response.getBody().id());
        assertEquals("get-" + uniqueSuffix, response.getBody().username());
    }

    @Test
    void testGetUserByIdNotFound() {
        // Act
        ResponseEntity<UserResource> response = usersController.getUserById(999999L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUpdateUser() {
        // Arrange
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        User user = new User("upd-" + uniqueSuffix + "@example.com", "upd-" + uniqueSuffix, "Old Name", Role.FAN);
        User savedUser = userRepository.save(user);

        UpdateUserResource updateResource = new UpdateUserResource(
                "new-" + uniqueSuffix + "@example.com",
                "new-" + uniqueSuffix,
                "New Name",
                "ARTIST",
                "http://image.url",
                "New description",
                null, null, null, null, null, null
        );

        // Act
        ResponseEntity<UserResource> response = usersController.updateUser(updateResource, savedUser.getId());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("new-" + uniqueSuffix, response.getBody().username());
        assertEquals("ARTIST", response.getBody().role());

        // Verify in DB
        Optional<User> updatedUser = userRepository.findById(savedUser.getId());
        assertTrue(updatedUser.isPresent());
        assertEquals("New Name", updatedUser.get().getName());
        assertEquals(Role.ARTIST, updatedUser.get().getRole());
    }
}
