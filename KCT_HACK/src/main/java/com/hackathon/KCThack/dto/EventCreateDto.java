package com.hackathon.KCThack.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class EventCreateDto {
    String name;

    @NotNull
    @Size(min = 5)
    String briefDescription;

    @NotNull
    @Size(min = 5)
    String description;

    List<Long> skillIds;

    @NotNull
    LocalDateTime startDateTime;

    @NotNull
    LocalDateTime endDateTime;
}
