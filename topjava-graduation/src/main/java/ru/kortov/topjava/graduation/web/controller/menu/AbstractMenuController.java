package ru.kortov.topjava.graduation.web.controller.menu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import ru.kortov.topjava.graduation.model.Menu;
import ru.kortov.topjava.graduation.repository.MenuRepository;

import java.time.LocalDate;
import java.util.List;

@Slf4j
public abstract class AbstractMenuController {

    @Autowired
    protected MenuRepository menuRepository;

    @Cacheable("menu")
    public ResponseEntity<Menu> getByDate(int restaurantId, LocalDate menuDate) {
        log.info("get menu on date {} for restaurant {}", menuDate, restaurantId);
        return ResponseEntity.of(menuRepository.getByDate(menuDate, restaurantId));
    }

    @Cacheable("menus")
    public List<Menu> getAllForRestaurantsByDate(LocalDate menuDate) {
        log.info("get menus on date {} for all restaurants", menuDate);
        return menuRepository.getAllForRestaurantsByDate(menuDate);
    }
}