package ru.kortov.topjava.graduation.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.kortov.topjava.graduation.error.DataConflictException;
import ru.kortov.topjava.graduation.model.Dish;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {

    @Query("SELECT d FROM Dish d WHERE d.restaurantId=:restaurantId ORDER BY d.name")
    List<Dish> getAll(int restaurantId);

    @Query("SELECT d.id FROM Dish d WHERE d.restaurantId=:restaurantId")
    Set<Integer> getAllIdsForRestaurant(int restaurantId);

    @Query("SELECT d FROM Dish d WHERE d.id = :id and d.restaurantId = :restaurantId")
    Optional<Dish> get(int id, int restaurantId);

    default Dish checkBelong(int id, int restaurantId) {
        return get(id, restaurantId).orElseThrow(
            () -> new DataConflictException("Dish id=" + id + " doesn't belong to Restaurant id=" + restaurantId));
    }
}