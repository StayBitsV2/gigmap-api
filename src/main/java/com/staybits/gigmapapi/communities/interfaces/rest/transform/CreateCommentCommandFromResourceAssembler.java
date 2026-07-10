package com.staybits.gigmapapi.communities.interfaces.rest.transform;

import com.staybits.gigmapapi.communities.domain.model.commands.AddCommentCommand;
import com.staybits.gigmapapi.communities.interfaces.rest.resources.CreateCommentResource;

public class CreateCommentCommandFromResourceAssembler {
    public static AddCommentCommand toCommandFromResource(CreateCommentResource resource) {
        return new AddCommentCommand(resource.threadId(), resource.userId(), resource.content());
    }
}
