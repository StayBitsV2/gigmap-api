package com.staybits.gigmapapi.communities.domain.services;

import com.staybits.gigmapapi.communities.domain.model.aggregates.Community;
import com.staybits.gigmapapi.communities.domain.model.aggregates.Post;
import com.staybits.gigmapapi.communities.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface ForumQueryService {
    List<Community> handle(GetForumsQuery query);
    List<Community> handle(GetForumByGenreQuery query);
    List<Post> handle(GetThreadsByForumIdQuery query);
    Optional<Post> handle(GetThreadByIdQuery query);
}
