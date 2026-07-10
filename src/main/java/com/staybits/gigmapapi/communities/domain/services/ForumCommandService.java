package com.staybits.gigmapapi.communities.domain.services;

import com.staybits.gigmapapi.communities.domain.model.aggregates.Thread;
import com.staybits.gigmapapi.communities.domain.model.commands.CreateThreadCommand;

public interface ForumCommandService {
    Thread handle(CreateThreadCommand command);
}
