package com.hackathon.KCThack.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hackathon.KCThack.enums.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Getter @Setter
@NoArgsConstructor
@Table(name = "test_users",
        indexes = {
                @Index(name = "idx_users_email", columnList = "email"),
                @Index(name = "idx_users_username", columnList = "username")
        }
)
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

    @Column(name ="gender", nullable = false)
    private Gender gender;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User u)) return false;
        return id != null && id.equals(u.id);
    }

    @Override
    public int hashCode() { return getClass().hashCode(); }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", role=" + role +
                '}';
    }



}
