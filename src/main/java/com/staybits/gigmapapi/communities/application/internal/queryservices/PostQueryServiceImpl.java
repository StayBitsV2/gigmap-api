package com.staybits.gigmapapi.communities.application.internal.queryservices;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.staybits.gigmapapi.communities.domain.model.aggregates.Community;
import com.staybits.gigmapapi.communities.domain.model.aggregates.Post;
import com.staybits.gigmapapi.communities.domain.model.queries.GetAllLikedPostsByUserIdQuery;
import com.staybits.gigmapapi.communities.domain.model.queries.GetPostByIdQuery;
import com.staybits.gigmapapi.communities.domain.model.queries.GetPostsByCommunityIdQuery;
import com.staybits.gigmapapi.communities.domain.model.queries.GetPostsQuery;
import com.staybits.gigmapapi.communities.domain.services.PostQueryService;
import com.staybits.gigmapapi.communities.infrastructure.persistence.jpa.repositories.CommunityRepository;
import com.staybits.gigmapapi.communities.infrastructure.persistence.jpa.repositories.PostRepository;

@Service
public class PostQueryServiceImpl implements PostQueryService {
    private final PostRepository postRepository;
    private final CommunityRepository communityRepository;

    public PostQueryServiceImpl(PostRepository postRepository, CommunityRepository communityRepository) {
        this.postRepository = postRepository;
        this.communityRepository = communityRepository;
    }

    @Override
    public List<Post> handle(GetPostsQuery query) {
        return postRepository.findAll(); 
    }

    @Override
    public Optional<Post> handle(GetPostByIdQuery query) {
        return postRepository.findById(query.id());
    }

    @Override
    public List<Post> handle(GetPostsByCommunityIdQuery query) {
        Community community = communityRepository.findById(query.communityId())
            .orElseThrow(() -> new RuntimeException("Community not found"));

        return postRepository.findAllByCommunity(community);
    }

    @Override
    public List<Post> handle(GetAllLikedPostsByUserIdQuery query) {
        return this.postRepository.findByLikedBy_Id(query.userId());
    }
}
