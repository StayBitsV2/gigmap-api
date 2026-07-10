package com.staybits.gigmapapi.communities.application.internal.commandservices;

import com.staybits.gigmapapi.authentication.domain.model.aggregates.User;
import com.staybits.gigmapapi.authentication.domain.model.valueobjects.Role;
import com.staybits.gigmapapi.authentication.infrastructure.persistence.jpa.repositories.UserRepository;
import com.staybits.gigmapapi.communities.domain.model.aggregates.Community;
import com.staybits.gigmapapi.communities.domain.model.commands.*;
import com.staybits.gigmapapi.communities.infrastructure.persistence.jpa.repositories.CommunityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CommunityCommandServiceImplTest {
        @Mock
        CommunityRepository communityRepository;
        @Mock
        UserRepository userRepository;
        @InjectMocks
        CommunityCommandServiceImpl communityCommandServiceImpl;
        CreateCommunityCommand command = new CreateCommunityCommand("ComunidadTesting", "img", "La mejor", "ROCK");
        Community community = new Community(command);

        @Test
        void Test_CreateCommunity() {
                when(communityRepository.existsByName("ComunidadTesting")).thenReturn(false);
                when(communityRepository.save(any(Community.class))).thenReturn(community);
                var result = communityCommandServiceImpl.handle(command);
                assertEquals("ComunidadTesting", result.getName());
        }

        @Test
        void Test_UpdateCommunity() {

                Community community = new Community(command);

                UpdateCommunityCommand updateCommand = new UpdateCommunityCommand(
                                1L,
                                "NuevoNombre",
                                "img", "NuevaDescripcion",
                                "ROCK"
                );

                when(communityRepository.findById(1L))
                                .thenReturn(Optional.of(community));

                when(communityRepository.save(any(Community.class)))
                                .thenReturn(community);

                var result = communityCommandServiceImpl.handle(updateCommand);

                assertTrue(result.isPresent());

                assertEquals("NuevoNombre", result.get().getName());

                assertEquals("NuevaDescripcion",
                                result.get().getDescription());
        }

        @Test
        void Test_DeleteCommunity() {

                Community community = new Community(command);

                DeleteCommunityCommand deleteCommand = new DeleteCommunityCommand(1L);

                when(communityRepository.findById(1L))
                                .thenReturn(Optional.of(community));

                assertDoesNotThrow(() -> communityCommandServiceImpl.handle(deleteCommand));
        }

        @Test
        void Test_JoinCommunity() {

                Community community = new Community(command);

                User user = new User(
                                "correo@prueba",
                                "user1",
                                "Roberto",
                                Role.FAN);

                JoinCommunityCommand joinCommand = new JoinCommunityCommand(1L, 1L);

                when(userRepository.findById(1L))
                                .thenReturn(Optional.of(user));

                when(communityRepository.findById(1L))
                                .thenReturn(Optional.of(community));

                when(communityRepository.save(any(Community.class)))
                                .thenReturn(community);

                when(userRepository.save(any(User.class)))
                                .thenReturn(user);

                communityCommandServiceImpl.handle(joinCommand);

                assertTrue(community.getMembers().contains(user));

                assertTrue(user.getCommunitiesJoined().contains(community));
        }

        @Test
        void Test_LeaveCommunity() {

                Community community = new Community(command);
                User user = mock(User.class);

                when(user.getId()).thenReturn(1L);

                LeaveCommunityCommand leaveCommand = new LeaveCommunityCommand(1L, 1L);

                when(user.getCommunitiesJoined()).thenReturn(new ArrayList<>());
                community.getMembers().add(user);
                user.getCommunitiesJoined().add(community);

                when(userRepository.findById(1L)).thenReturn(Optional.of(user));
                when(communityRepository.findById(1L)).thenReturn(Optional.of(community));

                when(communityRepository.save(any(Community.class)))
                                .thenReturn(community);

                when(userRepository.save(any(User.class)))
                                .thenReturn(user);

                communityCommandServiceImpl.handle(leaveCommand);

                assertFalse(community.getMembers().contains(user));
                assertFalse(user.getCommunitiesJoined().contains(community));
        }
}