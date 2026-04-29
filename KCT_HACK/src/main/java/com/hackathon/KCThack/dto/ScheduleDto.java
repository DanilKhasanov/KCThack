package com.hackathon.KCThack.dto;

import com.hackathon.KCThack.entity.Skills;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

public record ScheduleDto(

        @Null
        Long id,

        @NotNull
        @Size(min = 5)
        String name,

        @NotNull
        @Size(min = 5)
        String briefDescription,

        @NotNull
        @Size(min = 5)
        String description,

        @NotNull
        List<Skills> skills,

        @NotNull
        LocalDateTime startDateTime,

        @NotNull
        LocalDateTime endDateTime



){
}
