package com.nutrigain.social.domain.services;

import com.nutrigain.social.domain.model.commands.FollowUserCommand;
import com.nutrigain.social.domain.model.commands.RegisterCompetitorCommand;
import com.nutrigain.social.domain.model.commands.UnfollowUserCommand;
import com.nutrigain.social.domain.model.commands.UpdateCompetitorScoreCommand;

public interface SocialCommandService {
    void handle(RegisterCompetitorCommand command);

    void handle(UpdateCompetitorScoreCommand command);

    void handle(FollowUserCommand command);

    void handle(UnfollowUserCommand command);
}
