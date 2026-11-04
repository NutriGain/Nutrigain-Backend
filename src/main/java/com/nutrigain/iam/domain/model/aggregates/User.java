package com.nutrigain.iam.domain.model.aggregates;

import com.nutrigain.iam.domain.model.events.UserRegisteredEvent;
import com.nutrigain.iam.domain.model.valueobjects.EmailAddress;
import com.nutrigain.shared.domain.model.AggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(name = "uk_users_email", columnNames = "email"))
public class User extends AggregateRoot {

    public static final int MIN_AGE = 13;
    public static final int MAX_AGE = 100;

    @Column(nullable = false, length = 100)
    private String name;

    @Embedded
    private EmailAddress email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private Long fitnessPathId;

    protected User() {
    }

    public User(String name, EmailAddress email, String passwordHash, int age, Long fitnessPathId) {
        if (name == null || name.trim().length() < 2) {
            throw new IllegalArgumentException("Name must have at least 2 characters");
        }
        if (age < MIN_AGE || age > MAX_AGE) {
            throw new IllegalArgumentException("Age must be between " + MIN_AGE + " and " + MAX_AGE);
        }
        if (fitnessPathId == null) {
            throw new IllegalArgumentException("A fitness path is required");
        }
        this.name = name.trim();
        this.email = email;
        this.passwordHash = passwordHash;
        this.age = age;
        this.fitnessPathId = fitnessPathId;
    }

    /** Must be called after the user has been persisted so that the event carries the generated id. */
    public void confirmRegistration() {
        registerEvent(new UserRegisteredEvent(getId(), name, fitnessPathId));
    }

    public String getName() {
        return name;
    }

    public EmailAddress getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public int getAge() {
        return age;
    }

    public Long getFitnessPathId() {
        return fitnessPathId;
    }
}
