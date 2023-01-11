package ru.kortov.topjava.graduation.web.controller.menu;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kortov.topjava.graduation.model.Menu;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class MenuController extends AbstractMenuController {

    @GetMapping("/api/restaurants/{restaurantId}/menus/for-today")
    public ResponseEntity<Menu> getByDate(@PathVariable int restaurantId) {
        return super.getByDate(restaurantId, LocalDate.now());
    }

    @GetMapping("/api/menus/for-today")
    public List<Menu> getAllForRestaurantsByDate() {
        return super.getAllForRestaurantsByDate(LocalDate.now());
    }
}