package com.hackathon.KDT_HACK.Schedule;

import java.time.LocalDateTime;

public record EventRegistrationDto(
        Long id,
        String userId,          // только ID пользователя
        String fullName,
        Long scheduleId,        // только ID события
        RegistrationType type,
        RegistrationStatus status,
        LocalDateTime registeredAt
) {}