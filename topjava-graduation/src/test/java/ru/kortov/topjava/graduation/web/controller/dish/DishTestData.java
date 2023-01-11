package ru.kortov.topjava.graduation.web.controller.dish;

import ru.kortov.topjava.graduation.model.Dish;
import ru.kortov.topjava.graduation.web.MatcherFactory;

import java.util.List;

public class DishTestData {
    public static MatcherFactory.Matcher<Dish> DISH_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Dish.class,
                                                                                                           "restId");

    public static final int DISH1_ID = 1;
    public static final int REST2_DISH4_ID = 4;

    // TODO: rename to uppercase
    public static final Dish dish1 = new Dish(DISH1_ID, "Мисо суп", 330, 1);
    public static final Dish dish2 = new Dish(DISH1_ID + 1, "Ролл Филадельфия", 1000, 1);
    public static final Dish dish3 = new Dish(DISH1_ID + 2, "Пуэр", 500, 1);
    public static final Dish rest2Dish4 = new Dish(REST2_DISH4_ID, "Домашний супчик", 400, 2);
    public static final Dish rest2Dish5 = new Dish(REST2_DISH4_ID + 1, "Плов узбекский", 250, 2);
    public static final Dish rest2Dish6 = new Dish(REST2_DISH4_ID + 2, "Бургер из говядины", 200, 2);
    public static final Dish dish7 = new Dish(REST2_DISH4_ID + 3, "Гриль Кинг", 350, 3);
    public static final Dish dish8 = new Dish(REST2_DISH4_ID + 4, "Кола", 100, 3);

    public static final List<Dish> rest1Dishes = List.of(dish1, dish3, dish2);

    public static Dish getNew() {
        return new Dish(null, "Новая еда", 99, 1);
    }

    public static Dish getUpdated() {
        return new Dish(DISH1_ID, "Обновленная еда", 100, 4);
    }
}
