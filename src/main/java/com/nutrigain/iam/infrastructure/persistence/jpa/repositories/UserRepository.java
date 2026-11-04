package com.nutrigain.iam.infrastructure.persistence.jpa.repositories;

import com.nutrigain.iam.domain.model.aggregates.User;
import com.nutrigain.iam.domain.model.valueobjects.EmailAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(EmailAddress email);

    Optional<User> findByEmail(EmailAddress email);
}
