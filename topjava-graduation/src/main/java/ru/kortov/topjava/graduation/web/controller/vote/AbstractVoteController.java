package ru.kortov.topjava.graduation.web.controller.vote;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.kortov.topjava.graduation.model.Vote;
import ru.kortov.topjava.graduation.repository.RestaurantRepository;
import ru.kortov.topjava.graduation.repository.VoteRepository;
import ru.kortov.topjava.graduation.service.VoteService;

import java.net.URI;
import java.time.LocalDate;

@Slf4j
public abstract class AbstractVoteController {

    @Autowired
    protected VoteRepository repository;

    @Autowired
    protected RestaurantRepository restaurantRepository;

    @Autowired
    protected VoteService service;

    public ResponseEntity<Vote> getCurrentByToDayDate(int userId) {
        log.info("get current vote on today={} for user id={}", LocalDate.now(), userId);
        return ResponseEntity.of(repository.getCurrentByToDayDate(LocalDate.now(), userId));
    }

    @Transactional
    public void update(int userId, int newRestId) {
        log.info("update vote for user {}", userId);
        restaurantRepository.checkExistence(newRestId);
        service.update(userId, newRestId);
    }

    @Transactional
    public ResponseEntity<Vote> createWithLocation(int userId, int restId, String apiUrl) {
        log.info("create vote for user {}", userId);
        restaurantRepository.checkExistence(restId);
        Vote created = service.create(userId, restId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                                                          .path(apiUrl)
                                                          .buildAndExpand().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}