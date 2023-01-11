package ru.kortov.topjava.graduation.service;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.kortov.topjava.graduation.error.DataConflictException;
import ru.kortov.topjava.graduation.error.IllegalRequestDataException;
import ru.kortov.topjava.graduation.model.Vote;
import ru.kortov.topjava.graduation.repository.VoteRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@AllArgsConstructor
public class VoteService {
    public static LocalTime changeVoteDeadline = LocalTime.of(11, 0, 0);
    private VoteRepository repository;

    public void update(int userId, int restaurantId, LocalDateTime now) {
        Vote vote = repository.checkForToday(userId);
        vote.setTimeVote(now.toLocalTime());
        if (vote.getTimeVote().isBefore(changeVoteDeadline)) {
            vote.setRestaurantId(restaurantId);
        } else {
            throw new DataConflictException(
                "Vote id=" + vote.id() + " can't be updated after " + changeVoteDeadline);
        }
    }

    public Vote create(int userId, int restaurantId, LocalDateTime now) {
        try {
            return repository.save(new Vote(null, now, userId, restaurantId));
        } catch (DataIntegrityViolationException e) {
            throw new IllegalRequestDataException("User id=" + userId + " can only have one vote for today");
        }
    }
}
