package com.staybits.gigmapapi.communities.application.internal.queryservices;

import org.springframework.stereotype.Service;
import com.staybits.gigmapapi.communities.domain.model.entities.Comment;
import com.staybits.gigmapapi.communities.domain.model.queries.GetCommentsByThreadIdQuery;
import com.staybits.gigmapapi.communities.domain.services.CommentQueryService;
import com.staybits.gigmapapi.communities.infrastructure.persistence.jpa.repositories.CommentRepository;

import java.util.List;

@Service
public class CommentQueryServiceImpl implements CommentQueryService {
    private final CommentRepository commentRepository;

    public CommentQueryServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public List<Comment> handle(GetCommentsByThreadIdQuery query) {
        return commentRepository.findByThreadIdOrderByCreatedAtAsc(query.threadId());
    }
}
