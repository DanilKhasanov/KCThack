package com.hackathon.KDT_HACK.PersonalAccount.Skills;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hackathon.KDT_HACK.Registration.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Entity
@Data
public class UserSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skills skill;

    @Column(nullable = false)
    @Min(1) @Max(10)
    private Integer level;
}
