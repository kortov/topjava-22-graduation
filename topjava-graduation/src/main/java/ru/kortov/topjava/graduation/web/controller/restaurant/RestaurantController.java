package ru.kortov.topjava.graduation.web.controller.restaurant;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kortov.topjava.graduation.model.Restaurant;

import java.util.List;

@RestController
@RequestMapping(value = RestaurantController.API_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
@Tag(name = "Restaurant user API")
public class RestaurantController extends AbstractRestaurantController {
    static final String API_URL = "/api/restaurants";

    @GetMapping("/{restaurantId}")
    @Override
    public Restaurant get(@PathVariable int restaurantId) {
        return super.get(restaurantId);
    }

    @GetMapping
    @Override
    public List<Restaurant> getAll() {
        return super.getAll();
    }
}