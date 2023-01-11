package ru.kortov.topjava.graduation.web.controller.vote;

import ru.kortov.topjava.graduation.model.Vote;
import ru.kortov.topjava.graduation.service.VoteService;
import ru.kortov.topjava.graduation.web.MatcherFactory;

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
    public static final int INVALID_VOTE_ID = 99;

    public static final Vote userVote1 = new Vote(USER_VOTE1_ID, LocalDateTime.of(LocalDate.now(), LocalTime.MIN),
                                                  USER_ID, 1);
    public static final Vote userVote2 = new Vote(USER_VOTE1_ID + 1, LocalDateTime.of(2020, 1, 6, 10, 0, 0), USER_ID,
                                                  3);
    public static final Vote adminVote1 = new Vote(ADMIN_VOTE1_ID,
                                                   LocalDateTime.of(LocalDate.now(), LocalTime.of(13, 0)), ADMIN_ID, 2);

    public static final List<Vote> todayVotes = List.of(userVote1, adminVote1);
    public static final List<Vote> todayVotesRest1 = List.of(userVote1);

    public static Vote getNew() {
        return new Vote(null, LocalDateTime.now(), GUEST_ID, 1);
    }

    public static void setVoteDeadLineTime(LocalTime voteTestTime) {
        VoteService.deadlineToChangeVote = voteTestTime;
    }
}
