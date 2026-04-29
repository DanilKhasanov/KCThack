package com.hackathon.KCThack.dto;

import com.hackathon.KCThack.enums.RegistrationStatus;
import com.hackathon.KCThack.enums.RegistrationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
public class EventRegistrationDto {
    Long id;
    String userId;          // только ID пользователя
    String fullName;
    Long scheduleId;        // только ID события
    RegistrationType type;
    RegistrationStatus status;
    LocalDateTime registeredAt;

}