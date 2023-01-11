package ru.kortov.topjava.graduation.repository;

import org.springframework.transaction.annotation.Transactional;
import ru.kortov.topjava.graduation.error.EntityNotFoundException;
import ru.kortov.topjava.graduation.model.Restaurant;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    default Restaurant checkExistence(int id) {
        return findById(id).orElseThrow(
            () -> new EntityNotFoundException("Restaurant id=" + id + " doesn't exist in database"));
    }
}