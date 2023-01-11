package ru.kortov.topjava.graduation.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kortov.topjava.graduation.error.DataConflictException;
import ru.kortov.topjava.graduation.model.Dish;
import ru.kortov.topjava.graduation.model.Menu;
import ru.kortov.topjava.graduation.repository.DishRepository;
import ru.kortov.topjava.graduation.repository.MenuRepository;
import ru.kortov.topjava.graduation.repository.RestaurantRepository;
import ru.kortov.topjava.graduation.to.MenuTo;
import ru.kortov.topjava.graduation.util.MenuUtil;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public void update(MenuTo menuTo, int restaurantId, int menuId) {
        Menu menu = menuRepository.checkBelong(menuId, restaurantId);
        List<Dish> createdDishesInMenu = MenuUtil.createDishesInMenu(
            dishRepository.findAllById(getIds(menuTo, restaurantId)));
        List<Dish> dishesInMenu = menu.getDishesInMenu();
        dishesInMenu.clear();
        menuRepository.flush();
        dishesInMenu.addAll(createdDishesInMenu);
    }

    @Transactional
    public Menu create(MenuTo menuTo, int restaurantId, LocalDate menuDate) {
        return menuRepository.save(MenuUtil.createMenu(restaurantRepository.checkExistence(restaurantId),
                                                       dishRepository.findAllById(getIds(menuTo, restaurantId)),
                                                       menuDate));
    }

    public Set<Integer> getIds(MenuTo menuTo, int restaurantId) {
        Set<Integer> dishIds = new HashSet<>(menuTo.getDishIds());
        Set<Integer> idsInDb = dishRepository.getAllIdsForRestaurant(restaurantId);
        dishIds.removeAll(idsInDb);
        if (!dishIds.isEmpty()) {
            throw new DataConflictException(
                "Dishes with ids=" + dishIds + " don't belong to Restaurant id=" + restaurantId);
        }
        return menuTo.getDishIds();
    }
}