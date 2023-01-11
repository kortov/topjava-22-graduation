package ru.kortov.topjava.graduation.web.controller.dish;

import ru.kortov.topjava.graduation.model.Dish;
import ru.kortov.topjava.graduation.web.MatcherFactory;

import java.util.List;

public class DishTestData {
    public static MatcherFactory.Matcher<Dish> DISH_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Dish.class,
                                                                                                           "restaurantId");

    public static final int REST1_DISH1_ID = 1;
    public static final int REST2_DISH_ID = REST1_DISH1_ID + 3;

    public static final Dish DISH_1 = new Dish(REST1_DISH1_ID, "Пицца Моцарелла", 400, 1);
    public static final Dish DISH_2 = new Dish(REST1_DISH1_ID + 1, "Сырники", 150, 1);
    public static final Dish DISH_3 = new Dish(REST1_DISH1_ID + 2, "Ролл с курицей", 200, 1);
    public static final Dish REST2_DISH_4 = new Dish(REST2_DISH_ID, "Гамбургер", 150, 2);
    public static final Dish REST2_DISH_5 = new Dish(REST2_DISH_ID + 1, "Картошка", 100, 2);
    public static final Dish REST2_DISH_6 = new Dish(REST2_DISH_ID + 2, "Биг мак", 300, 2);
    public static final Dish REST3_DISH_7 = new Dish(REST2_DISH_ID + 3, "Боксмастер", 200, 3);
    public static final Dish REST3_DISH_8 = new Dish(REST2_DISH_ID + 4, "Крылья", 250, 3);

    public static final List<Dish> REST_1_DISHES = List.of(DISH_1, DISH_3, DISH_2);

    public static Dish getNew() {
        return new Dish(null, "Новая еда", 150, 1);
    }

    public static Dish getUpdated() {
        return new Dish(REST1_DISH1_ID, "Обновленная еда", 300, 4);
    }
}
