package com.hackathon.KCThack.TeamManagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterTeamRequest {
    @NotBlank
    private String teamId;

    @NotNull
    private Long eventId;

    private String eventName;
    /** Заполняется на сервере из сессии; из тела запроса не учитывается для авторизации. */
    private String registeredBy;
}
