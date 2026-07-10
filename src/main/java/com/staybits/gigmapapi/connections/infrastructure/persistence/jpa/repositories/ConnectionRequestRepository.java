package com.staybits.gigmapapi.connections.infrastructure.persistence.jpa.repositories;

import com.staybits.gigmapapi.connections.domain.model.aggregates.ConnectionRequest;
import com.staybits.gigmapapi.connections.domain.model.valueobjects.ConnectionRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConnectionRequestRepository extends JpaRepository<ConnectionRequest, Long> {
    List<ConnectionRequest> findByTargetIdAndStatus(Long targetId, ConnectionRequestStatus status);
    List<ConnectionRequest> findByRequesterIdAndStatus(Long requesterId, ConnectionRequestStatus status);
    Optional<ConnectionRequest> findByRequesterIdAndTargetIdAndStatus(Long requesterId, Long targetId, ConnectionRequestStatus status);
    boolean existsByRequesterIdAndTargetIdAndStatus(Long requesterId, Long targetId, ConnectionRequestStatus status);
}
