package com.nutrigain.gamification.application.internal.commandservices;

import com.nutrigain.gamification.application.internal.outboundservices.acl.ExternalFitnessService;
import com.nutrigain.gamification.domain.model.aggregates.Avatar;
import com.nutrigain.gamification.domain.model.aggregates.Streak;
import com.nutrigain.gamification.domain.model.commands.CreateAvatarCommand;
import com.nutrigain.gamification.domain.model.commands.InitializeStreakCommand;
import com.nutrigain.gamification.domain.model.commands.RecordActivityCommand;
import com.nutrigain.gamification.domain.model.events.ActivityRecordedEvent;
import com.nutrigain.gamification.domain.services.ProgressionCommandService;
import com.nutrigain.gamification.infrastructure.persistence.jpa.repositories.AvatarRepository;
import com.nutrigain.gamification.infrastructure.persistence.jpa.repositories.StreakRepository;
import com.nutrigain.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProgressionCommandServiceImpl implements ProgressionCommandService {

    private final AvatarRepository avatarRepository;
    private final StreakRepository streakRepository;
    private final ExternalFitnessService externalFitnessService;
    private final ApplicationEventPublisher eventPublisher;

    public ProgressionCommandServiceImpl(AvatarRepository avatarRepository, StreakRepository streakRepository,
                                         ExternalFitnessService externalFitnessService,
                                         ApplicationEventPublisher eventPublisher) {
        this.avatarRepository = avatarRepository;
        this.streakRepository = streakRepository;
        this.externalFitnessService = externalFitnessService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public Avatar handle(CreateAvatarCommand command) {
        return avatarRepository.findByUserId(command.userId())
                .orElseGet(() -> avatarRepository.save(
                        new Avatar(command.userId(), externalFitnessService.fetchCharacterForPath(command.fitnessPathId()))));
    }

    @Override
    @Transactional
    public Streak handle(InitializeStreakCommand command) {
        return streakRepository.findByUserId(command.userId())
                .orElseGet(() -> streakRepository.save(new Streak(command.userId())));
    }

    @Override
    @Transactional
    public void handle(RecordActivityCommand command) {
        Avatar avatar = avatarRepository.findByUserId(command.userId())
                .orElseThrow(() -> new ResourceNotFoundException("Avatar", command.userId()));
        Streak streak = streakRepository.findByUserId(command.userId())
                .orElseGet(() -> new Streak(command.userId()));
        int levelBefore = avatar.level();
        avatar.gainExperience(command.experience());
        streak.recordActivity(command.date());
        avatarRepository.save(avatar);
        streakRepository.save(streak);
        eventPublisher.publishEvent(new ActivityRecordedEvent(command.userId(), avatar.getExperience(),
                avatar.level(), avatar.level() > levelBefore, streak.getCurrentDays(), command.date()));
    }
}
