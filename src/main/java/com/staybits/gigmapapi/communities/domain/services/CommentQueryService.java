package com.staybits.gigmapapi.communities.domain.services;

import com.staybits.gigmapapi.communities.domain.model.entities.Comment;
import com.staybits.gigmapapi.communities.domain.model.queries.GetCommentsByThreadIdQuery;

import java.util.List;

public interface CommentQueryService {
    List<Comment> handle(GetCommentsByThreadIdQuery query);
}
