package com.nutrigain.notifications.infrastructure.persistence.jpa.repositories;

import com.nutrigain.notifications.domain.model.aggregates.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findTop50ByUserIdOrderByIdDesc(Long userId);

    List<Notification> findTop50ByUserIdAndSeenFalseOrderByIdDesc(Long userId);

    long countByUserIdAndSeenFalse(Long userId);

    Optional<Notification> findByIdAndUserId(Long id, Long userId);

    @Modifying
    @Query("update Notification n set n.seen = true where n.userId = :userId and n.seen = false")
    int markAllSeen(@Param("userId") Long userId);
}
