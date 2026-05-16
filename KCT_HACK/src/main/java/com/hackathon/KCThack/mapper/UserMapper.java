package com.hackathon.KCThack.mapper;

import com.hackathon.KCThack.TeamManagement.model.Team;
import com.hackathon.KCThack.dto.*;
import com.hackathon.KCThack.entity.Achievements;
import com.hackathon.KCThack.entity.User;
import com.hackathon.KCThack.entity.UserSkill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "age", expression = "java(user.getAge())")
    UserResponseDto toDto(User user);

    TeamShortResponseDto toTeamDto(Team team);

    AchievementResponseDto toAchievementDto(Achievements achievement);

    @Mapping(target = "skillId", source = "skill.id")
    @Mapping(target = "skillName", source = "skill.name")
    UserSkillResponseDto toSkillDto(UserSkill userSkill);
}