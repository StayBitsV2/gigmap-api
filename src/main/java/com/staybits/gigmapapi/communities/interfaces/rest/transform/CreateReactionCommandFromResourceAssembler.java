package com.staybits.gigmapapi.communities.interfaces.rest.transform;

import com.staybits.gigmapapi.communities.domain.model.commands.AddReactionCommand;
import com.staybits.gigmapapi.communities.domain.model.valueobjects.ReactionEmoji;
import com.staybits.gigmapapi.communities.interfaces.rest.resources.CreateReactionResource;

public class CreateReactionCommandFromResourceAssembler {
    public static AddReactionCommand toCommandFromResource(CreateReactionResource resource) {
        return new AddReactionCommand(
            resource.threadId(),
            resource.commentId(),
            resource.userId(),
            ReactionEmoji.valueOf(resource.emoji().toUpperCase())
        );
    }
}
