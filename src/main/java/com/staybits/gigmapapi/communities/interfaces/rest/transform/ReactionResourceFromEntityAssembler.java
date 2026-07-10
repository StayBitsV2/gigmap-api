package com.staybits.gigmapapi.communities.interfaces.rest.transform;

import com.staybits.gigmapapi.communities.domain.model.entities.Reaction;
import com.staybits.gigmapapi.communities.interfaces.rest.resources.ReactionResource;

public class ReactionResourceFromEntityAssembler {
    public static ReactionResource toResourceFromEntity(Reaction reaction) {
        return new ReactionResource(
            reaction.getId(),
            reaction.getEmoji().name(),
            reaction.getUser().getId(),
            reaction.getThreadId(),
            reaction.getCommentId()
        );
    }
}
