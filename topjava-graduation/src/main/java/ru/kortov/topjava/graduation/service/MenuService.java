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
    public void update(MenuTo menuTo, int restId, int menuId) {
        Menu menu = menuRepository.checkBelong(menuId, restId);
        List<Dish> createdDishesInMenu = MenuUtil.createDishesInMenu(
            dishRepository.findAllById(getIds(menuTo, restId)));
        List<Dish> dishesInMenu = menu.getDishesInMenu();
        // here Hibernate directly clear the rows of dishesInMenu from the DB table DISHINMENU
        dishesInMenu.clear();
        menuRepository.flush();
        // here Hibernate directly appends the rows of dishesInMenu to the DB table DISHINMENU
        dishesInMenu.addAll(createdDishesInMenu);
    }

    @Transactional
    public Menu create(MenuTo menuTo, int restId, LocalDate menuDate) {
        return menuRepository.save(MenuUtil.createMenu(restaurantRepository.checkExistence(restId),
                                                       dishRepository.findAllById(getIds(menuTo, restId)), menuDate));
    }

    public Set<Integer> getIds(MenuTo menuTo, int restId) {
        Set<Integer> dishIds = new HashSet<>(menuTo.getDishIds());
        Set<Integer> idsRepository = dishRepository.getAllIds(restId, dishIds);
        dishIds.removeAll(idsRepository);
        if (!dishIds.isEmpty()) {
            throw new DataConflictException(
                "Dishes with ids=" + dishIds + " doesn't belong to Restaurant id=" + restId);
        }
        return idsRepository;
    }
}