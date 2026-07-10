package com.staybits.gigmapapi.communities.application.internal.commandservices;

import org.springframework.stereotype.Service;
import com.staybits.gigmapapi.authentication.infrastructure.persistence.jpa.repositories.UserRepository;
import com.staybits.gigmapapi.communities.domain.model.commands.AddCommentCommand;
import com.staybits.gigmapapi.communities.domain.model.entities.Comment;
import com.staybits.gigmapapi.communities.domain.services.CommentCommandService;
import com.staybits.gigmapapi.communities.infrastructure.persistence.jpa.repositories.CommentRepository;
import com.staybits.gigmapapi.communities.infrastructure.persistence.jpa.repositories.PostRepository;

@Service
public class CommentCommandServiceImpl implements CommentCommandService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public CommentCommandServiceImpl(PostRepository postRepository, UserRepository userRepository,
            CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment handle(AddCommentCommand command) {
        var thread = postRepository.findById(command.threadId())
                .orElseThrow(
                        () -> new IllegalArgumentException("Thread with id " + command.threadId() + " does not exist"));
        var user = userRepository.findById(command.userId())
                .orElseThrow(
                        () -> new IllegalArgumentException("User with id " + command.userId() + " does not exist"));

        var comment = new Comment(command.content(), user, thread);

        try {
            return commentRepository.save(comment);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to create comment: " + e.getMessage(), e);
        }
    }
}
