package com.hackathon.KCThack.dto;

import com.hackathon.KCThack.entity.EventRegistration;
import com.hackathon.KCThack.enums.RegistrationStatus;
import com.hackathon.KCThack.enums.RegistrationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class EventRegistrationDto {
    private Long id;
    private String userId;          // id пользователя (только для соло)
    private String userName;        // имя пользователя (только для соло)
    private Long scheduleId;
    private RegistrationType type;
    private RegistrationStatus status;
    private Instant registeredAt;
    private String projectName;
    private String projectDescription;
    private String result;
    private String teamId;          // id команды (только для команд)
    private String teamName;        // название команды (только для команд)

    public static EventRegistrationDto fromEntity(EventRegistration reg) {
        return new EventRegistrationDto(
                reg.getId(),
                reg.getUser() != null ? reg.getUser().getId() : null,
                reg.getUser() != null ? reg.getUser().getFullName() : null,
                reg.getSchedule().getId(),
                reg.getType(),
                reg.getStatus(),
                reg.getRegisteredAt(),
                reg.getProjectName(),
                reg.getProjectDescription(),
                reg.getResult(),
                reg.getTeam() != null ? reg.getTeam().getId() : null,
                reg.getTeam() != null ? reg.getTeam().getName() : null
        );
    }
}