package com.staybits.gigmapapi.communities.application.internal.commandservices;

import org.springframework.stereotype.Service;

import com.staybits.gigmapapi.authentication.infrastructure.persistence.jpa.repositories.UserRepository;
import com.staybits.gigmapapi.communities.domain.model.aggregates.Thread;
import com.staybits.gigmapapi.communities.domain.model.commands.CreateThreadCommand;
import com.staybits.gigmapapi.communities.domain.services.ForumCommandService;
import com.staybits.gigmapapi.communities.infrastructure.persistence.jpa.repositories.CommunityRepository;
import com.staybits.gigmapapi.communities.infrastructure.persistence.jpa.repositories.ThreadRepository;

@Service
public class ForumCommandServiceImpl implements ForumCommandService {
    private final CommunityRepository communityRepository;
    private final ThreadRepository threadRepository;
    private final UserRepository userRepository;

    public ForumCommandServiceImpl(CommunityRepository communityRepository, ThreadRepository threadRepository,
            UserRepository userRepository) {
        this.communityRepository = communityRepository;
        this.threadRepository = threadRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Thread handle(CreateThreadCommand command) {
        var community = communityRepository.findById(command.communityId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Forum with id " + command.communityId() + " does not exist"));
        var user = userRepository.findById(command.userId())
                .orElseThrow(
                        () -> new IllegalArgumentException("User with id " + command.userId() + " does not exist"));

        var thread = new Thread(command.title(), command.content(), command.imageUrl(), community, user);

        try {
            return threadRepository.save(thread);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to create thread: " + e.getMessage(), e);
        }
    }
}
