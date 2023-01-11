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
    @UniqueConstraint(columnNames = {"menu_date", "rest_id"}, name = "menu_unique_date_rest_id_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Menu extends BaseEntity {

    @Column(name = "menu_date", nullable = false)
    @NotNull
    private LocalDate menuDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rest_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) //https://stackoverflow.com/a/44988100/548473
    @NotNull
    // 'restaurant' not need in Response when Admin retrieve menu data. Needed when User GET Menu of Restaurants
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @ToString.Exclude
    private Restaurant restaurant;

    // Basic Many-to-Many through @JoinTable: https://www.baeldung.com/jpa-many-to-many
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "dish_in_menu",
        joinColumns = @JoinColumn(name = "menu_id", nullable = false, referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "dish_id", nullable = false, referencedColumnName = "id"),
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"menu_id", "dish_id"}, name = "dish_in_menu_unique_menu_id_dish_id_idx")})
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
