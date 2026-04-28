package com.staybits.gigmapapi.communities.interfaces.rest.transform;

import java.util.List;

import com.staybits.gigmapapi.authentication.domain.model.aggregates.User;
import com.staybits.gigmapapi.communities.domain.model.aggregates.Community;
import com.staybits.gigmapapi.communities.domain.model.aggregates.Post;
import com.staybits.gigmapapi.communities.interfaces.rest.resources.CommunityResource;

public class CommunityResourceFromEntityAssembler {
    private static List<Long> getPostIds(List<Post> posts) {
        return posts.stream().map(Post::getId).toList();
    }

    private static List<Long> getMemberIds(List<User> members) {
        return members.stream().toList().stream().map(User::getId).toList();
    }

    public static CommunityResource toResourceFromEntity(Community entity) {
        return new CommunityResource(entity.getId(), entity.getName(), entity.getDescription(), entity.getImageUrl(), getPostIds(entity.getPosts()), getMemberIds(entity.getMembers()));
    }
}
