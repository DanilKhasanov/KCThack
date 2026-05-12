package com.hackathon.KCThack.TeamManagement.dto;

import com.hackathon.KCThack.TeamManagement.model.TeamMember;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TeamMemberDto {

    Long id;

    String userId;

    String userName;

    boolean isCreator;

    public static TeamMemberDto fromEntity(TeamMember member) {

        boolean isCreator =
                member.getTeam().getCreator().getId()
                        .equals(member.getUser().getId());

        return TeamMemberDto.builder()
                .id(member.getId())
                .userId(member.getUser().getId())
                .userName(member.getUser().getFullName())
                .isCreator(isCreator)
                .build();
    }
}
