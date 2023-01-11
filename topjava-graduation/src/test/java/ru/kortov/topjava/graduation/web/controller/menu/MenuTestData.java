package ru.kortov.topjava.graduation.web.controller.menu;

import ru.kortov.topjava.graduation.model.Menu;
import ru.kortov.topjava.graduation.to.MenuTo;
import ru.kortov.topjava.graduation.web.MatcherFactory;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.List;

import static ru.kortov.topjava.graduation.web.controller.dish.DishTestData.dish1;
import static ru.kortov.topjava.graduation.web.controller.dish.DishTestData.dish2;
import static ru.kortov.topjava.graduation.web.controller.dish.DishTestData.dish3;
import static ru.kortov.topjava.graduation.web.controller.dish.DishTestData.dish7;
import static ru.kortov.topjava.graduation.web.controller.dish.DishTestData.dish8;
import static ru.kortov.topjava.graduation.web.controller.dish.DishTestData.rest2Dish4;
import static ru.kortov.topjava.graduation.web.controller.dish.DishTestData.rest2Dish5;
import static ru.kortov.topjava.graduation.web.controller.dish.DishTestData.rest2Dish6;

public class MenuTestData {
    public static final MatcherFactory.Matcher<Menu> MENU_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(
        Menu.class, "restaurant", "dishesInMenu.id", "dishesInMenu.restId");

    public static final int MENU1_ID = 1;
    public static final int REST2_MENU1_ID = 4;
    public static final int INVALID_MENU_ID = 99;
    public static final String NEW_DATE = LocalDate.now().plusDays(1).toString();

    public static final Menu rest1Menu1 = new Menu(MENU1_ID, LocalDate.now(), null); //RestaurantTestData.rest1);
    public static final Menu rest1Menu2 = new Menu(MENU1_ID + 1, LocalDate.of(2020, Month.JANUARY, 31), null);
    // RestaurantTestData.rest1);
    public static final Menu rest1Menu3 = new Menu(MENU1_ID + 2, LocalDate.of(2020, Month.JANUARY, 29), null);
    // RestaurantTestData.rest1);
    public static final Menu rest2Menu1 = new Menu(REST2_MENU1_ID, LocalDate.now(), null);//, RestaurantTestData.rest2);
    public static final Menu rest3Menu1 = new Menu(MENU1_ID + 4, LocalDate.of(2020, Month.JANUARY, 31), null);
    //RestaurantTestData.rest3);

    public static final List<Menu> rest1Menus = List.of(rest1Menu1, rest1Menu2, rest1Menu3);
    public static final List<Menu> restMenusOnDate = List.of(rest1Menu1, rest2Menu1);

    static {
        rest1Menu1.setDishesInMenu(List.of(dish1));
        rest1Menu2.setDishesInMenu(List.of(dish2));
        rest1Menu3.setDishesInMenu(List.of(dish3));
        rest2Menu1.setDishesInMenu(List.of(rest2Dish6, rest2Dish4, rest2Dish5));
        rest3Menu1.setDishesInMenu(List.of(dish7, dish8));
    }

    public static Menu getNew() {
        return new Menu(null, LocalDate.of(2020, Month.FEBRUARY, 1), null,
                        List.of(dish1, dish2, dish3));
    }

    public static MenuTo getNewMenuTo() {
        return new MenuTo(null, new HashSet<>(List.of(2, 3, 1)), LocalDate.now());
    }

    public static Menu getUpdated() {
        return new Menu(MENU1_ID, rest1Menu1.getMenuDate(), null, List.of(dish1, dish2));
    }
}
