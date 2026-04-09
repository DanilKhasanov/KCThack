package com.hackathon.KDT_HACK.Response;

import java.time.LocalDateTime;

public record ErrorResponseDto(

        String message,

        String detailedMessage,

        LocalDateTime errorTime

) {

}
