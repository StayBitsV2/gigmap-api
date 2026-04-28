package com.staybits.gigmapapi;

import com.staybits.gigmapapi.communities.domain.model.aggregates.Community;
import com.staybits.gigmapapi.communities.domain.model.commands.CreateCommunityCommand;
import com.staybits.gigmapapi.authentication.domain.model.aggregates.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CommunityTests {

    private Community community;
    private CreateCommunityCommand command;

    @BeforeEach
    void setUp() {
        command = new CreateCommunityCommand(
                "Rock Fans Perú",
                "Comunidad para fans del rock en Perú",
                "https://image.com/community-rock.png"
        );

        community = new Community(command);
    }

    @Test
    void constructor_FromCommand_ShouldSetBasicFields() {
        assertThat(community.getName()).isEqualTo("Rock Fans Perú");
        assertThat(community.getDescription()).isEqualTo("Comunidad para fans del rock en Perú");
        assertThat(community.getImageUrl()).isEqualTo("https://image.com/community-rock.png");
    }

    @Test
    void defaultConstructor_ShouldInitializeCollectionsAsEmpty() {
        Community emptyCommunity = new Community();

        assertThat(emptyCommunity.getPosts()).isNotNull();
        assertThat(emptyCommunity.getPosts()).isEmpty();

        assertThat(emptyCommunity.getMembers()).isNotNull();
        assertThat(emptyCommunity.getMembers()).isEmpty();
    }

    @Test
    void shouldAllowSettingMembersList() {
        User user1 = new User();
        user1.setId(1L);

        User user2 = new User();
        user2.setId(2L);

        community.setMembers(List.of(user1, user2));

        assertThat(community.getMembers()).hasSize(2);
        assertThat(community.getMembers())
                .extracting(User::getId)
                .containsExactlyInAnyOrder(1L, 2L);
    }

    @Test
    void shouldAllowUpdatingBasicInformation() {
        community.setName("Indie Lovers");
        community.setDescription("Comunidad para fans del indie");
        community.setImageUrl("https://image.com/indie.png");

        assertThat(community.getName()).isEqualTo("Indie Lovers");
        assertThat(community.getDescription()).isEqualTo("Comunidad para fans del indie");
        assertThat(community.getImageUrl()).isEqualTo("https://image.com/indie.png");
    }
}
