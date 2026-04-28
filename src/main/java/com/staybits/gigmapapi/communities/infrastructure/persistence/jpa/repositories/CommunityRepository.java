package com.staybits.gigmapapi.communities.infrastructure.persistence.jpa.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.staybits.gigmapapi.communities.domain.model.aggregates.Community;

public interface CommunityRepository extends JpaRepository<Community, Long> {
    boolean existsByName(String name);

    List<Community> findByMembers_Id(Long userId);
}
