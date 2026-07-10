package com.staybits.gigmapapi.communities.application.internal.commandservices;

import org.springframework.stereotype.Service;
import com.staybits.gigmapapi.authentication.infrastructure.persistence.jpa.repositories.UserRepository;
import com.staybits.gigmapapi.communities.domain.model.commands.AddReactionCommand;
import com.staybits.gigmapapi.communities.domain.model.commands.RemoveReactionCommand;
import com.staybits.gigmapapi.communities.domain.model.entities.Reaction;
import com.staybits.gigmapapi.communities.domain.services.ReactionCommandService;
import com.staybits.gigmapapi.communities.infrastructure.persistence.jpa.repositories.ReactionRepository;

@Service
public class ReactionCommandServiceImpl implements ReactionCommandService {
    private final UserRepository userRepository;
    private final ReactionRepository reactionRepository;

    public ReactionCommandServiceImpl(UserRepository userRepository, ReactionRepository reactionRepository) {
        this.userRepository = userRepository;
        this.reactionRepository = reactionRepository;
    }

    @Override
    public Reaction handle(AddReactionCommand command) {
        var user = userRepository.findById(command.userId())
                .orElseThrow(
                        () -> new IllegalArgumentException("User with id " + command.userId() + " does not exist"));

        var reaction = new Reaction(command.emoji(), user, command.threadId(), command.commentId());

        try {
            return reactionRepository.save(reaction);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to add reaction: " + e.getMessage(), e);
        }
    }

    @Override
    public void handle(RemoveReactionCommand command) {
        if (!reactionRepository.existsById(command.reactionId()))
            throw new IllegalArgumentException("Reaction with id " + command.reactionId() + " does not exist");
        reactionRepository.deleteById(command.reactionId());
    }
}
