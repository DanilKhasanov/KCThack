package com.hackathon.KDT_HACK.Registration;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hackathon.KDT_HACK.PersonalAccount.Achievements.UserAchievements;
import com.hackathon.KDT_HACK.PersonalAccount.Skills.UserSkill;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@Table(name = "test_users")
@Entity
public class User   {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "last_name",nullable = false)
    private String lastName;

    @Column(name = "full_name", nullable = false)
    private String fullName;


    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name ="email",nullable = false, unique = true)
    private String email;

    @Column(name="phone")
    private String phone;

    @Column(name="telegram")
    private String telegram;

    @Column(name = "github")
    private String github;

    @Column(name = "birthday", nullable = false)
    private LocalDate birthday;

    @Column(name ="password",nullable = false)
    @JsonIgnore
    private String password;

    @Column(name = "avatar")
    private String avatar;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "job")
    private UserJob job;

    @Enumerated(EnumType.STRING)
    @Column(name = "level")
    private UserLevel level;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserSkill> skills = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAchievements> achievements = new ArrayList<>();

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "points")
    private int points;

    @Column(name = "token_version", nullable = false)
    private int tokenVersion = 0;

    @PrePersist
    @PreUpdate
    private void updateFullName() {
        this.fullName = this.name + " " + this.lastName;
    }



}
