package com.staybits.gigmapapi.communities.application.internal.queryservices;

import org.springframework.stereotype.Service;
import com.staybits.gigmapapi.communities.domain.model.aggregates.Community;
import com.staybits.gigmapapi.communities.domain.model.aggregates.Thread;
import com.staybits.gigmapapi.communities.domain.model.queries.*;
import com.staybits.gigmapapi.communities.domain.services.ForumQueryService;
import com.staybits.gigmapapi.communities.infrastructure.persistence.jpa.repositories.CommunityRepository;
import com.staybits.gigmapapi.communities.infrastructure.persistence.jpa.repositories.ThreadRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ForumQueryServiceImpl implements ForumQueryService {
    private final CommunityRepository communityRepository;
    private final ThreadRepository threadRepository;

    public ForumQueryServiceImpl(CommunityRepository communityRepository, ThreadRepository threadRepository) {
        this.communityRepository = communityRepository;
        this.threadRepository = threadRepository;
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
    public List<Thread> handle(GetThreadsByForumIdQuery query) {
        return threadRepository.findAllByCommunityIdOrderByCreatedAtDesc(query.communityId());
    }

    @Override
    public Optional<Thread> handle(GetThreadByIdQuery query) {
        return threadRepository.findById(query.threadId());
    }
}
