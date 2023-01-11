package ru.kortov.topjava.graduation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "vote", uniqueConstraints = {
    @UniqueConstraint(name = "uc_vote_date_user_id", columnNames = {"date_vote", "user_id"})
})
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class Vote extends BaseEntity {

    @Column(name = "date_vote", nullable = false)
    @NotNull
    private LocalDate dateVote;

    @Column(name = "time_vote", nullable = false)
    @NotNull
    private LocalTime timeVote;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    @ToString.Exclude
    private User user;

    @Column(name = "user_id", nullable = false)
    @Range(min = 1)
    private int userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    @ToString.Exclude
    private Restaurant restaurant;

    @Column(name = "restaurant_id", nullable = false)
    @Range(min = 1)
    private int restaurantId;

    public Vote(Integer id, LocalDateTime dtVote, int userId, int restaurantId) {
        super(id);
        this.timeVote = dtVote.toLocalTime();
        this.dateVote = dtVote.toLocalDate();
        this.userId = userId;
        this.restaurantId = restaurantId;
    }
}
