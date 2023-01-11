package ru.kortov.topjava.graduation.web.controller.vote;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.kortov.topjava.graduation.web.AbstractControllerTest;
import ru.kortov.topjava.graduation.web.controller.restaurant.RestaurantTestData;
import ru.kortov.topjava.graduation.web.controller.user.UserTestData;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminVoteControllerTest extends AbstractControllerTest {

    private static final String ADMIN_VOTES_API_URL = "/api/admin/votes";
    public static final String RESTAURANT_PARAM = "restaurantId";

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(ADMIN_VOTES_API_URL))
            .andExpect(status().isOk())
            .andDo(print())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(VoteTestData.VOTE_MATCHER.contentJson(VoteTestData.TODAY_VOTES));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getAllByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(ADMIN_VOTES_API_URL).queryParam("voteDate", LocalDate.now().toString()))
            .andExpect(status().isOk())
            .andDo(print())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(VoteTestData.VOTE_MATCHER.contentJson(VoteTestData.TODAY_VOTES));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getAllForRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(ADMIN_VOTES_API_URL)
                                      .param(RESTAURANT_PARAM, String.valueOf(RestaurantTestData.REST_ID1))
                                      .param("voteDate", String.valueOf(LocalDate.now())))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(VoteTestData.VOTE_MATCHER.contentJson(VoteTestData.TODAY_VOTES_FOR_REST_1));
    }

}