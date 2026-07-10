package com.staybits.gigmapapi.communities.application.internal.queryservices;

import com.staybits.gigmapapi.authentication.domain.model.aggregates.User;
import com.staybits.gigmapapi.authentication.domain.model.valueobjects.Role;
import com.staybits.gigmapapi.communities.domain.model.aggregates.Community;
import com.staybits.gigmapapi.communities.domain.model.aggregates.Post;
import com.staybits.gigmapapi.communities.domain.model.commands.CreateCommunityCommand;
import com.staybits.gigmapapi.communities.domain.model.commands.CreatePostCommand;
import com.staybits.gigmapapi.communities.domain.model.queries.GetAllLikedPostsByUserIdQuery;
import com.staybits.gigmapapi.communities.domain.model.queries.GetPostByIdQuery;
import com.staybits.gigmapapi.communities.domain.model.queries.GetPostsByCommunityIdQuery;
import com.staybits.gigmapapi.communities.domain.model.queries.GetPostsQuery;
import com.staybits.gigmapapi.communities.infrastructure.persistence.jpa.repositories.CommunityRepository;
import com.staybits.gigmapapi.communities.infrastructure.persistence.jpa.repositories.PostRepository;

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
class PostQueryServiceImplTest {

    @Mock
    PostRepository postRepository;

    @Mock
    CommunityRepository communityRepository;

    @InjectMocks
    PostQueryServiceImpl postQueryServiceImpl;

    CreateCommunityCommand communityCommand =
            new CreateCommunityCommand(
                    "Community Test",
                    "img",
                    "Descripcion",
                    "ROCK"
            );

    Community community = new Community(communityCommand);

    User user = new User(
            "correo@test.com",
            "usuario1",
            "Roberto",
            Role.FAN
    );

    CreatePostCommand createPostCommand =
            new CreatePostCommand(
                    "Contenido post",
                    "imgpost",
                    1L,
                    1L,
                    "Title"
            );

    Post post = new Post(createPostCommand, community, user);


    @Test
    void Test_GetAllPosts() {

        List<Post> posts = List.of(post);

        GetPostsQuery query = new GetPostsQuery();

        when(postRepository.findAll())
                .thenReturn(posts);

        List<Post> result =
                postQueryServiceImpl.handle(query);

        assertEquals(1, result.size());
    }


    @Test
    void Test_GetPostById() {

        GetPostByIdQuery query =
                new GetPostByIdQuery(1L);

        when(postRepository.findById(1L))
                .thenReturn(Optional.of(post));

        Optional<Post> result =
                postQueryServiceImpl.handle(query);

        assertTrue(result.isPresent());
        assertEquals("Contenido post", result.get().getContent());
    }


    @Test
    void Test_GetPostsByCommunityId() {

        List<Post> posts = List.of(post);

        GetPostsByCommunityIdQuery query =
                new GetPostsByCommunityIdQuery(1L);

        when(communityRepository.findById(1L))
                .thenReturn(Optional.of(community));

        when(postRepository.findAllByCommunity(community))
                .thenReturn(posts);

        List<Post> result =
                postQueryServiceImpl.handle(query);

        assertEquals(1, result.size());
    }


    @Test
    void Test_GetLikedPostsByUserId() {

        List<Post> likedPosts = List.of(post);

        GetAllLikedPostsByUserIdQuery query =
                new GetAllLikedPostsByUserIdQuery(1L);

        when(postRepository.findByLikedBy_Id(1L))
                .thenReturn(likedPosts);

        List<Post> result =
                postQueryServiceImpl.handle(query);

        assertEquals(1, result.size());
        assertEquals("Contenido post", result.get(0).getContent());
    }
}