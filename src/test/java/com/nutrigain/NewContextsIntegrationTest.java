package com.nutrigain;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Profile, achievements, notifications and social, driven by events from the original contexts. */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class NewContextsIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper json;

    private JsonNode read(MvcResult result) throws Exception {
        return json.readTree(result.getResponse().getContentAsString());
    }

    private record Session(String auth, long userId) {
    }

    private Session register(String name, String email) throws Exception {
        mvc.perform(post("/api/v1/auth/sign-up").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"" + name + "\",\"email\":\"" + email
                                + "\",\"password\":\"secret1\",\"age\":30,\"fitnessPathId\":1}"))
                .andExpect(status().isCreated());
        JsonNode signIn = read(mvc.perform(post("/api/v1/auth/sign-in").contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"" + email + "\",\"password\":\"secret1\"}")).andReturn());
        return new Session("Bearer " + signIn.get("token").asText(), signIn.get("user").get("id").asLong());
    }

    @Test
    void profileComputesBmiAndCalories() throws Exception {
        Session s = register("Profile User", "profile@example.com");

        JsonNode empty = read(mvc.perform(get("/api/v1/me/profile").header("Authorization", s.auth())).andReturn());
        assertTrue(empty.get("bmi").isNull());

        mvc.perform(post("/api/v1/me/body-weights").header("Authorization", s.auth())
                .contentType(MediaType.APPLICATION_JSON).content("{\"weightKg\":80}")).andExpect(status().isCreated());
        JsonNode summary = read(mvc.perform(put("/api/v1/me/profile").header("Authorization", s.auth())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"heightCm\":180,\"sex\":\"MALE\",\"activityLevel\":\"MODERATE\"}"))
                .andExpect(status().isOk()).andReturn());
        assertEquals(24.7, summary.get("bmi").asDouble(), 0.001);
        assertEquals("NORMAL", summary.get("bmiCategory").asText());
        // (10*80 + 6.25*180 - 5*30 + 5) * 1.55 = 1780 * 1.55 = 2759
        assertEquals(2759,summary.get("dailyCalorieTarget").asInt());

        mvc.perform(put("/api/v1/me/profile").header("Authorization", s.auth())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"heightCm\":50,\"sex\":\"MALE\",\"activityLevel\":\"MODERATE\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void workoutUnlocksBadgeAndNotifies() throws Exception {
        Session s = register("Badge User", "badge@example.com");

        JsonNode welcome = read(mvc.perform(get("/api/v1/me/notifications").header("Authorization", s.auth())).andReturn());
        assertEquals("WELCOME", welcome.get(0).get("type").asText());

        long routineId = read(mvc.perform(get("/api/v1/me/routines").header("Authorization", s.auth())).andReturn())
                .get(0).get("id").asLong();
        mvc.perform(post("/api/v1/me/routines/" + routineId + "/completions").header("Authorization", s.auth()))
                .andExpect(status().isCreated());

        JsonNode badges = read(mvc.perform(get("/api/v1/me/badges").header("Authorization", s.auth())).andReturn());
        boolean firstWorkout = false;
        for (JsonNode b : badges) {
            if ("FIRST_WORKOUT".equals(b.get("code").asText())) {
                firstWorkout = b.get("unlocked").asBoolean();
            }
        }
        assertTrue(firstWorkout);

        // welcome + FIRST_WORKOUT badge
        assertTrue(read(mvc.perform(get("/api/v1/me/notifications/unseen-count")
                .header("Authorization", s.auth())).andReturn()).get("count").asInt() >= 2);

        mvc.perform(post("/api/v1/me/notifications/seen-all").header("Authorization", s.auth()))
                .andExpect(status().isNoContent());
        assertEquals(0, read(mvc.perform(get("/api/v1/me/notifications/unseen-count")
                .header("Authorization", s.auth())).andReturn()).get("count").asInt());
    }

    @Test
    void extremeChallengeUnlocksChampionBadgeAndLevelUpNotification() throws Exception {
        Session s = register("Extreme User", "extremebadge@example.com");
        long assignmentId = read(mvc.perform(post("/api/v1/me/challenges/daily-extreme")
                .header("Authorization", s.auth())).andReturn()).get("id").asLong();
        mvc.perform(post("/api/v1/me/challenges/" + assignmentId + "/complete").header("Authorization", s.auth()))
                .andExpect(status().isOk());

        String badges = mvc.perform(get("/api/v1/me/badges").header("Authorization", s.auth()))
                .andReturn().getResponse().getContentAsString();
        JsonNode list = json.readTree(badges);
        for (JsonNode b : list) {
            if ("EXTREME_CHAMPION".equals(b.get("code").asText())) {
                assertTrue(b.get("unlocked").asBoolean());
            }
        }
        String notifications = mvc.perform(get("/api/v1/me/notifications").header("Authorization", s.auth()))
                .andReturn().getResponse().getContentAsString();
        assertTrue(notifications.contains("LEVEL_UP"));
        assertTrue(notifications.contains("BADGE_AWARDED"));
    }

    @Test
    void followingFriendsShapesTheFriendsLeaderboard() throws Exception {
        Session ana = register("Ana Ranking", "ana.rank@example.com");
        Session beto = register("Beto Ranking", "beto.rank@example.com");

        long routineId = read(mvc.perform(get("/api/v1/me/routines").header("Authorization", beto.auth())).andReturn())
                .get(0).get("id").asLong();
        mvc.perform(post("/api/v1/me/routines/" + routineId + "/completions").header("Authorization", beto.auth()))
                .andExpect(status().isCreated());

        assertEquals(1, read(mvc.perform(get("/api/v1/me/social/leaderboard").header("Authorization", ana.auth()))
                .andReturn()).size());

        JsonNode found = read(mvc.perform(get("/api/v1/social/users?name=beto rank").header("Authorization", ana.auth()))
                .andReturn());
        assertEquals(beto.userId(), found.get(0).get("userId").asLong());
        assertEquals(false, found.get(0).get("following").asBoolean());

        mvc.perform(put("/api/v1/me/following/" + beto.userId()).header("Authorization", ana.auth()))
                .andExpect(status().isNoContent());
        mvc.perform(put("/api/v1/me/following/" + beto.userId()).header("Authorization", ana.auth()))
                .andExpect(status().isUnprocessableEntity());
        mvc.perform(put("/api/v1/me/following/" + ana.userId()).header("Authorization", ana.auth()))
                .andExpect(status().isUnprocessableEntity());

        JsonNode friends = read(mvc.perform(get("/api/v1/me/social/leaderboard").header("Authorization", ana.auth()))
                .andReturn());
        assertEquals(2, friends.size());
        assertEquals(beto.userId(), friends.get(0).get("userId").asLong());
        assertEquals(1, friends.get(0).get("rank").asInt());
        assertEquals(20, friends.get(0).get("experience").asInt());

        mvc.perform(delete("/api/v1/me/following/" + beto.userId()).header("Authorization", ana.auth()))
                .andExpect(status().isNoContent());
        assertEquals(1, read(mvc.perform(get("/api/v1/me/social/leaderboard").header("Authorization", ana.auth()))
                .andReturn()).size());

        mvc.perform(put("/api/v1/me/following/999999").header("Authorization", ana.auth()))
                .andExpect(status().isNotFound());
    }
}
