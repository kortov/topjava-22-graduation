package ru.kortov.topjava.graduation.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "menu", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"menu_date", "restaurant_id"}, name = "uc_menu_date_restaurant_id")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Menu extends BaseEntity {

    @Column(name = "menu_date", nullable = false)
    @NotNull
    private LocalDate menuDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @ToString.Exclude
    private Restaurant restaurant;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "dish_in_menu",
        joinColumns = @JoinColumn(name = "menu_id", nullable = false, referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "dish_id", nullable = false, referencedColumnName = "id"),
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"menu_id", "dish_id"}, name = "uc_dish_in_menu_menu_id_dish_id")})
    @OrderBy("name")
    @ToString.Exclude
    private List<Dish> dishesInMenu;

    public Menu(Integer id, LocalDate menuDate, Restaurant restaurant) {
        super(id);
        this.menuDate = menuDate;
        this.restaurant = restaurant;
    }

    public Menu(Integer id, LocalDate menuDate, Restaurant restaurant, List<Dish> dishesInMenu) {
        this(id, menuDate, restaurant);
        this.dishesInMenu = dishesInMenu;
    }
}
