package com.staybits.gigmapapi.authentication.interfaces.rest.resources;

public record UpdateUserResource(String email, String username, String name, String role, String imagenUrl, String descripcion ) {
}
