package com.hackathon.KCThack.dto;

import lombok.Data;

@Data
public class UserSkillResponseDto {

    private Long skillId;

    private String skillName;

    private Integer level;
}