package ru.kortov.topjava.graduation.web.controller.dish;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.kortov.topjava.graduation.model.Dish;
import ru.kortov.topjava.graduation.repository.DishRepository;
import ru.kortov.topjava.graduation.util.JsonUtil;
import ru.kortov.topjava.graduation.web.AbstractControllerTest;
import ru.kortov.topjava.graduation.web.controller.user.UserTestData;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.kortov.topjava.graduation.web.controller.restaurant.RestaurantTestData.REST_ID1;

class AdminDishControllerTest extends AbstractControllerTest {

    private static final String API_URL = "/api/admin/restaurants/{restaurantId}/dishes";
    public static final String API_URL_WITH_DISH_ID = API_URL + "/{dishId}";

    @Autowired
    private DishRepository dishRepository;

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(API_URL_WITH_DISH_ID, REST_ID1, DishTestData.REST1_DISH1_ID))
            .andExpect(status().isOk())
            .andDo(print())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(DishTestData.DISH_MATCHER.contentJson(DishTestData.DISH_1));
    }

    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(API_URL_WITH_DISH_ID, REST_ID1, DishTestData.REST1_DISH1_ID))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(API_URL_WITH_DISH_ID, REST_ID1, DishTestData.REST2_DISH_ID))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(API_URL, REST_ID1))
            .andExpect(status().isOk())
            .andDo(print())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(DishTestData.DISH_MATCHER.contentJson(DishTestData.REST_1_DISHES));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Dish newMenu = DishTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(API_URL, REST_ID1)
                                                             .contentType(MediaType.APPLICATION_JSON)
                                                             .content(JsonUtil.writeValue(newMenu)))
            .andExpect(status().isCreated());

        Dish created = DishTestData.DISH_MATCHER.readFromJson(action);
        int newId = created.id();
        newMenu.setId(newId);
        DishTestData.DISH_MATCHER.assertMatch(created, newMenu);
        DishTestData.DISH_MATCHER.assertMatch(dishRepository.getReferenceById(newId), newMenu);
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void createInvalid() throws Exception {
        Dish invalid = new Dish(null, null, 0, 0);
        perform(MockMvcRequestBuilders.post(API_URL, REST_ID1)
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(invalid)))
            .andDo(print())
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void createDuplicate() throws Exception {
        Dish invalid = new Dish(null, DishTestData.DISH_1.getName(), 0, REST_ID1);
        perform(MockMvcRequestBuilders.post(API_URL, REST_ID1)
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(invalid)))
            .andDo(print())
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void update() throws Exception {
        Dish updated = DishTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(API_URL_WITH_DISH_ID, REST_ID1, DishTestData.REST1_DISH1_ID)
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(updated)))
            .andExpect(status().isNoContent());

        DishTestData.DISH_MATCHER.assertMatch(dishRepository.getReferenceById(DishTestData.REST1_DISH1_ID), updated);
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void updateInvalid() throws Exception {
        Dish invalid = new Dish(DishTestData.REST1_DISH1_ID, "", 100, 1);
        perform(MockMvcRequestBuilders.put(API_URL_WITH_DISH_ID, REST_ID1, DishTestData.REST1_DISH1_ID)
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(invalid)))
            .andDo(print())
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void updateHtmlUnsafe() throws Exception {
        Dish invalid = new Dish(DishTestData.REST1_DISH1_ID, "<script>alert(123)</script>", 99, REST_ID1);
        perform(MockMvcRequestBuilders.put(API_URL_WITH_DISH_ID, REST_ID1, DishTestData.REST1_DISH1_ID)
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(invalid)))
            .andDo(print())
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void updateDuplicate() throws Exception {
        Dish invalid = new Dish(DishTestData.REST1_DISH1_ID, DishTestData.DISH_2.getName(), 0, REST_ID1);
        perform(MockMvcRequestBuilders.put(API_URL_WITH_DISH_ID, REST_ID1, DishTestData.REST1_DISH1_ID)
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(invalid)))
            .andDo(print())
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(API_URL_WITH_DISH_ID, REST_ID1, DishTestData.REST1_DISH1_ID))
            .andExpect(status().isNoContent());
        assertFalse(dishRepository.get(DishTestData.REST1_DISH1_ID, REST_ID1).isPresent());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void deleteDataConflict() throws Exception {
        perform(MockMvcRequestBuilders.delete(API_URL_WITH_DISH_ID, REST_ID1, DishTestData.REST2_DISH_ID))
            .andExpect(status().isConflict());
    }
}