package ru.kortov.topjava.graduation.web;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.kortov.topjava.graduation.model.User;

import static java.util.Objects.requireNonNull;

@UtilityClass
public class SecurityUtil {

    public static AuthUser safeGet() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        Object principal = auth.getPrincipal();
        return (principal instanceof AuthUser pr) ? pr : null;
    }

    public static AuthUser get() {
        return requireNonNull(safeGet(), "No authorized user found");
    }

    public static User authUser() {
        return get().getUser();
    }

    public static int authId() {
        return get().getUser().id();
    }
}