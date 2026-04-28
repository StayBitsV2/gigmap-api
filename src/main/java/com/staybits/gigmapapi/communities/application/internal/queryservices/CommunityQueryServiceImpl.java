package com.staybits.gigmapapi.communities.application.internal.queryservices;

import org.springframework.stereotype.Service;

import com.staybits.gigmapapi.communities.domain.model.aggregates.Community;
import com.staybits.gigmapapi.communities.domain.model.queries.GetAllCommunitiesJoinedByUserIdQuery;
import com.staybits.gigmapapi.communities.domain.model.queries.GetCommunitiesQuery;
import com.staybits.gigmapapi.communities.domain.model.queries.GetCommunityByIdQuery;
import com.staybits.gigmapapi.communities.domain.services.CommunityQueryService;
import com.staybits.gigmapapi.communities.infrastructure.persistence.jpa.repositories.CommunityRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CommunityQueryServiceImpl implements CommunityQueryService {
    private final CommunityRepository communityRepository;

    public CommunityQueryServiceImpl(CommunityRepository communityRepository) {
        this.communityRepository = communityRepository;
    }

    @Override
    public Optional<Community> handle(GetCommunityByIdQuery query) {
        return communityRepository.findById(query.id());
    }

    @Override
    public List<Community> handle(GetCommunitiesQuery query) {
        return communityRepository.findAll();
    }

    @Override
    public List<Community> handle(GetAllCommunitiesJoinedByUserIdQuery query) {
        return this.communityRepository.findByMembers_Id(query.userId());
    }
}
