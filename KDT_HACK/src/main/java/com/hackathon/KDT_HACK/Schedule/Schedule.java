package com.hackathon.KDT_HACK.Schedule;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

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


        //@FutureOrPresent
        @NotNull
        LocalDateTime date



){
}
