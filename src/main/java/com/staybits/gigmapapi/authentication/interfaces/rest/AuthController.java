package com.staybits.gigmapapi.authentication.interfaces.rest;

import com.staybits.gigmapapi.authentication.domain.model.aggregates.User;
import com.staybits.gigmapapi.authentication.infrastructure.authorization.jwt.JwtService;
import com.staybits.gigmapapi.authentication.infrastructure.persistence.jpa.repositories.UserRepository;
import com.staybits.gigmapapi.authentication.interfaces.rest.resources.AuthResponse;
import com.staybits.gigmapapi.authentication.interfaces.rest.resources.LoginRequest;
import com.staybits.gigmapapi.authentication.interfaces.rest.resources.RegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication Controller
 * Public endpoints for user registration and login
 */
@RestController
@RequestMapping(value = "/api/v1/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Authentication", description = "Register and Login endpoints")
public class AuthController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register new user", description = "Create a new user account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input or user already exists")
    })
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        // Check if user already exists
        if (userRepository.existsByEmail(request.email())) {
            System.out.println("❌ Register failed: email already exists - " + request.email());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("Email already registered"));
        }
        
        if (userRepository.existsByUsername(request.username())) {
            System.out.println("❌ Register failed: username already taken - " + request.username());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("Username already taken"));
        }
        
        // Create new user - name can be null
        String hashedPassword = passwordEncoder.encode(request.password());
        User user = new User(request.email(), request.username(), null, hashedPassword, com.staybits.gigmapapi.authentication.domain.model.valueobjects.Role.valueOf(request.role()));
        
        User savedUser = userRepository.save(user);
        
        // Generate JWT token
        String token = jwtService.generateToken(
            savedUser.getId(),
            savedUser.getEmail(),
            savedUser.getUsername()
        );
        
        AuthResponse response = new AuthResponse(
            savedUser.getId(),
            savedUser.getEmail(),
            savedUser.getUsername(),
            savedUser.getName() != null ? savedUser.getName() : "",
            savedUser.getRole().name(),
            token,
            "User registered successfully"
        );
        
        System.out.println("✅ User registered: " + savedUser.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticate user with email/username and password")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        // Find user by email or username
        User user = userRepository.findByEmail(request.emailOrUsername())
            .orElseGet(() -> userRepository.findByUsername(request.emailOrUsername())
                .orElse(null));
        
        if (user == null) {
            System.out.println("❌ Login failed: user not found - " + request.emailOrUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Invalid credentials"));
        }
        
        // Verify password
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            System.out.println("❌ Login failed: invalid password for " + user.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Invalid credentials"));
        }
        
        // Generate JWT token
        String token = jwtService.generateToken(
            user.getId(),
            user.getEmail(),
            user.getUsername()
        );
        
        AuthResponse response = new AuthResponse(
            user.getId(),
            user.getEmail(),
            user.getUsername(),
            user.getName() != null ? user.getName() : "",
            user.getRole().name(),
            token,
            "Login successful"
        );
        
        System.out.println("✅ User logged in: " + user.getEmail());
        return ResponseEntity.ok(response);
    }

    private record ErrorResponse(String error) {}
}
