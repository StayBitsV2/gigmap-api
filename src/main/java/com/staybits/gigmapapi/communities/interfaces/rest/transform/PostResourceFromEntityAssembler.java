package com.staybits.gigmapapi.communities.interfaces.rest.transform;

import java.util.List;

import com.staybits.gigmapapi.authentication.domain.model.aggregates.User;
import com.staybits.gigmapapi.communities.domain.model.aggregates.Post;
import com.staybits.gigmapapi.communities.interfaces.rest.resources.PostResource;

public class PostResourceFromEntityAssembler {
    private static List<Long> getLikes(List<User> likes) {
        return likes.stream().map(User::getId).toList();
    }

    public static PostResource toResourceFromEntity(Post post) {
        return new PostResource(post.getId(), post.getCommunity().getId(), post.getUser().getId(), post.getContent(), post.getImageUrl(), getLikes(post.getLikedBy()));
    }
}
