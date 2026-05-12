package com.hackathon.KCThack.TeamManagement.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateJoinRequestRequest {
    @NotBlank
    private String teamId;
    @NotBlank
    private String userId;
    private String userName;
    private String message;
}
