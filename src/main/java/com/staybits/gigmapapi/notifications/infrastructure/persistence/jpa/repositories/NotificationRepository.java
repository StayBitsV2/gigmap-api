package com.staybits.gigmapapi.notifications.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.staybits.gigmapapi.notifications.domain.model.aggregates.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
}
