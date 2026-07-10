package com.staybits.gigmapapi.communities.interfaces.rest.transform;

import com.staybits.gigmapapi.communities.domain.model.aggregates.Community;
import com.staybits.gigmapapi.communities.interfaces.rest.resources.ForumResource;

public class ForumResourceFromEntityAssembler {
    public static ForumResource toResourceFromEntity(Community entity) {
        return new ForumResource(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getImageUrl(),
            entity.getGenre(),
            entity.getPosts().stream().map(p -> p.getId()).toList(),
            entity.getMembers().stream().map(m -> m.getId()).toList()
        );
    }
}
