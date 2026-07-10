package com.staybits.gigmapapi.communities.infrastructure.persistence.jpa.repositories;

import com.staybits.gigmapapi.communities.domain.model.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByThreadIdOrderByCreatedAtAsc(Long threadId);
}
