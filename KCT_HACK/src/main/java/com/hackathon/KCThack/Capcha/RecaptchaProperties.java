package com.hackathon.KCThack.Capcha;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "recaptcha")
public record RecaptchaProperties(
        boolean enabled,
        String secretKey,
        String verifyUrl,
        double minScore,
        String expectedAction
) {
    public RecaptchaProperties {
        if (verifyUrl == null || verifyUrl.isBlank()) {
            verifyUrl = "https://www.google.com/recaptcha/api/siteverify";
        }
        if (minScore <= 0 || minScore > 1) {
            minScore = 0.5;
        }
    }
}
