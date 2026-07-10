package com.staybits.gigmapapi.communities.application.internal.commandservices;

import org.springframework.stereotype.Service;

import com.staybits.gigmapapi.authentication.infrastructure.persistence.jpa.repositories.UserRepository;
import com.staybits.gigmapapi.communities.domain.model.aggregates.Post;
import com.staybits.gigmapapi.communities.domain.model.commands.CreatePostCommand;
import com.staybits.gigmapapi.communities.domain.model.commands.CreateThreadCommand;
import com.staybits.gigmapapi.communities.domain.services.ForumCommandService;
import com.staybits.gigmapapi.communities.infrastructure.persistence.jpa.repositories.CommunityRepository;
import com.staybits.gigmapapi.communities.infrastructure.persistence.jpa.repositories.PostRepository;

@Service
public class ForumCommandServiceImpl implements ForumCommandService {
    private final CommunityRepository communityRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public ForumCommandServiceImpl(CommunityRepository communityRepository, PostRepository postRepository,
            UserRepository userRepository) {
        this.communityRepository = communityRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Post handle(CreateThreadCommand command) {
        var community = communityRepository.findById(command.communityId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Forum with id " + command.communityId() + " does not exist"));
        var user = userRepository.findById(command.userId())
                .orElseThrow(
                        () -> new IllegalArgumentException("User with id " + command.userId() + " does not exist"));

        var createPostCommand = new CreatePostCommand(command.content(), command.imageUrl(), command.communityId(),
                command.userId(), command.title());
        var post = new Post(createPostCommand, community, user);

        try {
            return postRepository.save(post);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to create thread: " + e.getMessage(), e);
        }
    }
}
