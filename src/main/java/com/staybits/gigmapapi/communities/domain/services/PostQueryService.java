package com.staybits.gigmapapi.communities.domain.services;

import java.util.List;
import java.util.Optional;

import com.staybits.gigmapapi.communities.domain.model.aggregates.Post;
import com.staybits.gigmapapi.communities.domain.model.queries.GetAllLikedPostsByUserIdQuery;
import com.staybits.gigmapapi.communities.domain.model.queries.GetPostByIdQuery;
import com.staybits.gigmapapi.communities.domain.model.queries.GetPostsByCommunityIdQuery;
import com.staybits.gigmapapi.communities.domain.model.queries.GetPostsQuery;

public interface PostQueryService {
    List<Post> handle(GetPostsQuery query);

    List<Post> handle(GetPostsByCommunityIdQuery query);

    Optional<Post> handle(GetPostByIdQuery query);

    List<Post> handle(GetAllLikedPostsByUserIdQuery query);
}
