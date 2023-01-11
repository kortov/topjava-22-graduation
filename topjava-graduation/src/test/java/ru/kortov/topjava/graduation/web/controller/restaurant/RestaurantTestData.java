package ru.kortov.topjava.graduation.web.controller.restaurant;

import ru.kortov.topjava.graduation.model.Restaurant;
import ru.kortov.topjava.graduation.web.MatcherFactory;

import java.util.List;

import static ru.kortov.topjava.graduation.web.controller.dish.DishTestData.REST_1_DISHES;

public class RestaurantTestData {
    public static MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(
        Restaurant.class, "dishes");

    public static final Integer REST_ID1 = 1;
    public static final Integer REST_ID2 = 2;
    public static final Integer INVALID_RESTAURANT_ID = 1000;

    public static final Restaurant REST_1 = new Restaurant(REST_ID1, "Dodo", "Ленинградская 10");
    public static final Restaurant REST_2 = new Restaurant(REST_ID1 + 1, "Вкусно и точка", "Полевая 20");
    public static final Restaurant REST_3 = new Restaurant(REST_ID1 + 2, "KFC", "Московское ш. 200");
    public static final Restaurant REST_4 = new Restaurant(REST_ID1 + 3, "Вилка Ложка", "Ленинградская 20");

    public static final List<Restaurant> RESTAURANTS = List.of(REST_1, REST_3, REST_4, REST_2);

    static {
        REST_1.setDishes(REST_1_DISHES);
    }

    public static Restaurant getNew() {
        return new Restaurant(null, "Новый ресторан", "Новый адрес 50");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(REST_ID1, "Обновленный ресторан", "Обновленный адрес 100");
    }
}
