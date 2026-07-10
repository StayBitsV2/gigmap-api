package com.staybits.gigmapapi.communities.domain.services;

import com.staybits.gigmapapi.communities.domain.model.entities.Reaction;
import com.staybits.gigmapapi.communities.domain.model.commands.AddReactionCommand;
import com.staybits.gigmapapi.communities.domain.model.commands.RemoveReactionCommand;

public interface ReactionCommandService {
    Reaction handle(AddReactionCommand command);
    void handle(RemoveReactionCommand command);
}
