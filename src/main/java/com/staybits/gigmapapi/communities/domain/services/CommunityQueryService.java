package com.staybits.gigmapapi.communities.domain.services;

import java.util.List;
import java.util.Optional;

import com.staybits.gigmapapi.communities.domain.model.aggregates.Community;
import com.staybits.gigmapapi.communities.domain.model.queries.GetAllCommunitiesJoinedByUserIdQuery;
import com.staybits.gigmapapi.communities.domain.model.queries.GetCommunitiesQuery;
import com.staybits.gigmapapi.communities.domain.model.queries.GetCommunityByIdQuery;

/**
 * Domain service that handles read-only queries related to {@link Community}
 * aggregates.
 * <p>
 * This interface defines the contract for retrieving community data based on
 * different query types,
 * following the CQRS (Command Query Responsibility Segregation) pattern.
 * </p>
 *
 * <p>
 * Typical usage examples include:
 * </p>
 * <ul>
 * <li>Fetching all existing communities using {@link GetCommunitiesQuery}.</li>
 * <li>Retrieving a specific community by its identifier using
 * {@link GetCommunityByIdQuery}.</li>
 * </ul>
 *
 * <p>
 * Implementations of this interface should delegate the data access logic to
 * repositories
 * or persistence adapters within the infrastructure layer, keeping the domain
 * layer independent.
 * </p>
 */

public interface CommunityQueryService {
    List<Community> handle(GetCommunitiesQuery query);

    Optional<Community> handle(GetCommunityByIdQuery query);

    List<Community> handle(GetAllCommunitiesJoinedByUserIdQuery query);
}
