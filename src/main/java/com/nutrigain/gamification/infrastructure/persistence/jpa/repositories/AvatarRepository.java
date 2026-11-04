package com.nutrigain.gamification.infrastructure.persistence.jpa.repositories;

import com.nutrigain.gamification.domain.model.aggregates.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AvatarRepository extends JpaRepository<Avatar, Long> {
    Optional<Avatar> findByUserId(Long userId);
}
