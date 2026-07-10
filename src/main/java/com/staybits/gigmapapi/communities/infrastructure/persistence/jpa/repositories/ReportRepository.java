package com.staybits.gigmapapi.communities.infrastructure.persistence.jpa.repositories;

import com.staybits.gigmapapi.communities.domain.model.entities.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
