package com.hackathon.KCThack.dto;


import com.hackathon.KCThack.enums.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UserResponseDto {

    private String id;

    private String name;

    private String lastName;

    private String fullName;

    private String username;

    private String email;

    private String bio;

    private String phone;

    private String telegram;

    private String github;

    private LocalDate birthday;

    private Integer age;

    private Gender gender;

    private String avatar;

    private UserRole role;

    private UserStatus status;

    private UserJob job;

    private UserLevel level;

    private List<UserSkillResponseDto> skills;

    private AchievementResponseDto achievement;

    private TeamShortResponseDto team;

    private LocalDate createdAt;

    private Integer points;
}
