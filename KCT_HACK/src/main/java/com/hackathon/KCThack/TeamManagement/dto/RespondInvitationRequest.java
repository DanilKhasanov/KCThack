package com.hackathon.KCThack.TeamManagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RespondInvitationRequest {
    @NotNull
    private Long invitationId;
    @NotBlank
    private String userId;
    private String action;
}
