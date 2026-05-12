package com.hackathon.KCThack.TeamManagement.dto;

import com.hackathon.KCThack.TeamManagement.model.TeamJoinRequest;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class JoinRequestDto {
    Long id;
    String teamId;
    String userId;
    String userName;
    String status;
    String message;

    public static JoinRequestDto fromEntity(TeamJoinRequest request) {
        return JoinRequestDto.builder()
                .id(request.getId())
                .teamId(request.getTeam().getId())
                .userId(request.getUserId())
                .userName(request.getUserName())
                .status(request.getStatus().name())
                .message(request.getMessage())
                .build();
    }
}
