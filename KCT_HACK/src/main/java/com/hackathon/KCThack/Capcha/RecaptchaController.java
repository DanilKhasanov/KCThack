package com.hackathon.KCThack.Capcha;


import com.hackathon.KCThack.Capcha.dto.RecaptchaVerifyRequest;
import com.hackathon.KCThack.Capcha.dto.RecaptchaVerifyResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recaptcha")
@RequiredArgsConstructor
public class RecaptchaController {

    private final RecaptchaVerificationService recaptchaVerificationService;

    @PostMapping("/verify")
    public ResponseEntity<RecaptchaVerifyResponse> verify(@Valid @RequestBody RecaptchaVerifyRequest request) {
        RecaptchaVerifyResponse result = recaptchaVerificationService.verify(
                request.getToken(),
                request.getRemoteIp());
        return ResponseEntity.ok(result);
    }
}
