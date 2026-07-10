package com.staybits.gigmapapi.communities.interfaces.rest.transform;

import com.staybits.gigmapapi.communities.domain.model.entities.Comment;
import com.staybits.gigmapapi.communities.interfaces.rest.resources.CommentResource;

public class CommentResourceFromEntityAssembler {
    public static CommentResource toResourceFromEntity(Comment comment) {
        return new CommentResource(
            comment.getId(),
            comment.getThread().getId(),
            comment.getUser().getId(),
            comment.getUser().getUsername(),
            comment.getContent(),
            comment.getCreatedAt() != null ? comment.getCreatedAt().toString() : null
        );
    }
}
