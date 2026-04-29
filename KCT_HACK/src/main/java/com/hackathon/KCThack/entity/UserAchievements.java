package com.hackathon.KCThack.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "user_achievements",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"user_id", "achievements_id"}
        )
)
public class UserAchievements {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "achievements_id")
    private Achievements achievements;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAchievements that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "UserAchievements{" +
                "id=" + id +
                ", achievementId=" + (achievements != null ? achievements.getId() : null) +
                '}';
    }

}
