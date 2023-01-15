package ru.kortov.topjava.graduation.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.kortov.topjava.graduation.error.EntityNotFoundException;
import ru.kortov.topjava.graduation.model.Vote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

    @EntityGraph(attributePaths = {"user"})
    @Query("SELECT v FROM Vote v WHERE v.dateVote=:dateVote AND v.restaurantId=:restaurantId ORDER BY v.timeVote")
    List<Vote> findAllForRestaurant(int restaurantId, LocalDate dateVote);

    @EntityGraph(attributePaths = {"user"})
    @Query("SELECT v FROM Vote v WHERE v.dateVote=:dateVote ORDER BY v.restaurantId, v.timeVote")
    List<Vote> findAllByDateVote(LocalDate dateVote);

    @Query("SELECT v from Vote v WHERE v.user.id=:userId AND v.dateVote = :voteDay")
    Optional<Vote> findByUserAndTodayDate(LocalDate voteDay, int userId);

    default Vote checkForToday(int userId) {
        return findByUserAndTodayDate(LocalDate.now(), userId).orElseThrow(
            () -> new EntityNotFoundException(
                "User id=" + userId + " doesn't have vote on today date=" + LocalDate.now()));
    }
}