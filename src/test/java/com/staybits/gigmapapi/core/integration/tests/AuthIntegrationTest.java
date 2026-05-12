package com.staybits.gigmapapi.core.integration.tests;

import com.staybits.gigmapapi.authentication.domain.model.aggregates.User;
import com.staybits.gigmapapi.authentication.infrastructure.persistence.jpa.repositories.UserRepository;
import com.staybits.gigmapapi.authentication.interfaces.rest.AuthController;
import com.staybits.gigmapapi.authentication.interfaces.rest.resources.AuthResponse;
import com.staybits.gigmapapi.authentication.interfaces.rest.resources.LoginRequest;
import com.staybits.gigmapapi.authentication.interfaces.rest.resources.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AuthIntegrationTest {

    @Autowired
    private AuthController authController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    void testRegisterWhenDataIsValid() {
        // Arrange
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        String email = "test-" + uniqueSuffix + "@example.com";
        String username = "user-" + uniqueSuffix;
        String rawPassword = "password123";
        RegisterRequest request = new RegisterRequest(email, username, rawPassword, "FAN");

        // Act
        ResponseEntity<?> response = authController.register(request);

        // Assert Controller Response
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Assert Database Persistence
        Optional<User> persistedUser = userRepository.findByEmail(email);
        assertTrue(persistedUser.isPresent(), "User should be persisted in DB");
        assertEquals(username, persistedUser.get().getUsername());

        // Assert Password Encryption
        assertTrue(passwordEncoder.matches(rawPassword, persistedUser.get().getPasswordHash()),
                "Password should be correctly hashed in DB");
    }

    @Test
    void testLoginWhenUserExistsInDb() {
        // Arrange
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        String email = "login-" + uniqueSuffix + "@example.com";
        String password = "password123";
        RegisterRequest regRequest = new RegisterRequest(email, "user-" + uniqueSuffix, password, "FAN");
        authController.register(regRequest);

        // Act
        LoginRequest loginRequest = new LoginRequest(email, password);
        ResponseEntity<?> response = authController.login(loginRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof AuthResponse);
        AuthResponse body = (AuthResponse) response.getBody();
        assertNotNull(body.token());

        // Double check DB state
        assertTrue(userRepository.existsByEmail(email));
    }

    @Test
    void testLoginWhenCredentialsMismatchWithDb() {
        // Arrange
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        String email = "fail-" + uniqueSuffix + "@example.com";
        RegisterRequest regRequest = new RegisterRequest(email, "user-" + uniqueSuffix, "correctPass", "FAN");
        authController.register(regRequest);

        // Act
        LoginRequest loginRequest = new LoginRequest(email, "wrongPass");
        ResponseEntity<?> response = authController.login(loginRequest);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
