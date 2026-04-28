package com.staybits.gigmapapi.concerts.infrastructure.persistence.jpa.repositories;

import com.staybits.gigmapapi.concerts.domain.model.entities.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {
    Optional<Venue> findByName(String name);
}
