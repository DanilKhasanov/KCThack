package com.hackathon.KCThack.Capcha.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RecaptchaVerifyRequest {

    /**
     * Токен с фронтенда (g-recaptcha-response или execute() для v3).
     */
    @NotBlank
    private String token;

    /**
     * Необязательно: IP клиента для передачи в Google.
     */
    private String remoteIp;
}
