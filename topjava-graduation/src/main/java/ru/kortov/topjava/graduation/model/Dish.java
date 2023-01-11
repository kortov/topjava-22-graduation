package ru.kortov.topjava.graduation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Set;

@Entity
@Table(name = "dish", uniqueConstraints = {
    @UniqueConstraint(name = "uc_dish_restaurant_id_name", columnNames = {"restaurant_id", "name"})
})
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class Dish extends NamedEntity {

    @Column(name = "price", nullable = false)
    @NotNull
    @PositiveOrZero
    private int price;

    @Column(name = "restaurant_id", nullable = false)
    @JsonIgnore
    private int restaurantId;

    @ManyToMany(mappedBy = "dishesInMenu")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    @ToString.Exclude
    Set<Menu> menus;

    public Dish(Dish dish) {
        this(dish.getId(), dish.getName(), dish.getPrice(), dish.getRestaurantId());
    }

    public Dish(Integer id, String name, int price, int restaurantId) {
        super(id, name);
        this.price = price;
        this.restaurantId = restaurantId;
    }
}
