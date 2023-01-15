package ru.kortov.topjava.graduation.web.controller.vote;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.kortov.topjava.graduation.model.Vote;
import ru.kortov.topjava.graduation.repository.RestaurantRepository;
import ru.kortov.topjava.graduation.repository.VoteRepository;
import ru.kortov.topjava.graduation.service.VoteService;
import ru.kortov.topjava.graduation.web.AuthUser;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping(value = VoteController.API_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
@Tag(name = "Vote user API")
public class VoteController {
    static final String API_URL = "/api/votes";

    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;
    private final VoteService service;

    @GetMapping("/for-today")
    @Operation(summary = "User's vote for today")
    public ResponseEntity<Vote> getCurrentByToDayDate(@AuthenticationPrincipal AuthUser authUser) {
        final var userId = authUser.id();
        final var today = LocalDate.now();
        log.info("get current vote on today={} for user id={}", today, userId);
        return ResponseEntity.of(voteRepository.getCurrentByToDayDate(today, userId));
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Vote for a restaurant")
    public ResponseEntity<Vote> createWithLocation(@AuthenticationPrincipal AuthUser authUser,
                                                   @RequestParam int restaurantId
    ) {
        final var now = LocalDateTime.now();
        final var userId = authUser.id();
        log.info("create vote for user {} for {}", userId, now);
        restaurantRepository.checkExistence(restaurantId);
        Vote created = service.create(userId, restaurantId, now);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                                                          .path(API_URL + "/for-today")
                                                          .buildAndExpand().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PatchMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Change vote for a restaurant", description = "Vote can be changed only before 11 AM localtime")
    public void update(@AuthenticationPrincipal AuthUser authUser, @RequestParam int restaurantId) {
        final var userId = authUser.id();
        final var now = LocalDateTime.now();
        log.info("update vote for user {} to {}", userId, now);
        restaurantRepository.checkExistence(restaurantId);
        service.update(userId, restaurantId, now);
    }
}