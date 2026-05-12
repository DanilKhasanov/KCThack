package com.hackathon.KCThack.TeamManagement.dto;


import com.hackathon.KCThack.TeamManagement.model.TeamInvitation;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class InvitationDto {
    Long id;
    String teamId;
    String invitedUserId;
    String invitedUserName;
    String status;
    String message;

    public static InvitationDto fromEntity(TeamInvitation invitation) {
        return InvitationDto.builder()
                .id(invitation.getId())
                .teamId(invitation.getTeam().getId())
                .invitedUserId(invitation.getInvitedUserId())
                .invitedUserName(invitation.getInvitedUserName())
                .status(invitation.getStatus().name())
                .message(invitation.getMessage())
                .build();
    }
}
