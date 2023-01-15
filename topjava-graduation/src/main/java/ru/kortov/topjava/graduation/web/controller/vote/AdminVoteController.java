package ru.kortov.topjava.graduation.web.controller.vote;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kortov.topjava.graduation.model.Vote;
import ru.kortov.topjava.graduation.repository.VoteRepository;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = AdminVoteController.API_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
@Tag(name = "Vote admin API")
public class AdminVoteController {
    static final String API_URL = "/api/admin/votes";

    private final VoteRepository voteRepository;

    @GetMapping()
    public List<Vote> getAll(@RequestParam @Nullable Integer restaurantId,
                             @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate voteDate
    ) {
        final var searchDate = voteDate == null ? LocalDate.now() : voteDate;
        if (restaurantId != null) {
            log.info("getAll votes for restaurant id={} on date={}", restaurantId, searchDate);
            return voteRepository.findAllForRestaurant(restaurantId, searchDate);
        } else {
            log.info("getAll votes for restaurants on date={}", searchDate);
            return voteRepository.findAllByDateVote(searchDate);
        }
    }

}