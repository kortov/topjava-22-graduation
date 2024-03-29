package ru.kortov.topjava.graduation.web.controller.user;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import ru.kortov.topjava.graduation.model.User;
import ru.kortov.topjava.graduation.repository.UserRepository;
import ru.kortov.topjava.graduation.util.UsersUtil;

import static org.slf4j.LoggerFactory.getLogger;

public abstract class AbstractUserController {

    protected final Logger log = getLogger(getClass());

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    private UniqueMailValidator emailValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(emailValidator);
    }

    public ResponseEntity<User> get(int id) {
        log.info("get user {}", id);
        return ResponseEntity.of(userRepository.findById(id));
    }

    public void delete(int id) {
        log.info("delete user {}", id);
        userRepository.deleteExisted(id);
    }

    protected User prepareAndSave(User user) {
        return userRepository.save(UsersUtil.prepareToSave(user));
    }
}