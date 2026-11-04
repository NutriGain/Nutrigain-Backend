package com.nutrigain.gamification.domain.model.aggregates;

import com.nutrigain.gamification.domain.model.events.ChallengeCompletedEvent;
import com.nutrigain.gamification.domain.model.valueobjects.AssignmentKind;
import com.nutrigain.gamification.domain.model.valueobjects.AssignmentStatus;
import com.nutrigain.shared.domain.exceptions.BusinessRuleViolationException;
import com.nutrigain.shared.domain.model.AggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDate;

/** A challenge handed to a user, either drawn at random or the extreme challenge of the day. */
@Entity
@Table(name = "challenge_assignments",
        uniqueConstraints = @UniqueConstraint(name = "uk_assignment_user_daily_slot", columnNames = {"user_id", "daily_slot"}))
public class ChallengeAssignment extends AggregateRoot {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "challenge_id", nullable = false)
    private Long challengeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AssignmentKind kind;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 12)
    private AssignmentStatus status;

    @Column(nullable = false)
    private LocalDate assignedOn;

    private LocalDate completedOn;

    @Column(nullable = false)
    private int xpReward;

    /** Only set for the daily extreme challenge, so a user cannot get two of them on the same day. */
    @Column(name = "daily_slot", length = 20)
    private String dailySlot;

    protected ChallengeAssignment() {
    }

    private ChallengeAssignment(Long userId, Long challengeId, AssignmentKind kind, LocalDate assignedOn,
                                int xpReward, String dailySlot) {
        this.userId = userId;
        this.challengeId = challengeId;
        this.kind = kind;
        this.status = AssignmentStatus.PENDING;
        this.assignedOn = assignedOn;
        this.xpReward = xpReward;
        this.dailySlot = dailySlot;
    }

    public static ChallengeAssignment random(Long userId, Long challengeId, LocalDate date, int xpReward) {
        return new ChallengeAssignment(userId, challengeId, AssignmentKind.RANDOM, date, xpReward, null);
    }

    public static ChallengeAssignment dailyExtreme(Long userId, Long challengeId, LocalDate date, int xpReward) {
        return new ChallengeAssignment(userId, challengeId, AssignmentKind.DAILY_EXTREME, date, xpReward, date.toString());
    }

    /** The assignment must already be persisted so the event carries its id. */
    public void complete(LocalDate date) {
        if (status == AssignmentStatus.COMPLETED) {
            throw new BusinessRuleViolationException("The challenge was already completed");
        }
        this.status = AssignmentStatus.COMPLETED;
        this.completedOn = date;
        registerEvent(new ChallengeCompletedEvent(userId, getId(), xpReward, kind == AssignmentKind.DAILY_EXTREME, date));
    }

    public Long getUserId() {
        return userId;
    }

    public Long getChallengeId() {
        return challengeId;
    }

    public AssignmentKind getKind() {
        return kind;
    }

    public AssignmentStatus getStatus() {
        return status;
    }

    public LocalDate getAssignedOn() {
        return assignedOn;
    }

    public LocalDate getCompletedOn() {
        return completedOn;
    }

    public int getXpReward() {
        return xpReward;
    }
}
