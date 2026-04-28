package com.staybits.gigmapapi.authentication.interfaces.rest.resources;

public record AuthResponse(
    Long id,
    String email,
    String username,
    String name,
    String role,
    String token,
    String message
) {}
