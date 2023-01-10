package ru.kortov.topjava.graduation.web.controller.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.kortov.topjava.graduation.model.User;
import ru.kortov.topjava.graduation.repository.UserRepository;
import ru.kortov.topjava.graduation.to.UserTo;
import ru.kortov.topjava.graduation.util.JsonUtil;
import ru.kortov.topjava.graduation.util.UsersUtil;
import ru.kortov.topjava.graduation.web.AbstractControllerTest;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.kortov.topjava.graduation.web.controller.user.ProfileController.PROFILE_REST_URL;
import static ru.kortov.topjava.graduation.web.controller.user.UniqueMailValidator.EXCEPTION_DUPLICATE_EMAIL;
import static ru.kortov.topjava.graduation.web.controller.user.UserTestData.ADMIN_MAIL;
import static ru.kortov.topjava.graduation.web.controller.user.UserTestData.USER_ID;
import static ru.kortov.topjava.graduation.web.controller.user.UserTestData.USER_MAIL;
import static ru.kortov.topjava.graduation.web.controller.user.UserTestData.USER_MATCHER;
import static ru.kortov.topjava.graduation.web.controller.user.UserTestData.admin;
import static ru.kortov.topjava.graduation.web.controller.user.UserTestData.guest;
import static ru.kortov.topjava.graduation.web.controller.user.UserTestData.user;

class ProfileControllerTest extends AbstractControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(PROFILE_REST_URL))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(USER_MATCHER.contentJson(user));
    }

    @Test
    void getUnauthorized() throws Exception {
        perform(MockMvcRequestBuilders.get(PROFILE_REST_URL))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(PROFILE_REST_URL))
            .andExpect(status().isNoContent());
        USER_MATCHER.assertMatch(userRepository.findAll(), admin, guest);
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void update() throws Exception {
        UserTo updatedTo = new UserTo(null, "newName", USER_MAIL, "newPassword", 1500);
        perform(MockMvcRequestBuilders.put(PROFILE_REST_URL).contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(updatedTo)))
            .andDo(print())
            .andExpect(status().isNoContent());

        USER_MATCHER.assertMatch(userRepository.getExisted(USER_ID), UsersUtil.updateFromTo(new User(user), updatedTo));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateInvalid() throws Exception {
        UserTo updatedTo = new UserTo(null, null, "password", null, 1);
        perform(MockMvcRequestBuilders.put(PROFILE_REST_URL)
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(updatedTo)))
            .andDo(print())
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateDuplicate() throws Exception {
        UserTo updatedTo = new UserTo(null, "newName", ADMIN_MAIL, "newPassword", 1500);
        perform(MockMvcRequestBuilders.put(PROFILE_REST_URL).contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(updatedTo)))
            .andDo(print())
            .andExpect(status().isUnprocessableEntity())
            .andExpect(content().string(containsString(EXCEPTION_DUPLICATE_EMAIL)));
    }

}