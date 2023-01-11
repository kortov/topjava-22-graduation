package ru.kortov.topjava.graduation.web.controller.menu;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.kortov.topjava.graduation.web.AbstractControllerTest;
import ru.kortov.topjava.graduation.web.controller.restaurant.RestaurantTestData;
import ru.kortov.topjava.graduation.web.controller.user.UserTestData;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MenuControllerTest extends AbstractControllerTest {

    private static final String API_PROFILE_URL = MenuController.API_URL + '/';

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(API_PROFILE_URL + RestaurantTestData.REST_ID2 + "/menus/for-today"))
            .andExpect(status().isOk())
            .andDo(print())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(MenuTestData.MENU_MATCHER.contentJson(MenuTestData.rest2Menu1));
    }

    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(
            API_PROFILE_URL + RestaurantTestData.REST_ID1 + "/menus/" + MenuTestData.MENU1_ID))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getAllForRestaurantsByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(API_PROFILE_URL + "menus/for-today"))
            .andExpect(status().isOk())
            .andDo(print())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(MenuTestData.MENU_MATCHER.contentJson(MenuTestData.restMenusOnDate));
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get("/api/admin/restaurants/"))
            .andExpect(status().isForbidden());
    }
}