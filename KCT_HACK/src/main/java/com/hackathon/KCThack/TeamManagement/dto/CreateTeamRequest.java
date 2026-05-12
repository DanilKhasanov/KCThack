package com.hackathon.KCThack.TeamManagement.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateTeamRequest {
    @NotBlank
    private String name;
    private String description;
    private String projectName;
    private String projectDescription;
    @NotBlank
    private String creatorId;
    private String creatorName;
}
