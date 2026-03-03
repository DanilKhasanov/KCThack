package com.hackathon.KDT_HACK;

import java.time.LocalDateTime;

public record ErrorResponseDto(

        String message,

        String detailedMessage,

        LocalDateTime errorTime

) {

}
