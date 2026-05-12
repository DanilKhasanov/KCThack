package com.hackathon.KCThack.TeamManagement.dto;

import com.hackathon.KCThack.TeamManagement.model.TeamHackathonRegistration;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class TeamRegistrationDto {
    Long id;
    String teamId;
    Long eventId;
    String eventName;
    String registeredBy;
    LocalDateTime registeredAt;

    public static TeamRegistrationDto fromEntity(TeamHackathonRegistration registration) {
        return TeamRegistrationDto.builder()
                .id(registration.getId())
                .teamId(registration.getTeam().getId())
                .eventId(registration.getEventId())
                .eventName(registration.getEventName())
                .registeredBy(registration.getRegisteredBy())
                .registeredAt(registration.getRegisteredAt())
                .build();
    }
}
