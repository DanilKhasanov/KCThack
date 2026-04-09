package com.hackathon.KDT_HACK.PersonalAccount.Achievements;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hackathon.KDT_HACK.PersonalAccount.Skills.Skills;
import com.hackathon.KDT_HACK.Registration.User;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user_achievements")
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

}
