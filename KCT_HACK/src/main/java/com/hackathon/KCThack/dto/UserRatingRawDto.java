package com.hackathon.KCThack.dto;

import com.hackathon.KCThack.entity.Achievements;
import com.hackathon.KCThack.enums.Gender;
import com.hackathon.KCThack.enums.UserJob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@AllArgsConstructor
public class UserRatingRawDto {

    private String fullName;

    private int points;

    private UserJob job;

    private Gender gender;

    private LocalDate birthday;

    private Achievements achievements;




}