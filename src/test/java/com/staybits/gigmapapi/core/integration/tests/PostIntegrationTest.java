package com.staybits.gigmapapi.core.integration.tests;

import com.staybits.gigmapapi.authentication.domain.model.aggregates.User;
import com.staybits.gigmapapi.authentication.domain.model.valueobjects.Role;
import com.staybits.gigmapapi.authentication.infrastructure.persistence.jpa.repositories.UserRepository;
import com.staybits.gigmapapi.communities.domain.model.aggregates.Community;
import com.staybits.gigmapapi.communities.domain.model.aggregates.Post;
import com.staybits.gigmapapi.communities.domain.model.commands.CreateCommunityCommand;
import com.staybits.gigmapapi.communities.domain.model.commands.CreatePostCommand;
import com.staybits.gigmapapi.communities.infrastructure.persistence.jpa.repositories.CommunityRepository;
import com.staybits.gigmapapi.communities.infrastructure.persistence.jpa.repositories.PostRepository;
import com.staybits.gigmapapi.communities.interfaces.rest.PostsController;
import com.staybits.gigmapapi.communities.interfaces.rest.resources.CreatePostResource;
import com.staybits.gigmapapi.communities.interfaces.rest.resources.PostResource;
import com.staybits.gigmapapi.communities.interfaces.rest.resources.UpdatePostResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostIntegrationTest {

    @Autowired
    private PostsController postsController;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testCreatePost() {
        // Arrange
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        Community community = communityRepository.save(new Community(new CreateCommunityCommand("Comm-" + uniqueSuffix, "Desc", "Img")));
        User user = userRepository.save(new User("user-" + uniqueSuffix + "@example.com", "user-" + uniqueSuffix, "Name", Role.FAN));

        CreatePostResource resource = new CreatePostResource("Content", "http://image.url", community.getId(), user.getId());

        // Act
        ResponseEntity<PostResource> response = postsController.createPost(resource);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Content", response.getBody().content());

        // Verify in DB
        Optional<Post> post = postRepository.findById(response.getBody().id());
        assertTrue(post.isPresent());
        assertEquals("Content", post.get().getContent());
    }

    @Test
    void testGetAllPosts() {
        // Arrange
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        Community community = communityRepository.save(new Community(new CreateCommunityCommand("Comm-" + uniqueSuffix, "Desc", "Img")));
        User user = userRepository.save(new User("user-" + uniqueSuffix + "@example.com", "user-" + uniqueSuffix, "Name", Role.FAN));
        postRepository.save(new Post(new CreatePostCommand("Post 1", "Img", community.getId(), user.getId()), community, user));

        // Act
        ResponseEntity<List<PostResource>> response = postsController.getAllPosts(null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().size() >= 1);
    }

    @Test
    void testUpdatePost() {
        // Arrange
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        Community community = communityRepository.save(new Community(new CreateCommunityCommand("Comm-" + uniqueSuffix, "Desc", "Img")));
        User user = userRepository.save(new User("user-" + uniqueSuffix + "@example.com", "user-" + uniqueSuffix, "Name", Role.FAN));
        Post post = postRepository.save(new Post(new CreatePostCommand("Old Content", "Old Img", community.getId(), user.getId()), community, user));

        UpdatePostResource updateResource = new UpdatePostResource("New Content", "New Img");

        // Act
        ResponseEntity<PostResource> response = postsController.updatePost(post.getId(), updateResource);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("New Content", response.getBody().content());

        // Verify in DB
        Optional<Post> updatedPost = postRepository.findById(post.getId());
        assertTrue(updatedPost.isPresent());
        assertEquals("New Content", updatedPost.get().getContent());
    }

    @Test
    void testLikePost() {
        // Arrange
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        Community community = communityRepository.save(new Community(new CreateCommunityCommand("Comm-" + uniqueSuffix, "Desc", "Img")));
        User author = userRepository.save(new User("author-" + uniqueSuffix + "@example.com", "author-" + uniqueSuffix, "Author", Role.FAN));
        User liker = userRepository.save(new User("liker-" + uniqueSuffix + "@example.com", "liker-" + uniqueSuffix, "Liker", Role.FAN));
        Post post = postRepository.save(new Post(new CreatePostCommand("Content", "Img", community.getId(), author.getId()), community, author));

        // Act
        ResponseEntity<Void> response = postsController.likePost(post.getId(), liker.getId());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Verify in DB
        Post postWithLikes = postRepository.findById(post.getId()).get();
        assertTrue(postWithLikes.getLikedBy().stream().anyMatch(u -> u.getId().equals(liker.getId())));
    }
}
