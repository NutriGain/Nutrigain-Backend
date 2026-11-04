package com.nutrigain.social.domain.model.aggregates;

import com.nutrigain.shared.domain.exceptions.BusinessRuleViolationException;
import com.nutrigain.shared.domain.model.AggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "follows",
        uniqueConstraints = @UniqueConstraint(name = "uk_follow_pair", columnNames = {"follower_id", "followee_id"}))
public class Follow extends AggregateRoot {

    @Column(name = "follower_id", nullable = false)
    private Long followerId;

    @Column(name = "followee_id", nullable = false)
    private Long followeeId;

    protected Follow() {
    }

    public Follow(Long followerId, Long followeeId) {
        if (followerId.equals(followeeId)) {
            throw new BusinessRuleViolationException("You cannot follow yourself");
        }
        this.followerId = followerId;
        this.followeeId = followeeId;
    }

    public Long getFollowerId() {
        return followerId;
    }

    public Long getFolloweeId() {
        return followeeId;
    }
}
