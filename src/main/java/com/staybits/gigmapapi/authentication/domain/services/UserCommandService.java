package com.staybits.gigmapapi.authentication.domain.services;

import com.staybits.gigmapapi.authentication.domain.model.aggregates.User;
import com.staybits.gigmapapi.authentication.domain.model.commands.UpdateUserCommand;

import java.util.Optional;

/**
 * User command service
 * <p>
 *     This interface represents the service to handle user commands.
 *     Authentication (SignIn/SignUp) is now handled by Auth0.
 * </p>
 */
public interface UserCommandService {
    /**
     * Handle update user command
     * @param command the {@link UpdateUserCommand} command
     * @return an {@link Optional} of {@link User} entity
     */
    Optional<User> handle(UpdateUserCommand command);
}
