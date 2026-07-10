package com.staybits.gigmapapi.communities.interfaces.rest.transform;

import com.staybits.gigmapapi.communities.domain.model.aggregates.Post;
import com.staybits.gigmapapi.communities.interfaces.rest.resources.ThreadResource;

public class ThreadResourceFromEntityAssembler {
    public static ThreadResource toResourceFromEntity(Post post) {
        return new ThreadResource(
            post.getId(),
            post.getCommunity().getId(),
            post.getUser().getId(),
            post.getTitle(),
            post.getContent(),
            post.getImageUrl(),
            post.getLikedBy().stream().map(u -> u.getId()).toList(),
            0
        );
    }
}
