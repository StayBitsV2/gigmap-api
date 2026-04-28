package com.staybits.gigmapapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.staybits.gigmapapi.authentication.domain.model.valueobjects.Role;
import com.staybits.gigmapapi.authentication.infrastructure.persistence.jpa.repositories.UserRepository;
import com.staybits.gigmapapi.authentication.interfaces.rest.resources.LoginRequest;
import com.staybits.gigmapapi.authentication.interfaces.rest.resources.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.staybits.gigmapapi.authentication.infrastructure.authorization.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;


    private JwtService jwtService; // mock token generation

    @BeforeEach
    void cleanDb() {
        userRepository.deleteAll();
        Mockito.when(jwtService.generateToken(anyLong(), anyString(), anyString()))
                .thenReturn("MOCK_TOKEN_12345");
    }

    @Test
    void register_ShouldReturn201_WhenUserIsValid() throws Exception {
        RegisterRequest request = new RegisterRequest(
                "diegorivas@gmail.com",
                "diegorivas",
                "123457",
                "ARTIST"
        );

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("diegorivas@gmail.com"))
                .andExpect(jsonPath("$.username").value("diegorivas"))
                .andExpect(jsonPath("$.token").value("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaWdtYXAtYXBpIiwic3ViIjoiNCIsImVtYWlsIjoiZGllZ29yaXZhc0BnbWFpbC5jb20iLCJ1c2VybmFtZSI6ImRpZWdvcml2YXMiLCJpYXQiOjE3NjMxMDMwNjksImV4cCI6MTc2MzE4OTQ2OX0.yupAXhtVxesPfFka4_n072CJCTEQacDbdW8N8npTfcQ"));
    }

    @Test
    void register_ShouldReturn400_WhenEmailAlreadyExists() throws Exception {
        // Insert existing user
        var existingUser = new com.staybits.gigmapapi.authentication.domain.model.aggregates.User(
                "diegorivas@gmail.com",
                "diegorivas",
                "Diego Rivas",
                passwordEncoder.encode("123457"),
                Role.ARTIST
        );
        userRepository.save(existingUser);

        RegisterRequest request = new RegisterRequest(
                "test@example.com",
                "testuser",
                "password123",
                "USER"
        );

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_ShouldReturn200_WhenCredentialsAreValid() throws Exception {
        // Create user
        var user = new com.staybits.gigmapapi.authentication.domain.model.aggregates.User(
                "test@example.com",
                "testuser",
                "Test",
                passwordEncoder.encode("password123"),
                Role.ARTIST
        );

        userRepository.save(user);

        LoginRequest login = new LoginRequest("test@example.com", "password123");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.token").value("MOCK_TOKEN_12345"));
    }

    @Test
    void login_ShouldReturn401_WhenPasswordIsInvalid() throws Exception {
        // Create user with real hash
        var user = new com.staybits.gigmapapi.authentication.domain.model.aggregates.User(
                "diegorivas@gmail.com",
                "diegorivas",
                "Diego Rivas",
                passwordEncoder.encode("123457"),
                Role.ARTIST
        );
        userRepository.save(user);

        LoginRequest login = new LoginRequest("test@example.com", "wrongPassword");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_ShouldReturn401_WhenUserDoesNotExist() throws Exception {
        LoginRequest login = new LoginRequest("unknown@example.com", "password");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isUnauthorized());
    }
}
