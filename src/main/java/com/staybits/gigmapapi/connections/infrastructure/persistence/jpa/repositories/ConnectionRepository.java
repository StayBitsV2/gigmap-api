package com.staybits.gigmapapi.connections.infrastructure.persistence.jpa.repositories;

import com.staybits.gigmapapi.connections.domain.model.aggregates.Connection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, Long> {
    @Query("SELECT c FROM Connection c WHERE c.fan1.id = :userId OR c.fan2.id = :userId")
    List<Connection> findByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(c) > 0 FROM Connection c WHERE (c.fan1.id = :userId1 AND c.fan2.id = :userId2) OR (c.fan1.id = :userId2 AND c.fan2.id = :userId1)")
    boolean areConnected(@Param("userId1") Long userId1, @Param("userId2") Long userId2);
}
