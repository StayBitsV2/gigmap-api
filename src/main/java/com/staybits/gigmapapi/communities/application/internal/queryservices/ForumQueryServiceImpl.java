package com.staybits.gigmapapi.communities.application.internal.queryservices;

import org.springframework.stereotype.Service;
import com.staybits.gigmapapi.communities.domain.model.aggregates.Community;
import com.staybits.gigmapapi.communities.domain.model.aggregates.Post;
import com.staybits.gigmapapi.communities.domain.model.queries.*;
import com.staybits.gigmapapi.communities.domain.services.ForumQueryService;
import com.staybits.gigmapapi.communities.infrastructure.persistence.jpa.repositories.CommunityRepository;
import com.staybits.gigmapapi.communities.infrastructure.persistence.jpa.repositories.PostRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ForumQueryServiceImpl implements ForumQueryService {
    private final CommunityRepository communityRepository;
    private final PostRepository postRepository;

    public ForumQueryServiceImpl(CommunityRepository communityRepository, PostRepository postRepository) {
        this.communityRepository = communityRepository;
        this.postRepository = postRepository;
    }

    @Override
    public List<Community> handle(GetForumsQuery query) {
        return communityRepository.findByGenreNotNull();
    }

    @Override
    public List<Community> handle(GetForumByGenreQuery query) {
        return communityRepository.findAll().stream()
                .filter(c -> query.genre().equals(c.getGenre()))
                .toList();
    }

    @Override
    public List<Post> handle(GetThreadsByForumIdQuery query) {
        return postRepository.findAllByCommunityIdOrderByCreatedAtDesc(query.communityId());
    }

    @Override
    public Optional<Post> handle(GetThreadByIdQuery query) {
        return postRepository.findById(query.postId());
    }
}
