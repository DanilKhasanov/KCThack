package com.hackathon.KCThack.dto;

import com.hackathon.KCThack.enums.SkillsCategory;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SkillAddDto {

    String name;
    SkillsCategory skillsCategory;
}
