package com.hackathon.KDT_HACK.dto;

import com.hackathon.KDT_HACK.enums.SkillsCategory;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SkillAddDto {

    String name;
    SkillsCategory skillsCategory;
}
