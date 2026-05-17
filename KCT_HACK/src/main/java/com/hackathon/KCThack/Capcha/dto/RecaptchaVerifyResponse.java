package com.hackathon.KCThack.Capcha.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecaptchaVerifyResponse {

    private boolean success;
    /**
     * true если проверка пропущена из‑за recaptcha.enabled=false (только для разработки).
     */
    private boolean skipped;
    private Double score;
    private String action;
    private List<String> errorCodes;
}
