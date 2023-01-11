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

    private static final String API_URL = "/api/votes";

    @Autowired
    private VoteRepository voteRepository;

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getCurrentByToDayDate() throws Exception {
        perform(MockMvcRequestBuilders.get(API_URL + "/by-today"))
            .andExpect(status().isOk())
            .andDo(print())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(VoteTestData.VOTE_MATCHER.contentJson(VoteTestData.userVote1));
    }

    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(API_URL + "/by-today"))
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
        perform(MockMvcRequestBuilders.get(API_URL + "/" + VoteTestData.INVALID_VOTE_ID))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = UserTestData.GUEST_MAIL)
    void createWithLocation() throws Exception {
        Vote newVote = VoteTestData.getNew();
        ResultActions action = perform(
            MockMvcRequestBuilders.post(API_URL)
                                  .queryParam("restId", RestaurantTestData.REST_ID1.toString()));

        Vote created = VoteTestData.VOTE_MATCHER.readFromJson(action);
        int newId = created.id();
        newVote.setId(newId);
        VoteTestData.VOTE_MATCHER.assertMatch(created, newVote);
        VoteTestData.VOTE_MATCHER.assertMatch(voteRepository.getReferenceById(newId), newVote);
    }

    @Test
    @WithUserDetails(value = UserTestData.GUEST_MAIL)
    void createInvalid() throws Exception {
        perform(MockMvcRequestBuilders.post(API_URL)
                                      .queryParam("restId", RestaurantTestData.INVALID_RESTAURANT_ID.toString()))
            .andDo(print())
            .andExpect(status().isConflict());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void createDuplicate() throws Exception {
        perform(MockMvcRequestBuilders.post(API_URL).queryParam("restId", RestaurantTestData.REST_ID1.toString()))
            .andDo(print())
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void updateInvalid() throws Exception {
        perform(MockMvcRequestBuilders.patch(API_URL)
                                      .queryParam("newRestId", RestaurantTestData.INVALID_RESTAURANT_ID.toString()))
            .andDo(print())
            .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void updateBeforeEleven() throws Exception {
        VoteTestData.setVoteDeadLineTime(LocalTime.now().plusHours(1));
        perform(MockMvcRequestBuilders.patch(API_URL).queryParam("newRestId", RestaurantTestData.REST_ID2.toString()))
            .andExpect(status().isNoContent());

        assertEquals(RestaurantTestData.REST_ID2,
                     voteRepository.getReferenceById(VoteTestData.USER_VOTE1_ID).getRestId());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void updateAfterEleven() throws Exception {
        VoteTestData.setVoteDeadLineTime(LocalTime.now().minusHours(1));
        perform(MockMvcRequestBuilders.patch(API_URL).queryParam("newRestId", RestaurantTestData.REST_ID2.toString()))
            .andExpect(status().isConflict());

        assertEquals(RestaurantTestData.REST_ID1,
                     voteRepository.getReferenceById(VoteTestData.USER_VOTE1_ID).getRestId());
    }
}