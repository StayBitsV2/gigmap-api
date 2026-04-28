package com.staybits.gigmapapi.authentication.domain.model.commands;

import com.staybits.gigmapapi.authentication.domain.model.valueobjects.Role;

public record UpdateUserCommand(Long userId, String email, String username, String name, Role role, String imagenUrl, String descripcion) {
}
