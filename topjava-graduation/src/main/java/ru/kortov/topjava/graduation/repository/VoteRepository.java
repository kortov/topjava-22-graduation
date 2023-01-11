package ru.kortov.topjava.graduation.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.kortov.topjava.graduation.error.DataConflictException;
import ru.kortov.topjava.graduation.model.Vote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

    @Query("SELECT v FROM Vote v WHERE v.id = :id AND v.user.id = :userId AND v.restId =:restId")
    Optional<Vote> get(int id, int userId, int restId);

    @EntityGraph(attributePaths = {"user"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT v FROM Vote v WHERE v.dateVote=:dateVote AND v.restId=:restId ORDER BY v.user.name")
    List<Vote> getAllForRestaurant(int restId, LocalDate dateVote);

    @EntityGraph(attributePaths = {"user"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT v FROM Vote v WHERE v.dateVote=:dateVote ORDER BY v.restId, v.user.name")
    List<Vote> getAll(LocalDate dateVote);

    @Query("SELECT v from Vote v WHERE v.user.id=:userId AND v.dateVote = :voteDay")
    Optional<Vote> getCurrentByToDayDate(LocalDate voteDay, int userId);

    default Vote checkForToday(int userId) {
        return getCurrentByToDayDate(LocalDate.now(), userId).orElseThrow(
            () -> new DataConflictException(
                "User id=" + userId + " doesn't have vote on today date=" + LocalDate.now()));
    }
}