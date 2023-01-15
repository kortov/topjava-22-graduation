package ru.kortov.topjava.graduation.web.controller.dish;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.kortov.topjava.graduation.model.Dish;
import ru.kortov.topjava.graduation.repository.DishRepository;
import ru.kortov.topjava.graduation.repository.RestaurantRepository;
import ru.kortov.topjava.graduation.util.validation.ValidationUtil;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = AdminDishController.API_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
@CacheConfig(cacheNames = {"menu", "menus"})
@Tag(name = "Dish admin API")
public class AdminDishController {
    static final String API_URL = "/api/admin/restaurants/{restaurantId}/dishes";
    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Dish> get(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("get dish {} for restaurant {}", id, restaurantId);
        return ResponseEntity.of(dishRepository.get(id, restaurantId));
    }

    @GetMapping
    public List<Dish> getAll(@PathVariable int restaurantId) {
        log.info("getAll for restaurant {}", restaurantId);
        return dishRepository.getAll(restaurantId);
    }

    @Transactional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Dish> createWithLocation(@PathVariable int restaurantId, @Valid @RequestBody Dish dish) {
        log.info("create dish {} for restaurant {}", dish, restaurantId);
        ValidationUtil.checkNew(dish);
        restaurantRepository.checkExistence(restaurantId);
        dish.setRestaurantId(restaurantId);
        Dish created = dishRepository.save(dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                                                          .path(API_URL + "/{id}")
                                                          .buildAndExpand(created.getRestaurantId(), created.getId())
                                                          .toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Transactional
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void update(@Valid @RequestBody Dish dish, @PathVariable int restaurantId, @PathVariable int id) {
        log.info("update dish {} for user {}", dish, restaurantId);
        ValidationUtil.assureIdConsistent(dish, id);
        dishRepository.checkBelong(id, restaurantId);
        dish.setRestaurantId(restaurantId);
        dishRepository.save(dish);
    }

    @Transactional
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void delete(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("delete dish {} of restaurant {}", id, restaurantId);
        Dish dish = dishRepository.checkBelong(id, restaurantId);
        dishRepository.delete(dish);
    }
}