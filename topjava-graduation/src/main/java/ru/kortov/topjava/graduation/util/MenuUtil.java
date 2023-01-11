package ru.kortov.topjava.graduation.util;

import lombok.experimental.UtilityClass;
import ru.kortov.topjava.graduation.model.Dish;
import ru.kortov.topjava.graduation.model.Menu;
import ru.kortov.topjava.graduation.model.Restaurant;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class MenuUtil {

    public static Menu createMenu(Restaurant restaurant, List<Dish> dishes, LocalDate menuDate) {
        List<Dish> menus = createDishesInMenu(dishes);
        return new Menu(null, menuDate, restaurant, menus);
    }

    public static List<Dish> createDishesInMenu(List<Dish> dishes) {
        List<Dish> menus = new ArrayList<>();
        dishes.forEach(dish -> menus.add(new Dish(dish)));
        return menus;
    }
}
