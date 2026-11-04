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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** End to end through HTTP: sign up fans out to every bounded context and activity flows back to gamification. */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class NutrigainFlowIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper json;

    private JsonNode read(MvcResult result) throws Exception {
        return json.readTree(result.getResponse().getContentAsString());
    }

    private String signUpAndSignIn(String email, long fitnessPathId) throws Exception {
        String body = "{\"name\":\"Test User\",\"email\":\"" + email + "\",\"password\":\"secret1\",\"age\":25,\"fitnessPathId\":"
                + fitnessPathId + "}";
        mvc.perform(post("/api/v1/auth/sign-up").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated());
        MvcResult signIn = mvc.perform(post("/api/v1/auth/sign-in").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + email + "\",\"password\":\"secret1\"}"))
                .andExpect(status().isOk()).andReturn();
        return "Bearer " + read(signIn).get("token").asText();
    }

    @Test
    void fitnessPathsArePublic() throws Exception {
        MvcResult result = mvc.perform(get("/api/v1/fitness-paths")).andExpect(status().isOk()).andReturn();
        assertEquals(3, read(result).size());
    }

    @Test
    void protectedEndpointsRejectAnonymousRequests() throws Exception {
        mvc.perform(get("/api/v1/me/avatar")).andExpect(status().isUnauthorized());
    }

    @Test
    void wrongPasswordIsRejected() throws Exception {
        signUpAndSignIn("wrongpass@example.com", 1);
        mvc.perform(post("/api/v1/auth/sign-in").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"wrongpass@example.com\",\"password\":\"nope-nope\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void duplicatedEmailIsRejected() throws Exception {
        signUpAndSignIn("dup@example.com", 1);
        String body = "{\"name\":\"Other\",\"email\":\"DUP@example.com\",\"password\":\"secret1\",\"age\":30,\"fitnessPathId\":1}";
        mvc.perform(post("/api/v1/auth/sign-up").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isConflict());
    }

    @Test
    void unknownFitnessPathIsRejected() throws Exception {
        String body = "{\"name\":\"Other\",\"email\":\"nopath@example.com\",\"password\":\"secret1\",\"age\":30,\"fitnessPathId\":999}";
        mvc.perform(post("/api/v1/auth/sign-up").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    void signUpBootstrapsEveryContext() throws Exception {
        String auth = signUpAndSignIn("flow@example.com", 1);

        JsonNode routines = read(mvc.perform(get("/api/v1/me/routines").header("Authorization", auth))
                .andExpect(status().isOk()).andReturn());
        assertEquals(3, routines.size());

        JsonNode plan = read(mvc.perform(get("/api/v1/me/nutrition-plan").header("Authorization", auth))
                .andExpect(status().isOk()).andReturn());
        assertEquals("HIGH_PROTEIN", plan.get("focus").asText());
        assertEquals(7, plan.get("days").size());

        JsonNode avatar = read(mvc.perform(get("/api/v1/me/avatar").header("Authorization", auth))
                .andExpect(status().isOk()).andReturn());
        assertEquals(1, avatar.get("level").asInt());
        assertEquals("Titan", avatar.get("characterName").asText());
    }

    @Test
    void completingAWorkoutUpdatesProgressAvatarAndStreak() throws Exception {
        String auth = signUpAndSignIn("workout@example.com", 2);

        JsonNode routines = read(mvc.perform(get("/api/v1/me/routines").header("Authorization", auth)).andReturn());
        long routineDayId = routines.get(0).get("id").asLong();

        JsonNode progress = read(mvc.perform(post("/api/v1/me/routines/" + routineDayId + "/completions")
                .header("Authorization", auth)).andExpect(status().isCreated()).andReturn());
        assertEquals(1, progress.get("completedWorkouts").asInt());
        assertEquals(4, progress.get("plannedWorkouts").asInt());
        assertEquals(25, progress.get("completionPercentage").asInt());

        mvc.perform(post("/api/v1/me/routines/" + routineDayId + "/completions").header("Authorization", auth))
                .andExpect(status().isUnprocessableEntity());

        JsonNode avatar = read(mvc.perform(get("/api/v1/me/avatar").header("Authorization", auth)).andReturn());
        assertEquals(20, avatar.get("experience").asInt());

        JsonNode streak = read(mvc.perform(get("/api/v1/me/streak").header("Authorization", auth)).andReturn());
        assertEquals(1, streak.get("currentDays").asInt());
    }

    @Test
    void dailyExtremeChallengeIsIdempotentAndRewardsOnCompletion() throws Exception {
        String auth = signUpAndSignIn("extreme@example.com", 3);

        mvc.perform(get("/api/v1/me/challenges/daily-extreme").header("Authorization", auth))
                .andExpect(status().isNotFound());

        JsonNode first = read(mvc.perform(post("/api/v1/me/challenges/daily-extreme").header("Authorization", auth))
                .andExpect(status().isOk()).andReturn());
        JsonNode second = read(mvc.perform(post("/api/v1/me/challenges/daily-extreme").header("Authorization", auth))
                .andExpect(status().isOk()).andReturn());
        assertEquals(first.get("id").asLong(), second.get("id").asLong());
        assertEquals("EXTREME", first.get("challenge").get("difficulty").asText());

        JsonNode done = read(mvc.perform(post("/api/v1/me/challenges/" + first.get("id").asLong() + "/complete")
                .header("Authorization", auth)).andExpect(status().isOk()).andReturn());
        assertEquals("COMPLETED", done.get("status").asText());

        JsonNode avatar = read(mvc.perform(get("/api/v1/me/avatar").header("Authorization", auth)).andReturn());
        assertEquals(100, avatar.get("experience").asInt());
        assertEquals(2, avatar.get("level").asInt());

        mvc.perform(post("/api/v1/me/challenges/" + first.get("id").asLong() + "/complete").header("Authorization", auth))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void randomChallengeNeverIsExtreme() throws Exception {
        String auth = signUpAndSignIn("random@example.com", 1);
        for (int i = 0; i < 10; i++) {
            JsonNode drawn = read(mvc.perform(post("/api/v1/me/challenges/draw").header("Authorization", auth))
                    .andExpect(status().isCreated()).andReturn());
            assertFalse("EXTREME".equals(drawn.get("challenge").get("difficulty").asText()));
        }
    }

    @Test
    void bodyWeightAndManualPlanFlow() throws Exception {
        String auth = signUpAndSignIn("manual@example.com", 1);

        mvc.perform(post("/api/v1/me/body-weights").header("Authorization", auth)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"weightKg\":70.5}"))
                .andExpect(status().isCreated());
        JsonNode weights = read(mvc.perform(get("/api/v1/me/body-weights").header("Authorization", auth)).andReturn());
        assertEquals(1, weights.size());

        mvc.perform(post("/api/v1/me/body-weights").header("Authorization", auth)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"weightKg\":5}"))
                .andExpect(status().isBadRequest());

        JsonNode manual = read(mvc.perform(put("/api/v1/me/manual-plan/meals/1").header("Authorization", auth))
                .andExpect(status().isOk()).andReturn());
        assertEquals(1, manual.get("meals").size());
        assertTrue(manual.get("totals").get("calories").asInt() > 0);

        mvc.perform(put("/api/v1/me/manual-plan/meals/1").header("Authorization", auth))
                .andExpect(status().isUnprocessableEntity());
    }
}
