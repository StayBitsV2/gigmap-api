package com.staybits.gigmapapi.communities.infrastructure.persistence.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.staybits.gigmapapi.communities.domain.model.aggregates.Thread;

@Repository
public interface ThreadRepository extends JpaRepository<Thread, Long> {
    List<Thread> findAllByCommunityIdOrderByCreatedAtDesc(Long communityId);
}
