package ru.kortov.topjava.graduation.web.controller.vote;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.kortov.topjava.graduation.model.Vote;
import ru.kortov.topjava.graduation.repository.VoteRepository;
import ru.kortov.topjava.graduation.web.AbstractControllerTest;
import ru.kortov.topjava.graduation.web.controller.restaurant.RestaurantTestData;
import ru.kortov.topjava.graduation.web.controller.user.UserTestData;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VoteControllerTest extends AbstractControllerTest {

    private static final String VOTES_API_URL = "/api/votes";
    public static final String RESTAURANT_PARAM = "restaurantId";

    @Autowired
    private VoteRepository voteRepository;

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getCurrentByToDayDate() throws Exception {
        perform(MockMvcRequestBuilders.get(VOTES_API_URL + "/for-today"))
            .andExpect(status().isOk())
            .andDo(print())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(VoteTestData.VOTE_MATCHER.contentJson(VoteTestData.USER_VOTE_1));
    }

    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(VOTES_API_URL + "/for-today"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get("/api/admin/votes/"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(VOTES_API_URL + "/" + VoteTestData.INVALID_VOTE_ID))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = UserTestData.GUEST_MAIL)
    void createWithLocation() throws Exception {
        Vote newVote = VoteTestData.getNew();
        ResultActions action = perform(
            MockMvcRequestBuilders.post(VOTES_API_URL)
                                  .queryParam(RESTAURANT_PARAM, RestaurantTestData.REST_ID1.toString()))
            .andExpect(status().isCreated());

        Vote created = VoteTestData.VOTE_MATCHER.readFromJson(action);
        int newId = created.id();
        newVote.setId(newId);
        VoteTestData.VOTE_MATCHER.assertMatch(created, newVote);
        VoteTestData.VOTE_MATCHER.assertMatch(voteRepository.getReferenceById(newId), newVote);
    }

    @Test
    @WithUserDetails(value = UserTestData.GUEST_MAIL)
    void createInvalid() throws Exception {
        perform(MockMvcRequestBuilders.post(VOTES_API_URL)
                                      .queryParam(RESTAURANT_PARAM,
                                                  RestaurantTestData.INVALID_RESTAURANT_ID.toString()))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void createDuplicate() throws Exception {
        perform(MockMvcRequestBuilders.post(VOTES_API_URL)
                                      .queryParam(RESTAURANT_PARAM, RestaurantTestData.REST_ID1.toString()))
            .andDo(print())
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void updateInvalid() throws Exception {
        perform(MockMvcRequestBuilders.patch(VOTES_API_URL)
                                      .queryParam(RESTAURANT_PARAM,
                                                  RestaurantTestData.INVALID_RESTAURANT_ID.toString()))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void updateBeforeEleven() throws Exception {
        VoteTestData.setVoteDeadLineTime(LocalTime.now().plusSeconds(1));
        perform(
            MockMvcRequestBuilders.patch(VOTES_API_URL)
                                  .queryParam(RESTAURANT_PARAM, RestaurantTestData.REST_ID2.toString()))
            .andExpect(status().isNoContent());

        assertEquals(RestaurantTestData.REST_ID2,
                     voteRepository.getReferenceById(VoteTestData.USER_VOTE1_ID).getRestaurantId());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void updateAfterEleven() throws Exception {
        VoteTestData.setVoteDeadLineTime(LocalTime.now().minusSeconds(1));
        perform(
            MockMvcRequestBuilders.patch(VOTES_API_URL)
                                  .queryParam(RESTAURANT_PARAM, RestaurantTestData.REST_ID2.toString()))
            .andExpect(status().isConflict());

        assertEquals(RestaurantTestData.REST_ID1,
                     voteRepository.getReferenceById(VoteTestData.USER_VOTE1_ID).getRestaurantId());
    }
}