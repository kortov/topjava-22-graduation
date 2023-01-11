package ru.kortov.topjava.graduation.web.controller.menu;

import ru.kortov.topjava.graduation.model.Menu;
import ru.kortov.topjava.graduation.to.MenuTo;
import ru.kortov.topjava.graduation.web.MatcherFactory;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static ru.kortov.topjava.graduation.web.controller.dish.DishTestData.DISH_1;
import static ru.kortov.topjava.graduation.web.controller.dish.DishTestData.DISH_2;
import static ru.kortov.topjava.graduation.web.controller.dish.DishTestData.DISH_3;
import static ru.kortov.topjava.graduation.web.controller.dish.DishTestData.REST2_DISH_4;
import static ru.kortov.topjava.graduation.web.controller.dish.DishTestData.REST2_DISH_5;
import static ru.kortov.topjava.graduation.web.controller.dish.DishTestData.REST2_DISH_6;
import static ru.kortov.topjava.graduation.web.controller.dish.DishTestData.REST3_DISH_7;
import static ru.kortov.topjava.graduation.web.controller.dish.DishTestData.REST3_DISH_8;

public class MenuTestData {
    public static final MatcherFactory.Matcher<Menu> MENU_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(
        Menu.class, "restaurant", "dishesInMenu.id", "dishesInMenu.restaurantId");

    public static final int MENU1_ID = 1;
    public static final int REST2_MENU1_ID = 4;
    public static final int INVALID_MENU_ID = 1000;
    public static final String MENU_NEW_DATE = LocalDate.now().plusDays(1).toString();

    public static final Menu REST_1_MENU_1 = new Menu(MENU1_ID, LocalDate.now(), null);
    public static final Menu REST_1_MENU_2 = new Menu(MENU1_ID + 1, LocalDate.now().minusMonths(1), null);
    public static final Menu REST_1_MENU_3 = new Menu(MENU1_ID + 2, LocalDate.now().minusMonths(2), null);
    public static final Menu REST_2_MENU_1 = new Menu(REST2_MENU1_ID, LocalDate.now(), null);
    public static final Menu REST_3_MENU_1 = new Menu(MENU1_ID + 4, LocalDate.now().minusMonths(1), null);

    public static final List<Menu> REST_1_MENUS = List.of(REST_1_MENU_1, REST_1_MENU_2, REST_1_MENU_3);
    public static final List<Menu> REST_MENUS_ON_DATE = List.of(REST_1_MENU_1, REST_2_MENU_1);

    static {
        REST_1_MENU_1.setDishesInMenu(List.of(DISH_1));
        REST_1_MENU_2.setDishesInMenu(List.of(DISH_2));
        REST_1_MENU_3.setDishesInMenu(List.of(DISH_3));
        REST_2_MENU_1.setDishesInMenu(List.of(REST2_DISH_6, REST2_DISH_4, REST2_DISH_5));
        REST_3_MENU_1.setDishesInMenu(List.of(REST3_DISH_7, REST3_DISH_8));
    }

    public static Menu getNew() {
        return new Menu(null, null, null, List.of(DISH_1, DISH_2, DISH_3));
    }

    public static MenuTo getNewMenuTo() {
        return new MenuTo(null, new HashSet<>(List.of(2, 3, 1)), LocalDate.now());
    }

    public static Menu getUpdated() {
        return new Menu(MENU1_ID, REST_1_MENU_1.getMenuDate(), null, List.of(DISH_1, DISH_2));
    }
}
