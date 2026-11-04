package com.nutrigain.social.application.internal.commandservices;

import com.nutrigain.shared.domain.exceptions.BusinessRuleViolationException;
import com.nutrigain.shared.domain.exceptions.ResourceNotFoundException;
import com.nutrigain.social.domain.model.aggregates.Competitor;
import com.nutrigain.social.domain.model.aggregates.Follow;
import com.nutrigain.social.domain.model.commands.FollowUserCommand;
import com.nutrigain.social.domain.model.commands.RegisterCompetitorCommand;
import com.nutrigain.social.domain.model.commands.UnfollowUserCommand;
import com.nutrigain.social.domain.model.commands.UpdateCompetitorScoreCommand;
import com.nutrigain.social.domain.services.SocialCommandService;
import com.nutrigain.social.infrastructure.persistence.jpa.repositories.CompetitorRepository;
import com.nutrigain.social.infrastructure.persistence.jpa.repositories.FollowRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SocialCommandServiceImpl implements SocialCommandService {

    private final CompetitorRepository competitorRepository;
    private final FollowRepository followRepository;

    public SocialCommandServiceImpl(CompetitorRepository competitorRepository, FollowRepository followRepository) {
        this.competitorRepository = competitorRepository;
        this.followRepository = followRepository;
    }

    @Override
    @Transactional
    public void handle(RegisterCompetitorCommand command) {
        if (!competitorRepository.existsByUserId(command.userId())) {
            competitorRepository.save(new Competitor(command.userId(), command.displayName()));
        }
    }

    @Override
    @Transactional
    public void handle(UpdateCompetitorScoreCommand command) {
        Competitor competitor = competitorRepository.findByUserId(command.userId())
                .orElseThrow(() -> new ResourceNotFoundException("Competitor", command.userId()));
        competitor.updateScore(command.experience(), command.level(), command.currentStreak());
        competitorRepository.save(competitor);
    }

    @Override
    @Transactional
    public void handle(FollowUserCommand command) {
        if (!competitorRepository.existsByUserId(command.followeeId())) {
            throw new ResourceNotFoundException("User", command.followeeId());
        }
        if (followRepository.existsByFollowerIdAndFolloweeId(command.followerId(), command.followeeId())) {
            throw new BusinessRuleViolationException("You already follow this user");
        }
        followRepository.save(new Follow(command.followerId(), command.followeeId()));
    }

    @Override
    @Transactional
    public void handle(UnfollowUserCommand command) {
        followRepository.deleteByFollowerIdAndFolloweeId(command.followerId(), command.followeeId());
    }
}
