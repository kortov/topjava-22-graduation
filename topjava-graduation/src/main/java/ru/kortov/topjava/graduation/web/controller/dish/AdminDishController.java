package ru.kortov.topjava.graduation.web.controller.dish;

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
public class AdminDishController {
    static final String API_URL = "/api/admin/restaurants/{restId}/dishes";
    private final DishRepository repository;
    private final RestaurantRepository restaurantRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Dish> get(@PathVariable int restId, @PathVariable int id) {
        log.info("get dish {} of restaurant {}", id, restId);
        return ResponseEntity.of(repository.get(id, restId));
    }

    @Transactional
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void delete(@PathVariable int restId, @PathVariable int id) {
        log.info("delete dish {} of restaurant {}", id, restId);
        Dish dish = repository.checkBelong(id, restId);
        repository.delete(dish);
    }

    @GetMapping
    public List<Dish> getAll(@PathVariable int restId) {
        log.info("getAll for restaurant {}", restId);
        return repository.getAll(restId);
    }

    @Transactional
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void update(@Valid @RequestBody Dish dish, @PathVariable int id, @PathVariable int restId) {
        log.info("update {} for user {}", dish, restId);
        ValidationUtil.assureIdConsistent(dish, id);
        repository.checkBelong(id, restId);
        dish.setRestId(restId);
        repository.save(dish);
    }

    @Transactional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(@PathVariable int restId, @Valid @RequestBody Dish dish) {
        log.info("create dish {} for restaurant {}", dish, restId);
        ValidationUtil.checkNew(dish);
        restaurantRepository.checkExistence(restId);
        dish.setRestId(restId);
        Dish created = repository.save(dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                                                          .path(API_URL + "/{id}")
                                                          .buildAndExpand(created.getRestId(), created.getId())
                                                          .toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}