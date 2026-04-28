package com.staybits.gigmapapi.communities.domain.services;

import java.util.Optional;

import com.staybits.gigmapapi.communities.domain.model.aggregates.Post;
import com.staybits.gigmapapi.communities.domain.model.commands.CreatePostCommand;
import com.staybits.gigmapapi.communities.domain.model.commands.DeletePostCommand;
import com.staybits.gigmapapi.communities.domain.model.commands.LikePostCommand;
import com.staybits.gigmapapi.communities.domain.model.commands.UnlikePostCommand;
import com.staybits.gigmapapi.communities.domain.model.commands.UpdatePostCommand;


public interface PostCommandService {
    Post handle(CreatePostCommand command);

    Optional<Post> handle(UpdatePostCommand command);

    void handle(DeletePostCommand command);

    void handle(LikePostCommand command);

    void handle(UnlikePostCommand command);
}
