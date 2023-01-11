package ru.kortov.topjava.graduation.web.controller.vote;

import ru.kortov.topjava.graduation.model.Vote;
import ru.kortov.topjava.graduation.service.VoteService;
import ru.kortov.topjava.graduation.web.MatcherFactory;
import ru.kortov.topjava.graduation.web.controller.restaurant.RestaurantTestData;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static ru.kortov.topjava.graduation.web.controller.user.UserTestData.ADMIN_ID;
import static ru.kortov.topjava.graduation.web.controller.user.UserTestData.GUEST_ID;
import static ru.kortov.topjava.graduation.web.controller.user.UserTestData.USER_ID;

public class VoteTestData {
    public static MatcherFactory.Matcher<Vote> VOTE_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Vote.class,
                                                                                                           "restaurant",
                                                                                                           "timeVote");

    public static final int USER_VOTE1_ID = 1;
    public static final int ADMIN_VOTE1_ID = 3;
    public static final int INVALID_VOTE_ID = 1000;

    public static final Vote USER_VOTE_1 = new Vote(USER_VOTE1_ID, LocalDateTime.of(LocalDate.now(), LocalTime.MIN),
                                                    USER_ID, RestaurantTestData.REST_ID1);
    public static final Vote ADMIN_VOTE_1 = new Vote(ADMIN_VOTE1_ID,
                                                     LocalDateTime.of(LocalDate.now(), LocalTime.of(15, 0)), ADMIN_ID,
                                                     RestaurantTestData.REST_ID2);

    public static final List<Vote> TODAY_VOTES = List.of(USER_VOTE_1, ADMIN_VOTE_1);
    public static final List<Vote> TODAY_VOTES_FOR_REST_1 = List.of(USER_VOTE_1);

    public static Vote getNew() {
        return new Vote(null, LocalDateTime.now(), GUEST_ID, 1);
    }

    public static void setVoteDeadLineTime(LocalTime voteTestTime) {
        VoteService.changeVoteDeadline = voteTestTime;
    }
}
