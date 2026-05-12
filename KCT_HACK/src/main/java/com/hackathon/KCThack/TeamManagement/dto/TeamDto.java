package com.hackathon.KCThack.TeamManagement.dto;

import com.hackathon.KCThack.TeamManagement.model.Team;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class TeamDto {

    String id;
    String name;
    String description;
    String creatorId;
    String creatorName;

    Boolean isActive;

    Instant createdAt;

    public static TeamDto fromEntity(Team team) {
        return TeamDto.builder()
                .id(team.getId())
                .name(team.getName())
                .description(team.getDescription())
                .creatorId(team.getCreator().getId())
                .creatorName(team.getCreator().getFullName())
                .isActive(team.getIsActive())
                .createdAt(team.getCreatedAt())
                .build();
    }
}