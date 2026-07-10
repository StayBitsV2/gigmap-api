package com.staybits.gigmapapi.communities.application.internal.queryservices;

import com.staybits.gigmapapi.communities.domain.model.aggregates.Community;
import com.staybits.gigmapapi.communities.domain.model.commands.CreateCommunityCommand;
import com.staybits.gigmapapi.communities.domain.model.queries.GetAllCommunitiesJoinedByUserIdQuery;
import com.staybits.gigmapapi.communities.domain.model.queries.GetCommunitiesQuery;
import com.staybits.gigmapapi.communities.domain.model.queries.GetCommunityByIdQuery;
import com.staybits.gigmapapi.communities.infrastructure.persistence.jpa.repositories.CommunityRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommunityQueryServiceImplTest {

    @Mock
    CommunityRepository communityRepository;

    @InjectMocks
    CommunityQueryServiceImpl communityQueryServiceImpl;

    CreateCommunityCommand command1 =
            new CreateCommunityCommand(
                    "Community 1",
                    "img1",
                    "Descripcion 1",
                    "ROCK"
            );

    CreateCommunityCommand command2 =
            new CreateCommunityCommand(
                    "Community 2",
                    "img2",
                    "Descripcion 2",
                    "ROCK"
            );

    Community community1 = new Community(command1);
    Community community2 = new Community(command2);


    @Test
    void Test_GetCommunityById() {

        GetCommunityByIdQuery query =
                new GetCommunityByIdQuery(1L);

        when(communityRepository.findById(1L))
                .thenReturn(Optional.of(community1));

        Optional<Community> result =
                communityQueryServiceImpl.handle(query);

        assertTrue(result.isPresent());
        assertEquals("Community 1", result.get().getName());
    }


    @Test
    void Test_GetAllCommunities() {

        List<Community> communities =
                List.of(community1, community2);

        GetCommunitiesQuery query =
                new GetCommunitiesQuery();

        when(communityRepository.findAll())
                .thenReturn(communities);

        List<Community> result =
                communityQueryServiceImpl.handle(query);

        assertEquals(2, result.size());
    }


    @Test
    void Test_GetCommunitiesJoinedByUserId() {

        List<Community> communities =
                List.of(community1);

        GetAllCommunitiesJoinedByUserIdQuery query =
                new GetAllCommunitiesJoinedByUserIdQuery(1L);

        when(communityRepository.findByMembers_Id(1L))
                .thenReturn(communities);

        List<Community> result =
                communityQueryServiceImpl.handle(query);

        assertEquals(1, result.size());
        assertEquals("Community 1", result.get(0).getName());
    }
}