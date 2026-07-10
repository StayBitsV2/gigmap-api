package com.staybits.gigmapapi.communities.domain.services;

import com.staybits.gigmapapi.communities.domain.model.entities.Comment;
import com.staybits.gigmapapi.communities.domain.model.commands.AddCommentCommand;

public interface CommentCommandService {
    Comment handle(AddCommentCommand command);
}
