package ru.kortov.topjava.graduation.util;

import lombok.experimental.UtilityClass;
import ru.kortov.topjava.graduation.model.Role;
import ru.kortov.topjava.graduation.model.User;
import ru.kortov.topjava.graduation.to.UserTo;

import static ru.kortov.topjava.graduation.config.SecurityConfiguration.PASSWORD_ENCODER;

@UtilityClass
public class UsersUtil {

    public static User createNewFromTo(UserTo userTo) {
        return new User(null, userTo.getName(), userTo.getEmail().toLowerCase(), userTo.getPassword(), Role.USER);
    }

    public static User updateFromTo(User user, UserTo userTo) {
        user.setName(userTo.getName());
        user.setEmail(userTo.getEmail().toLowerCase());
        user.setPassword(userTo.getPassword());
        return user;
    }

    public static User prepareToSave(User user) {
        user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));
        user.setEmail(user.getEmail().toLowerCase());
        return user;
    }
}