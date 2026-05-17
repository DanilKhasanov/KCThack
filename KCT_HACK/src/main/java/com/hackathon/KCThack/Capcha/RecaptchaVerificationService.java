package com.hackathon.KCThack.Capcha;


import com.hackathon.KCThack.Capcha.dto.RecaptchaVerifyResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
@RequiredArgsConstructor
public class RecaptchaVerificationService {

    private static final Logger log = LoggerFactory.getLogger(RecaptchaVerificationService.class);

    private final RecaptchaProperties properties;
    private final RestClient recaptchaRestClient = RestClient.builder().build();

    /**
     * Проверяет токен через Google siteverify.
     */
    public RecaptchaVerifyResponse verify(String token, String remoteIp) {
        if (!properties.enabled()) {
            log.debug("reCAPTCHA отключён (recaptcha.enabled=false), проверка пропущена");
            return RecaptchaVerifyResponse.builder()
                    .success(true)
                    .skipped(true)
                    .build();
        }
        if (!StringUtils.hasText(properties.secretKey())) {
            log.warn("reCAPTCHA включён, но secret-key пуст — проверка не выполнена");
            return RecaptchaVerifyResponse.builder()
                    .success(false)
                    .skipped(false)
                    .errorCodes(java.util.List.of("missing-input-secret"))
                    .build();
        }
        if (!StringUtils.hasText(token)) {
            return RecaptchaVerifyResponse.builder()
                    .success(false)
                    .errorCodes(java.util.List.of("missing-input-response"))
                    .build();
        }

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("secret", properties.secretKey());
        form.add("response", token.trim());
        if (StringUtils.hasText(remoteIp)) {
            form.add("remoteip", remoteIp.trim());
        }

        try {
            GoogleSiteVerifyApiResponse body = recaptchaRestClient.post()
                    .uri(properties.verifyUrl())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(form)
                    .retrieve()
                    .body(GoogleSiteVerifyApiResponse.class);

            if (body == null) {
                return RecaptchaVerifyResponse.builder()
                        .success(false)
                        .errorCodes(java.util.List.of("invalid-response"))
                        .build();
            }

            boolean ok = body.isSuccess();
            if (ok && body.getScore() != null) {
                if (body.getScore() < properties.minScore()) {
                    ok = false;
                }
                if (StringUtils.hasText(properties.expectedAction())
                        && StringUtils.hasText(body.getAction())
                        && !properties.expectedAction().equals(body.getAction())) {
                    ok = false;
                }
            }

            return RecaptchaVerifyResponse.builder()
                    .success(ok)
                    .skipped(false)
                    .score(body.getScore())
                    .action(body.getAction())
                    .errorCodes(body.getErrorCodes())
                    .build();

        } catch (RestClientException e) {
            log.error("Ошибка запроса к Google reCAPTCHA: {}", e.getMessage());
            return RecaptchaVerifyResponse.builder()
                    .success(false)
                    .errorCodes(java.util.List.of("network-error"))
                    .build();
        }
    }
}
