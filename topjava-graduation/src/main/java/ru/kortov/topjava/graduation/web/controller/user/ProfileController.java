package ru.kortov.topjava.graduation.web.controller.user;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.kortov.topjava.graduation.model.User;
import ru.kortov.topjava.graduation.to.UserTo;
import ru.kortov.topjava.graduation.util.UsersUtil;
import ru.kortov.topjava.graduation.web.AuthUser;

import static ru.kortov.topjava.graduation.util.validation.ValidationUtil.assureIdConsistent;

@RestController
@RequestMapping(value = ProfileController.PROFILE_REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileController extends AbstractUserController {
    static final String PROFILE_REST_URL = "/api/profile";

    @GetMapping
    public User get(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get {}", authUser);
        return authUser.getUser();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser) {
        super.delete(authUser.id());
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@RequestBody @Valid UserTo userTo, @AuthenticationPrincipal AuthUser authUser) {
        log.info("update {} with id={}", userTo, authUser.id());
        assureIdConsistent(userTo, authUser.id());
        User user = authUser.getUser();
        prepareAndSave(UsersUtil.updateFromTo(user, userTo));
    }

}