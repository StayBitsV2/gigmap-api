package com.staybits.gigmapapi.authentication.interfaces.rest.resources;

public record UserDetailsResource(Long id, String email, String username, String role, String imagenUrl, String descripcion, String bannerUrl, String generoMusical, String sitioWeb, String spotifyUrl, String instagramUrl, String youtubeUrl) {
}
