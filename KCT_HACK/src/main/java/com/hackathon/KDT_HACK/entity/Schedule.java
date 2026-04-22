package com.hackathon.KDT_HACK.entity;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.List;

public record Schedule (

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
