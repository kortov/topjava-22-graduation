package ru.kortov.topjava.graduation.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.kortov.topjava.graduation.error.DataConflictException;
import ru.kortov.topjava.graduation.model.Menu;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MenuRepository extends BaseRepository<Menu> {

    @EntityGraph(attributePaths = {"dishesInMenu"})
    @Query("SELECT m FROM Menu m WHERE m.restaurant.id=:restaurantId ORDER BY m.menuDate DESC")
    List<Menu> findAllForRestaurant(int restaurantId);

    @EntityGraph(attributePaths = {"dishesInMenu"})
    @Query("SELECT m FROM Menu m WHERE m.id = :id and m.restaurant.id = :restaurantId")
    Optional<Menu> findByIdAndRestaurant(int id, int restaurantId);

    @Query("SELECT m FROM Menu m WHERE m.id = :id and m.restaurant.id = :restaurantId")
    Optional<Menu> findByIdAndRestaurantLazy(int id, int restaurantId);

    @EntityGraph(attributePaths = {"dishesInMenu", "restaurant"})
    @Query("SELECT m FROM Menu m WHERE m.restaurant.id = :restaurantId AND m.menuDate = :menuDate")
    Optional<Menu> findByDateAndRestaurant(LocalDate menuDate, int restaurantId);

    @EntityGraph(attributePaths = {"dishesInMenu", "restaurant"})
    @Query("SELECT m FROM Menu m WHERE m.menuDate = :menuDate ORDER BY m.restaurant.id")
    List<Menu> getAllByDate(LocalDate menuDate);

    default Menu checkBelong(int id, int restaurantId) {
        return findByIdAndRestaurantLazy(id, restaurantId).orElseThrow(
            () -> new DataConflictException("Menu id=" + id + " doesn't belong to Restaurant id=" + restaurantId));
    }
}