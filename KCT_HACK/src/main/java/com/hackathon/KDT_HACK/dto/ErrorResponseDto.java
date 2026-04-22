package com.hackathon.KDT_HACK.dto;

import java.time.LocalDateTime;

public record ErrorResponseDto(

        String message,

        String detailedMessage,

        LocalDateTime errorTime

) {

}
