package com.staybits.gigmapapi.communities.application.internal.commandservices;

import com.staybits.gigmapapi.authentication.domain.model.aggregates.User;
import com.staybits.gigmapapi.authentication.domain.model.valueobjects.Role;
import com.staybits.gigmapapi.authentication.infrastructure.persistence.jpa.repositories.UserRepository;
import com.staybits.gigmapapi.communities.domain.model.aggregates.Community;
import com.staybits.gigmapapi.communities.domain.model.aggregates.Post;
import com.staybits.gigmapapi.communities.domain.model.commands.*;
import com.staybits.gigmapapi.communities.infrastructure.persistence.jpa.repositories.CommunityRepository;
import com.staybits.gigmapapi.communities.infrastructure.persistence.jpa.repositories.PostRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostCommandServiceImplTest {

    @Mock
    CommunityRepository communityRepository;

    @Mock
    PostRepository postRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    PostCommandServiceImpl postCommandServiceImpl;


    @Test
    void Test_CreatePost() {

        Community community = mock(Community.class);

        User user = new User(
                "correo@test.com",
                "usuario1",
                "Roberto",
                Role.FAN
        );

        CreatePostCommand command =
                new CreatePostCommand(
                        "Hola mundo",
                        "img",
                        1L,
                        1L
                );

        when(communityRepository.findById(1L))
                .thenReturn(Optional.of(community));

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(postRepository.save(any(Post.class)))
                .thenAnswer(i -> i.getArgument(0));

        Post result = postCommandServiceImpl.handle(command);

        assertEquals("Hola mundo", result.getContent());
    }


    @Test
    void Test_LikePost() {

        User user = mock(User.class);
        User author = mock(User.class);

        when(user.getId()).thenReturn(1L);
        when(user.getUsername()).thenReturn("usuario1");

        when(author.getId()).thenReturn(2L);

        Post post = mock(Post.class);

        when(post.getLikedBy()).thenReturn(new java.util.ArrayList<>());
        when(post.getUser()).thenReturn(author);

        LikePostCommand command = new LikePostCommand(1L,1L);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(postRepository.findById(1L))
                .thenReturn(Optional.of(post));

        when(user.getLikes())
                .thenReturn(new java.util.ArrayList<>());

        postCommandServiceImpl.handle(command);

        assertTrue(post.getLikedBy().contains(user));
    }


    @Test
    void Test_UnlikePost() {

        User user = mock(User.class);

        when(user.getId()).thenReturn(1L);

        Post post = mock(Post.class);

        java.util.ArrayList<User> likedUsers = new java.util.ArrayList<>();
        likedUsers.add(user);

        java.util.ArrayList<Post> likedPosts = new java.util.ArrayList<>();
        likedPosts.add(post);

        when(post.getLikedBy()).thenReturn(likedUsers);
        when(user.getLikes()).thenReturn(likedPosts);

        UnlikePostCommand command = new UnlikePostCommand(1L,1L);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(postRepository.findById(1L))
                .thenReturn(Optional.of(post));

        postCommandServiceImpl.handle(command);

        assertFalse(post.getLikedBy().contains(user));
    }


    @Test
    void Test_UpdatePost() {

        Community community = mock(Community.class);

        User user = new User(
                "correo@test.com",
                "usuario1",
                "Roberto",
                Role.FAN
        );

        CreatePostCommand createCommand =
                new CreatePostCommand(
                        "Contenido viejo",
                        "img1",
                        1L,
                        1L
                );

        Post post = new Post(createCommand, community, user);

        UpdatePostCommand updateCommand =
                new UpdatePostCommand(
                        1L,
                        "Contenido nuevo",
                        "img2"
                );

        when(postRepository.findById(1L))
                .thenReturn(Optional.of(post));

        when(postRepository.save(any(Post.class)))
                .thenReturn(post);

        Optional<Post> result =
                postCommandServiceImpl.handle(updateCommand);

        assertTrue(result.isPresent());
        assertEquals("Contenido nuevo", result.get().getContent());
    }


    @Test
    void Test_DeletePost() {

        Community community = mock(Community.class);

        User user = new User(
                "correo@test.com",
                "usuario1",
                "Roberto",
                Role.FAN
        );

        CreatePostCommand createCommand =
                new CreatePostCommand(
                        "Contenido",
                        "img",
                        1L,
                        1L
                );

        Post post = new Post(createCommand, community, user);

        DeletePostCommand command =
                new DeletePostCommand(1L);

        when(postRepository.findById(1L))
                .thenReturn(Optional.of(post));

        assertDoesNotThrow(() ->
                postCommandServiceImpl.handle(command)
        );

        verify(postRepository).delete(post);
    }
}