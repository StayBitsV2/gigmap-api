package com.staybits.gigmapapi.analytics.infrastructure.persistence.jpa.repositories;

import com.staybits.gigmapapi.analytics.domain.model.aggregates.AnalyticsEvent;
import com.staybits.gigmapapi.analytics.domain.model.valueobjects.AnalyticsEventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AnalyticsEventRepository extends JpaRepository<AnalyticsEvent, Long> {

    List<AnalyticsEvent> findByUserIdAndEventTypeAndCreatedAtBetween(
        Long userId, AnalyticsEventType eventType, LocalDateTime start, LocalDateTime end);

    List<AnalyticsEvent> findByUserIdAndCreatedAtBetween(
        Long userId, LocalDateTime start, LocalDateTime end);

    @Query("SELECT COUNT(e) FROM AnalyticsEvent e WHERE e.userId = :userId AND e.eventType = :eventType AND e.createdAt BETWEEN :start AND :end")
    long countByUserIdAndEventTypeAndCreatedAtBetween(
        @Param("userId") Long userId,
        @Param("eventType") AnalyticsEventType eventType,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(e) FROM AnalyticsEvent e WHERE e.userId = :userId AND e.createdAt BETWEEN :start AND :end")
    long countByUserIdAndCreatedAtBetween(
        @Param("userId") Long userId,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end);
}
