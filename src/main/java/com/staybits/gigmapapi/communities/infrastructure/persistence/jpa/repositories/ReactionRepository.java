package com.staybits.gigmapapi.communities.infrastructure.persistence.jpa.repositories;

import com.staybits.gigmapapi.communities.domain.model.entities.Reaction;
import com.staybits.gigmapapi.communities.domain.model.valueobjects.ReactionEmoji;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    List<Reaction> findByThreadId(Long threadId);
    List<Reaction> findByCommentId(Long commentId);
    Optional<Reaction> findByUserIdAndThreadIdAndEmoji(Long userId, Long threadId, ReactionEmoji emoji);
    Optional<Reaction> findByUserIdAndCommentIdAndEmoji(Long userId, Long commentId, ReactionEmoji emoji);
}
