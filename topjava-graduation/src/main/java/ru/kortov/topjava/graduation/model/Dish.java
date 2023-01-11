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
    @UniqueConstraint(name = "dish_unique_rest_id_name_idx", columnNames = {"rest_id", "name"})
})
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Dish extends NamedEntity {

    @Column(name = "price", nullable = false)
    @NotNull
    @PositiveOrZero
    private int price;

    @Column(name = "rest_id", nullable = false)
    // 'restId' not need in request/response, because restId get from url
    @JsonIgnore
    private int restId;

    //field 'menus' needed only for @OnDelete in "dish_in_menu" datatable
    @ManyToMany(mappedBy = "dishesInMenu")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    Set<Menu> menus;

    public Dish(Dish dish) {
        this(dish.getId(), dish.getName(), dish.getPrice(), dish.getRestId());
    }

    public Dish(Integer id, String name, int price, int restId) {
        super(id, name);
        this.price = price;
        this.restId = restId;
    }
}
