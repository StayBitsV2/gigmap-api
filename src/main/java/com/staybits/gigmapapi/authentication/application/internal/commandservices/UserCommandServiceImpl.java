package com.staybits.gigmapapi.authentication.application.internal.commandservices;

import com.staybits.gigmapapi.authentication.domain.model.aggregates.User;
import com.staybits.gigmapapi.authentication.domain.model.commands.UpdateUserCommand;
import com.staybits.gigmapapi.authentication.domain.services.UserCommandService;
import com.staybits.gigmapapi.authentication.infrastructure.persistence.jpa.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * User command service implementation.
 * <p>
 * Authentication (SignIn/SignUp) is now handled by Auth0.
 * This service only handles user profile updates.
 * </p>
 */
@Service
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;

    public UserCommandServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> handle(UpdateUserCommand command) {
        var result = userRepository.findById(command.userId());
        if (result.isEmpty())
            throw new IllegalArgumentException("User with id %s not found".formatted(command.userId()));
        var userToUpdate = result.get();
        try {
            var updatedUser = userRepository.save(userToUpdate.updateInformation(
                command.email(),
        command.username(),
        command.name(),
        command.role(),
        command.imagenUrl(),
        command.descripcion()
      
            ));
            return Optional.of(updatedUser);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while updating user: %s".formatted(e.getMessage()));
        }
    }
}
