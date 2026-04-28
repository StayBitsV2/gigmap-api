package com.staybits.gigmapapi.notifications.infrastructure.persistence.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.staybits.gigmapapi.notifications.domain.model.aggregates.UserNotification;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {
    List<UserNotification> findByUserIdOrderByCreatedAtDesc(Long userId);

    boolean existsByUserIdAndNotification_ConcertIdAndNotification_TitleStartingWith(Long userId, Long concertId,
            String titlePrefix);
}
