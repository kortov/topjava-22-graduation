package ru.kortov.topjava.graduation.web.controller.menu;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.kortov.topjava.graduation.model.Menu;
import ru.kortov.topjava.graduation.repository.RestaurantRepository;
import ru.kortov.topjava.graduation.service.MenuService;
import ru.kortov.topjava.graduation.to.MenuTo;
import ru.kortov.topjava.graduation.util.validation.ValidationUtil;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
@CacheConfig(cacheNames = {"menu", "menus"})
public class AdminMenuController extends AbstractMenuController {
    static final String API_URL = "/api/admin/restaurants/{restaurantId}/menus";
    private final MenuService service;
    private final RestaurantRepository restaurantRepository;

    @GetMapping(API_URL + "/{id}")
    public ResponseEntity<Menu> get(@PathVariable int restaurantId, @PathVariable int id) {
        restaurantRepository.checkExistence(restaurantId);
        return ResponseEntity.of(menuRepository.get(id, restaurantId));
    }

    @GetMapping(API_URL + "/by-date")
    @Override
    public ResponseEntity<Menu> getByDate(@PathVariable int restaurantId,
                                          @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate
    ) {
        return super.getByDate(restaurantId, menuDate == null ? LocalDate.now() : menuDate);
    }

    @GetMapping("/api/admin/menus/by-date")
    @Override
    public List<Menu> getAllForRestaurantsByDate(
        @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate
    ) {
        return super.getAllForRestaurantsByDate(menuDate == null ? LocalDate.now() : menuDate);
    }

    @GetMapping(API_URL)
    public List<Menu> getAllForRestaurant(@PathVariable int restaurantId) {
        log.info("getAll menus for restaurant {}", restaurantId);
        restaurantRepository.checkExistence(restaurantId);
        return menuRepository.getAllForRestaurant(restaurantId);
    }

    @PostMapping(value = API_URL, consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(allEntries = true)
    public ResponseEntity<Menu> createWithLocation(@Valid @RequestBody MenuTo menuTo, @PathVariable int restaurantId) {
        log.info("create menu {} for restaurant {}", menuTo, restaurantId);
        ValidationUtil.checkNew(menuTo);
        Menu saved = service.create(menuTo, restaurantId, menuTo.getMenuDate());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                                                          .path(API_URL + "/{id}")
                                                          .buildAndExpand(saved.getRestaurant().getId(), saved.getId())
                                                          .toUri();
        return ResponseEntity.created(uriOfNewResource).body(saved);
    }

    @PatchMapping(value = API_URL + "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void update(@Valid @RequestBody MenuTo menuTo, @PathVariable int restaurantId, @PathVariable int id) {
        log.info("update menu id={} of restaurant {}", id, restaurantId);
        ValidationUtil.assureIdConsistent(menuTo, id);
        service.update(menuTo, restaurantId, id);
    }

    @Transactional
    @DeleteMapping(API_URL + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void delete(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("delete menu id={} of restaurant {}", id, restaurantId);
        menuRepository.delete(menuRepository.checkBelong(id, restaurantId));
    }
}