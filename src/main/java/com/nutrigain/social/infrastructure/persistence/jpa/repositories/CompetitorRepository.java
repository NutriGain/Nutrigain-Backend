package com.nutrigain.social.infrastructure.persistence.jpa.repositories;

import com.nutrigain.social.domain.model.aggregates.Competitor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompetitorRepository extends JpaRepository<Competitor, Long> {
    Optional<Competitor> findByUserId(Long userId);

    boolean existsByUserId(Long userId);

    List<Competitor> findAllByOrderByExperienceDescIdAsc(Pageable pageable);

    List<Competitor> findByUserIdInOrderByExperienceDescIdAsc(Collection<Long> userIds);

    List<Competitor> findTop20ByDisplayNameContainingIgnoreCaseOrderByDisplayNameAsc(String name);
}
