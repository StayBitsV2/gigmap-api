package com.staybits.gigmapapi.communities.domain.services;

import com.staybits.gigmapapi.communities.domain.model.aggregates.Post;
import com.staybits.gigmapapi.communities.domain.model.commands.CreateThreadCommand;

public interface ForumCommandService {
    Post handle(CreateThreadCommand command);
}
