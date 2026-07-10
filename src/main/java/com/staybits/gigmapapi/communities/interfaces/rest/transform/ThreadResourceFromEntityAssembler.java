package com.staybits.gigmapapi.communities.interfaces.rest.transform;

import java.util.List;

import com.staybits.gigmapapi.communities.domain.model.aggregates.Thread;
import com.staybits.gigmapapi.communities.interfaces.rest.resources.ThreadResource;

public class ThreadResourceFromEntityAssembler {
    public static ThreadResource toResourceFromEntity(Thread thread) {
        return new ThreadResource(
            thread.getId(),
            thread.getCommunity().getId(),
            thread.getUser().getId(),
            thread.getTitle(),
            thread.getContent(),
            thread.getImageUrl(),
            List.of(),
            0
        );
    }
}
