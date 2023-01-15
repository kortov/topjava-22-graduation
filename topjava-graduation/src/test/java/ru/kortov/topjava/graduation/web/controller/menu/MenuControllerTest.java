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

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getByDate() throws Exception {
        perform(
            MockMvcRequestBuilders.get("/api/restaurants/{restaurantId}/menus/for-today", RestaurantTestData.REST_ID2))
            .andExpect(status().isOk())
            .andDo(print())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(MenuTestData.MENU_MATCHER.contentJson(MenuTestData.REST_2_MENU_1));
    }

    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(
            "/api/admin/restaurants/{restaurantId}/menus/{menuId}}", RestaurantTestData.REST_ID1,
            MenuTestData.MENU1_ID))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getAllByDate() throws Exception {
        perform(MockMvcRequestBuilders.get("/api/menus/for-today"))
            .andExpect(status().isOk())
            .andDo(print())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(MenuTestData.MENU_MATCHER.contentJson(MenuTestData.REST_MENUS_ON_DATE));
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get("/api/admin/restaurants/"))
            .andExpect(status().isForbidden());
    }
}