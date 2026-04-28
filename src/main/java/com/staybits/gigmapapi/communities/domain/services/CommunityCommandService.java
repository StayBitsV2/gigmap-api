package com.staybits.gigmapapi.communities.domain.services;

import java.util.Optional;

import com.staybits.gigmapapi.communities.domain.model.aggregates.Community;
import com.staybits.gigmapapi.communities.domain.model.commands.CreateCommunityCommand;
import com.staybits.gigmapapi.communities.domain.model.commands.DeleteCommunityCommand;
import com.staybits.gigmapapi.communities.domain.model.commands.JoinCommunityCommand;
import com.staybits.gigmapapi.communities.domain.model.commands.LeaveCommunityCommand;
import com.staybits.gigmapapi.communities.domain.model.commands.UpdateCommunityCommand;

/**
 * Domain service responsible for handling commands that modify the state of {@link Community} aggregates.
 * <p>
 * This interface defines the contract for executing write operations in accordance with the
 * CQRS (Command Query Responsibility Segregation) pattern. Each method processes a specific command
 * that represents an intention to change the system’s state.
 * </p>
 *
 * <p>Typical use cases include:</p>
 * <ul>
 *     <li>Creating a new community via {@link CreateCommunityCommand}.</li>
 *     <li>Updating an existing community via {@link UpdateCommunityCommand}.</li>
 *     <li>Deleting a community via {@link DeleteCommunityCommand}.</li>
 *     <li>Allowing a user to join a community via {@link JoinCommunityCommand}.</li>
 *     <li>Allowing a user to leave a community via {@link LeaveCommunityCommand}.</li>
 * </ul>
 *
 * <p>Implementations should delegate domain logic to the corresponding aggregate roots and ensure
 * that all business invariants are respected.</p>
 */
public interface CommunityCommandService {
    Community handle(CreateCommunityCommand command);

    Optional<Community> handle(UpdateCommunityCommand command);

    void handle(DeleteCommunityCommand command);

    void handle(JoinCommunityCommand command);

    void handle(LeaveCommunityCommand command);
}
