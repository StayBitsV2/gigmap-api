package com.staybits.gigmapapi.core.integration.tests;

import com.staybits.gigmapapi.authentication.domain.model.aggregates.User;
import com.staybits.gigmapapi.authentication.domain.model.valueobjects.Role;
import com.staybits.gigmapapi.authentication.infrastructure.persistence.jpa.repositories.UserRepository;
import com.staybits.gigmapapi.communities.domain.model.aggregates.Community;
import com.staybits.gigmapapi.communities.domain.model.commands.CreateCommunityCommand;
import com.staybits.gigmapapi.communities.infrastructure.persistence.jpa.repositories.CommunityRepository;
import com.staybits.gigmapapi.communities.interfaces.rest.CommunitiesController;
import com.staybits.gigmapapi.communities.interfaces.rest.resources.CommunityResource;
import com.staybits.gigmapapi.communities.interfaces.rest.resources.CreateCommunityResource;
import com.staybits.gigmapapi.communities.interfaces.rest.resources.UpdateCommunityResource;
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
class CommunityIntegrationTest {

    @Autowired
    private CommunitiesController communitiesController;

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testCreateCommunity() {
        // Arrange
        String uniqueName = "Community-" + UUID.randomUUID().toString().substring(0, 8);
        CreateCommunityResource resource = new CreateCommunityResource(uniqueName, "Description", "http://image.url");

        // Act
        ResponseEntity<CommunityResource> response = communitiesController.createCommunity(resource);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(uniqueName, response.getBody().name());

        // Verify in DB
        Optional<Community> community = communityRepository.findById(response.getBody().id());
        assertTrue(community.isPresent());
        assertEquals(uniqueName, community.get().getName());
    }

    @Test
    void testGetAllCommunities() {
        // Arrange
        String uniqueName = "List-" + UUID.randomUUID().toString().substring(0, 8);
        Community community = new Community(new CreateCommunityCommand(uniqueName, "Desc", "Img"));
        communityRepository.save(community);

        // Act
        ResponseEntity<List<CommunityResource>> response = communitiesController.getAllCommunities();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().size() >= 1);
        assertTrue(response.getBody().stream().anyMatch(c -> c.name().equals(uniqueName)));
    }

    @Test
    void testGetCommunityById() {
        // Arrange
        String uniqueName = "Get-" + UUID.randomUUID().toString().substring(0, 8);
        Community community = new Community(new CreateCommunityCommand(uniqueName, "Desc", "Img"));
        Community savedCommunity = communityRepository.save(community);

        // Act
        ResponseEntity<CommunityResource> response = communitiesController.getCommunityById(savedCommunity.getId());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(savedCommunity.getId(), response.getBody().id());
        assertEquals(uniqueName, response.getBody().name());
    }

    @Test
    void testUpdateCommunity() {
        // Arrange
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        Community community = new Community(new CreateCommunityCommand("Old-" + uniqueSuffix, "Old Desc", "Old Img"));
        Community savedCommunity = communityRepository.save(community);

        UpdateCommunityResource updateResource = new UpdateCommunityResource("New Name", "New Img", "New Desc");

        // Act
        ResponseEntity<CommunityResource> response = communitiesController.updateCommunity(savedCommunity.getId(), updateResource);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("New Name", response.getBody().name());

        // Verify in DB
        Optional<Community> updatedCommunity = communityRepository.findById(savedCommunity.getId());
        assertTrue(updatedCommunity.isPresent());
        assertEquals("New Name", updatedCommunity.get().getName());
    }

    @Test
    void testJoinCommunity() {
        // Arrange
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        Community community = new Community(new CreateCommunityCommand("Join-" + uniqueSuffix, "Desc", "Img"));
        Community savedCommunity = communityRepository.save(community);

        User user = new User("join-" + uniqueSuffix + "@example.com", "join-" + uniqueSuffix, "Joiner", Role.FAN);
        User savedUser = userRepository.save(user);

        // Act
        ResponseEntity<Void> response = communitiesController.joinCommunity(savedCommunity.getId(), savedUser.getId());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Verify in DB
        Community communityWithMembers = communityRepository.findById(savedCommunity.getId()).get();
        assertTrue(communityWithMembers.getMembers().stream().anyMatch(m -> m.getId().equals(savedUser.getId())));
    }
}
