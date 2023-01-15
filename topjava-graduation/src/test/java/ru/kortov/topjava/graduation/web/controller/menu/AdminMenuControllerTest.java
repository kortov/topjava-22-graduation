package ru.kortov.topjava.graduation.web.controller.menu;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.kortov.topjava.graduation.model.Menu;
import ru.kortov.topjava.graduation.repository.MenuRepository;
import ru.kortov.topjava.graduation.to.MenuTo;
import ru.kortov.topjava.graduation.util.JsonUtil;
import ru.kortov.topjava.graduation.web.AbstractControllerTest;
import ru.kortov.topjava.graduation.web.controller.user.UserTestData;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.kortov.topjava.graduation.web.controller.menu.MenuTestData.MENU_NEW_DATE;
import static ru.kortov.topjava.graduation.web.controller.restaurant.RestaurantTestData.REST_ID1;
import static ru.kortov.topjava.graduation.web.controller.restaurant.RestaurantTestData.REST_ID2;

class AdminMenuControllerTest extends AbstractControllerTest {

    private static final String API_ADMIN_URL = "/api/admin/restaurants/{restaurantId}/menus";
    public static final String API_ADMIN_URL_WITH_MENU_ID = API_ADMIN_URL + "/{menuId}";

    @Autowired
    private MenuRepository menuRepository;

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(API_ADMIN_URL_WITH_MENU_ID, REST_ID1, MenuTestData.MENU1_ID))
            .andExpect(status().isOk())
            .andDo(print())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(MenuTestData.MENU_MATCHER.contentJson(MenuTestData.REST_1_MENU_1));
    }

    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(API_ADMIN_URL_WITH_MENU_ID, REST_ID1, MenuTestData.MENU1_ID))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(API_ADMIN_URL_WITH_MENU_ID, REST_ID1, MenuTestData.INVALID_MENU_ID))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(API_ADMIN_URL + "/by-date", REST_ID2)
                                      .queryParam("menuDate", MenuTestData.REST_2_MENU_1.getMenuDate().toString()))
            .andExpect(status().isOk())
            .andDo(print())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(MenuTestData.MENU_MATCHER.contentJson(MenuTestData.REST_2_MENU_1));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getAllForRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(API_ADMIN_URL, REST_ID1))
            .andExpect(status().isOk())
            .andDo(print())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(MenuTestData.MENU_MATCHER.contentJson(MenuTestData.REST_1_MENUS));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getAllByDate() throws Exception {
        perform(MockMvcRequestBuilders.get("/api/admin/menus/by-date")
                                      .queryParam("menuDate", LocalDate.now().toString()))
            .andExpect(status().isOk())
            .andDo(print())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(MenuTestData.MENU_MATCHER.contentJson(MenuTestData.REST_MENUS_ON_DATE));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void createWithLocation() throws Exception {
        MenuTo newMenuTo = MenuTestData.getNewMenuTo();
        newMenuTo.setMenuDate(LocalDate.parse(MENU_NEW_DATE));
        ResultActions action = perform(MockMvcRequestBuilders.post(API_ADMIN_URL, REST_ID1)
                                                             .contentType(MediaType.APPLICATION_JSON)
                                                             .content(JsonUtil.writeValue(newMenuTo)))
            .andExpect(status().isCreated());

        Menu created = MenuTestData.MENU_MATCHER.readFromJson(action);
        int newId = created.id();
        Menu newMenu = MenuTestData.getNew();
        newMenu.setId(newId);
        newMenu.setMenuDate(LocalDate.parse(MENU_NEW_DATE));
        MenuTestData.MENU_MATCHER.assertMatch(created, newMenu);
        MenuTestData.MENU_MATCHER.assertMatch(menuRepository.getReferenceById(newId), newMenu);
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void createInvalid() throws Exception {
        MenuTo invalid = new MenuTo(null, new HashSet<>(List.of()));
        perform(MockMvcRequestBuilders.post(API_ADMIN_URL, REST_ID1)
                                      .queryParam("menuDate", MENU_NEW_DATE)
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(invalid)))
            .andDo(print())
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void createDuplicate() throws Exception {
        MenuTo invalid = new MenuTo(null, new HashSet<>(List.of(3, 2, 1)));
        perform(MockMvcRequestBuilders.post(API_ADMIN_URL, REST_ID1)
                                      .queryParam("menuDate", MenuTestData.REST_1_MENU_1.getMenuDate().toString())
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(invalid)))
            .andDo(print())
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void update() throws Exception {
        MenuTo update = MenuTestData.getNewMenuTo();
        update.setDishIds(Set.of(2, 1));
        perform(MockMvcRequestBuilders.patch(API_ADMIN_URL_WITH_MENU_ID, REST_ID1, MenuTestData.MENU1_ID)
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(update)))
            .andExpect(status().isNoContent());

        Menu expectedUpdated = MenuTestData.getUpdated();
        expectedUpdated.setId(MenuTestData.MENU1_ID);
        expectedUpdated.setMenuDate(LocalDate.now());
        MenuTestData.MENU_MATCHER.assertMatch(
            menuRepository.findByDateAndRestaurant(MenuTestData.REST_1_MENU_1.getMenuDate(), REST_ID1).orElseThrow(),
            expectedUpdated);
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void updateInvalid() throws Exception {
        MenuTo invalid = new MenuTo(null, new HashSet<>(List.of()));
        perform(MockMvcRequestBuilders.patch(API_ADMIN_URL_WITH_MENU_ID, REST_ID1, MenuTestData.MENU1_ID)
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(invalid)))
            .andDo(print())
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(API_ADMIN_URL_WITH_MENU_ID, REST_ID1, MenuTestData.MENU1_ID))
            .andExpect(status().isNoContent());
        assertFalse(menuRepository.findByIdAndRestaurant(MenuTestData.MENU1_ID, REST_ID1).isPresent());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void deleteDataConflict() throws Exception {
        perform(MockMvcRequestBuilders.delete(API_ADMIN_URL_WITH_MENU_ID, REST_ID1, MenuTestData.REST2_MENU1_ID))
            .andExpect(status().isConflict());
    }
}