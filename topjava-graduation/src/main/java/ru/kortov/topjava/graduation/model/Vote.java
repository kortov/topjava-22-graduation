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
    @UniqueConstraint(name = "vote_unique_date_user_id_idx", columnNames = {"date_vote", "user_id"})
})
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Vote extends BaseEntity {

    @Column(name = "time_vote", nullable = false)
    @NotNull
    private LocalTime timeVote;

    @Column(name = "date_vote", nullable = false)
    @NotNull
    private LocalDate dateVote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    @ToString.Exclude
    private User user;

    // Foreign key 'userId' instead of Entity User: https://stackoverflow.com/questions/6311776/hibernate-foreign-keys-instead-of-entities
    @Column(name = "user_id", nullable = false)
    @Range(min = 1)
    private int userId;

    // field 'restaurant' used only for delete votes from DB
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rest_id", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    @ToString.Exclude
    private Restaurant restaurant;

    // Foreign key 'restId' instead of Entity Restaurant: https://stackoverflow.com/questions/6311776/hibernate-foreign-keys-instead-of-entities
    @Column(name = "rest_id", nullable = false)
    @Range(min = 1)
    private int restId;

    public Vote(Integer id, LocalDateTime dtVote, int userId, int restId) {
        super(id);
        this.timeVote = dtVote.toLocalTime();
        this.dateVote = dtVote.toLocalDate();
        this.userId = userId;
        this.restId = restId;
    }
}
