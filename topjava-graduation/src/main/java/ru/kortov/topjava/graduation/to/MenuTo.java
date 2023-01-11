package ru.kortov.topjava.graduation.to;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MenuTo extends BaseTo {

    @NotNull
    private LocalDate menuDate;

    @Size(min = 1)
    @NotNull
    private Set<Integer> dishIds;

    public MenuTo(Integer id, Set<Integer> dishIds) {
        super(id);
        this.dishIds = dishIds;
    }

    public MenuTo(Integer id, Set<Integer> dishIds, LocalDate menuDate) {
        this(id, dishIds);
        this.menuDate = menuDate;
    }

/* // if we want to add - set 'id' in Swagger
   @Schema(accessMode = Schema.AccessMode.READ_WRITE)  // https://stackoverflow.com/questions/17319266/can-i-annotate-a-member-inherited-from-a-superclass
    public Integer getId() {
        return this.id;
    }*/
}
