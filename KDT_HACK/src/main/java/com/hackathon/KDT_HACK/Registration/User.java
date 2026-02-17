package com.hackathon.KDT_HACK.Registration;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Table(name = "test_users")
@Entity
public class User   {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name",nullable = false)
    private String fullName;
    @Column(name ="user_name",nullable = false, unique = true)
    private String userName;
    @Column(name ="email",nullable = false, unique = true)
    private String email;
    @Column(name ="password",nullable = false)
    private String password;


    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus status;
}
