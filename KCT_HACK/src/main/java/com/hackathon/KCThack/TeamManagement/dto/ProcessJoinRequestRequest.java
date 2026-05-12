package com.hackathon.KCThack.TeamManagement.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProcessJoinRequestRequest {
    @NotNull
    private Long requestId;
    private String action;
}
