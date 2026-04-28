package com.staybits.gigmapapi.concerts.infrastructure.persistence.jpa.repositories;

import com.staybits.gigmapapi.concerts.domain.model.entities.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlatformRepository extends JpaRepository<Platform, Long> {
    Optional<Platform> findByName(String name);
}
