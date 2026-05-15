package com.hackathon.KCThack.dto;

import com.hackathon.KCThack.entity.Achievements;
import com.hackathon.KCThack.enums.Gender;
import com.hackathon.KCThack.enums.UserJob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class UserRatingDto {
    private int place;
    private String fullName;
    private int points;
    private UserJob job;
    private Gender gender;
    private int age;
    private Achievements achievements;




}