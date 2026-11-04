package com.nutrigain.achievements.infrastructure.persistence.jpa.repositories;

import com.nutrigain.achievements.domain.model.aggregates.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {
    List<UserBadge> findByUserIdOrderByIdAsc(Long userId);
}
