package com.nutrigain.gamification.infrastructure.seed;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nutrigain.gamification.domain.model.aggregates.Challenge;
import com.nutrigain.gamification.domain.model.aggregates.MotivationalMessage;
import com.nutrigain.gamification.domain.model.valueobjects.ChallengeCategory;
import com.nutrigain.gamification.domain.model.valueobjects.Difficulty;
import com.nutrigain.gamification.infrastructure.persistence.jpa.repositories.ChallengeRepository;
import com.nutrigain.gamification.infrastructure.persistence.jpa.repositories.MotivationalMessageRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/** Seeds the challenge catalog and the motivational messages when their tables are empty. */
@Component
@Order(3)
public class ChallengeSeeder implements ApplicationRunner {

    private final ChallengeRepository challengeRepository;
    private final MotivationalMessageRepository messageRepository;
    private final ObjectMapper objectMapper;

    public ChallengeSeeder(ChallengeRepository challengeRepository, MotivationalMessageRepository messageRepository,
                           ObjectMapper objectMapper) {
        this.challengeRepository = challengeRepository;
        this.messageRepository = messageRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(ApplicationArguments args) throws IOException {
        if (challengeRepository.count() == 0) {
            List<Challenge> challenges = new ArrayList<>();
            try (InputStream in = new ClassPathResource("seed/challenges.json").getInputStream()) {
                for (JsonNode n : objectMapper.readTree(in)) {
                    challenges.add(new Challenge(n.get("title").asText(), n.get("description").asText(),
                            Difficulty.valueOf(n.get("difficulty").asText()),
                            ChallengeCategory.valueOf(n.get("category").asText()), n.get("xpReward").asInt()));
                }
            }
            challengeRepository.saveAll(challenges);
        }
        if (messageRepository.count() == 0) {
            List<MotivationalMessage> messages = new ArrayList<>();
            try (InputStream in = new ClassPathResource("seed/motivational-messages.json").getInputStream()) {
                for (JsonNode n : objectMapper.readTree(in)) {
                    messages.add(new MotivationalMessage(n.asText()));
                }
            }
            messageRepository.saveAll(messages);
        }
    }
}
