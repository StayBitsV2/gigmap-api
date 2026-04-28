package com.staybits.gigmapapi.authentication.interfaces.rest.resources;

public record UserDetailsResource(Long id, String email, String username, String role, String imagenUrl, String descripcion) {
}
