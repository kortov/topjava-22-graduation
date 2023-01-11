package ru.kortov.topjava.graduation.web.controller.restaurant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import ru.kortov.topjava.graduation.model.Restaurant;
import ru.kortov.topjava.graduation.repository.RestaurantRepository;

import java.util.List;

@Slf4j
public abstract class AbstractRestaurantController {

    @Autowired
    protected RestaurantRepository restaurantRepository;

    public Restaurant get(int restaurantId) {
        log.info("get restaurant id={}", restaurantId);
        return restaurantRepository.checkExistence(restaurantId);
    }

    public List<Restaurant> getAll() {
        log.info("getAll restaurants");
        return restaurantRepository.findAll(Sort.by("name"));
    }
}