package com.staybits.gigmapapi.relatedevents.infrastructure.persistence.jpa.repositories;

import com.staybits.gigmapapi.relatedevents.domain.model.aggregates.RelatedEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelatedEventRepository extends JpaRepository<RelatedEvent, Long> {
    List<RelatedEvent> findByConcertId(Long concertId);
}
