package com.hackathon.KCThack.TeamManagement.dto;

import lombok.Data;

@Data
public class UpdateTeamRequest {
    private String name;
    private String description;
    private String projectName;
    private String projectDescription;
    private Boolean isActive;
}
