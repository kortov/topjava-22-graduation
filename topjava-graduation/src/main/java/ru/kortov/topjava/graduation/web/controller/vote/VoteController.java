package ru.kortov.topjava.graduation.web.controller.vote;

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
import ru.kortov.topjava.graduation.model.Vote;
import ru.kortov.topjava.graduation.web.AuthUser;

@RestController
@RequestMapping(value = VoteController.API_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class VoteController extends AbstractVoteController {
    static final String API_URL = "/api/votes";

    @GetMapping("/by-today")
    public ResponseEntity<Vote> getCurrentByToDayDate(@AuthenticationPrincipal AuthUser authUser) {
        return super.getCurrentByToDayDate(authUser.id());
    }

    @PatchMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@AuthenticationPrincipal AuthUser authUser, @RequestParam int newRestId) {
        super.update(authUser.id(), newRestId);
    }

    @PostMapping()
    public ResponseEntity<Vote> createWithLocation(@AuthenticationPrincipal AuthUser authUser, @RequestParam int restId
    ) {
        return super.createWithLocation(authUser.id(), restId, API_URL + "/by-today");
    }
}