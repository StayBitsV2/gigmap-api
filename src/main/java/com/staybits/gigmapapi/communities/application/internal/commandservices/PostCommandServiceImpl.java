package com.staybits.gigmapapi.communities.application.internal.commandservices;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.staybits.gigmapapi.authentication.domain.model.aggregates.User;
import com.staybits.gigmapapi.authentication.infrastructure.persistence.jpa.repositories.UserRepository;
import com.staybits.gigmapapi.communities.domain.model.aggregates.Post;
import com.staybits.gigmapapi.communities.domain.model.commands.CreatePostCommand;
import com.staybits.gigmapapi.communities.domain.model.commands.DeletePostCommand;
import com.staybits.gigmapapi.communities.domain.model.commands.LikePostCommand;
import com.staybits.gigmapapi.communities.domain.model.commands.UnlikePostCommand;
import com.staybits.gigmapapi.communities.domain.model.commands.UpdatePostCommand;
import com.staybits.gigmapapi.communities.domain.services.PostCommandService;
import com.staybits.gigmapapi.communities.infrastructure.persistence.jpa.repositories.CommunityRepository;
import com.staybits.gigmapapi.communities.infrastructure.persistence.jpa.repositories.PostRepository;

@Service
public class PostCommandServiceImpl implements PostCommandService {
    private final CommunityRepository communityRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostCommandServiceImpl(CommunityRepository communityRepository, PostRepository postRepository,
            UserRepository userRepository) {
        this.communityRepository = communityRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Post handle(CreatePostCommand command) {
        var community = communityRepository.findById(command.communityId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Community with id " + command.communityId() + " does not exist"));

        var user = userRepository.findById(command.userId())
                .orElseThrow(
                        () -> new IllegalArgumentException("User with id " + command.userId() + " does not exist"));

        var post = new Post(command, community, user);

        try {
            return postRepository.save(post);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to create post: " + e.getMessage(), e);
        }
    }

    @Override
    public void handle(LikePostCommand command) {
        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = postRepository.findById(command.postId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        boolean alreadyLiked = post.getLikedBy()
                .stream()
                .anyMatch(u -> u.getId().equals(user.getId()));

        if (alreadyLiked) {
            throw new IllegalArgumentException("User has already liked this post");
        }

        post.getLikedBy().add(user);
        user.getLikes().add(post);

        // Notify author if they are not the one liking the post
        if (!post.getUser().getId().equals(user.getId())) {
            post.registerLikedEvent(user.getUsername());
        }

        try {
            postRepository.saveAndFlush(post);
            userRepository.save(user);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to like post: " + e.getMessage());
        }
    }

    @Override
    public void handle(UnlikePostCommand command) {
        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = postRepository.findById(command.postId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        boolean hasLiked = post.getLikedBy()
                .stream()
                .anyMatch(u -> u.getId().equals(user.getId()));

        if (!hasLiked) {
            throw new IllegalArgumentException("User has not liked this post");
        }

        post.getLikedBy().remove(user);
        user.getLikes().remove(post);

        try {
            postRepository.saveAndFlush(post);
            userRepository.save(user);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to unlike post: " + e.getMessage());
        }
    }

    @Override
    public Optional<Post> handle(UpdatePostCommand command) {
        var existingPost = postRepository.findById(command.id());
        if (existingPost.isEmpty())
            return Optional.empty();

        var post = existingPost.get();

        post.setContent(command.content());
        post.setImageUrl(command.imageUrl());
        post.setUpdatedAt(LocalDateTime.now());

        var updatedPost = postRepository.save(post);
        return Optional.of(updatedPost);
    }

    @Override
    public void handle(DeletePostCommand command) {
        var existingPost = postRepository.findById(command.id());

        if (existingPost.isEmpty())
            throw new IllegalArgumentException("Failed to delete community ");

        postRepository.delete(existingPost.get());
    }
}
