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

    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "name", source = "user.name")
    @Mapping(target = "createdAt", source = "user.createdAt")
    @Mapping(target = "age", expression = "java(user.getAge())")
    @Mapping(target = "team", source = "team")
    UserResponseDto toDto(User user, Team team);

    TeamShortResponseDto toTeamDto(Team team);

    AchievementResponseDto toAchievementDto(Achievements achievement);

    @Mapping(target = "skillId", source = "skill.id")
    @Mapping(target = "skillName", source = "skill.name")
    UserSkillResponseDto toSkillDto(UserSkill userSkill);
}