package com.staybits.gigmapapi.communities.domain.services;

import com.staybits.gigmapapi.communities.domain.model.entities.Reaction;
import com.staybits.gigmapapi.communities.domain.model.queries.GetReactionsByTargetQuery;

import java.util.List;

public interface ReactionQueryService {
    List<Reaction> handle(GetReactionsByTargetQuery query);
}
