package com.hackathon.KDT_HACK.Registration;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "last_name",nullable = false)
    private String lastName;


    @Column(name = "username", nullable = false)
    private String username;

    @Column(name ="email",nullable = false, unique = true)
    private String email;

    @Column(name="phone")
    private String phone;

    @Column(name="telegram")
    private String telegram;

    @Column(name = "birthday", nullable = false)
    private LocalDate birthday;

    @Column(name ="password",nullable = false)
    private String password;


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

}
