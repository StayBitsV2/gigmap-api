package com.staybits.gigmapapi.communities.application.internal.queryservices;

import org.springframework.stereotype.Service;
import com.staybits.gigmapapi.communities.domain.model.entities.Reaction;
import com.staybits.gigmapapi.communities.domain.model.queries.GetReactionsByTargetQuery;
import com.staybits.gigmapapi.communities.domain.services.ReactionQueryService;
import com.staybits.gigmapapi.communities.infrastructure.persistence.jpa.repositories.ReactionRepository;

import java.util.List;

@Service
public class ReactionQueryServiceImpl implements ReactionQueryService {
    private final ReactionRepository reactionRepository;

    public ReactionQueryServiceImpl(ReactionRepository reactionRepository) {
        this.reactionRepository = reactionRepository;
    }

    @Override
    public List<Reaction> handle(GetReactionsByTargetQuery query) {
        if (query.threadId() != null) {
            return reactionRepository.findByThreadId(query.threadId());
        }
        return reactionRepository.findByCommentId(query.commentId());
    }
}
