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

    List<Dish> findAllByRestaurantIdOrderByName(int restaurantId);

    @Query("SELECT d.id FROM Dish d WHERE d.restaurantId=:restaurantId")
    Set<Integer> findAllIdsForRestaurant(int restaurantId);

    Optional<Dish> findByIdAndRestaurantId(int id, int restaurantId);

    default Dish checkBelong(int id, int restaurantId) {
        return findByIdAndRestaurantId(id, restaurantId).orElseThrow(
            () -> new DataConflictException("Dish id=" + id + " doesn't belong to Restaurant id=" + restaurantId));
    }
}