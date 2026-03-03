package com.hackathon.KDT_HACK.Registration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class EmailVerificationService {

    private final JavaMailSender mailSender;
    private final CacheManager cacheManager;



    @Value("${app.verification.expiration-minutes}")
    private int expirationMinutes;

    @Value("${spring.mail.username}")
    private String senderEmail;

    public EmailVerificationService(JavaMailSender mailSender, CacheManager cacheManager) {
        this.mailSender = mailSender;
        this.cacheManager = cacheManager;
    }

    /**
     * Генерация и отправка кода подтверждения
     */
    public void sendVerificationCode(String email) {
        // Генерация кода
        String verificationCode = generateVerificationCode();

        // Сохранение в кэш с временем истечения
        VerificationCodeData codeData = new VerificationCodeData(
                verificationCode,
                LocalDateTime.now().plusMinutes(expirationMinutes)
        );

        Cache cache = cacheManager.getCache("verificationCodes");
        if (cache != null) {
            cache.put(email, codeData);
        }

        // Отправка email
        sendEmail(email, verificationCode);
    }

    /**
     * Проверка кода подтверждения
     */
    public boolean verifyCode(String email, String code) {
        Cache cache = cacheManager.getCache("verificationCodes");
        if (cache == null) {
            return false;
        }

        VerificationCodeData codeData = cache.get(email, VerificationCodeData.class);
        if (codeData == null) {
            return false;
        }

        // Проверка срока действия
        if (codeData.getExpirationTime().isBefore(LocalDateTime.now())) {
            cache.evict(email);
            return false;
        }

        // Проверка кода
        if (codeData.getCode().equals(code)) {
            cache.evict(email); // Удаляем использованный код
            return true;
        }

        return false;
    }

    /**
     * Генерация случайного кода
     */
    private String generateVerificationCode() {
        // Используем только цифры для кода подтверждения
        SecureRandom sr = new SecureRandom();
        long randomNum = sr.nextLong(999999 - 100000 + 1) + 100000 ;

        return  String.valueOf(randomNum);

//        return RandomStringUtils.randomNumeric(codeLength);
    }

    /**
     * Отправка email с кодом
     */
    private void sendEmail(String toEmail, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(toEmail);
        message.setSubject("Код подтверждения");
        message.setText(String.format(
                """
                Здравствуйте!
                
                Ваш код подтверждения: %s
                
                Код действителен в течение %d минут.
                
                Если вы не запрашивали этот код, проигнорируйте это письмо.
                
                С уважением,
                Ваш сервис
                """,
                code, expirationMinutes
        ));

        mailSender.send(message);
    }

    /**
     * Класс для хранения данных кода с временем истечения
     */
    public static class VerificationCodeData {
        private final String code;
        private final LocalDateTime expirationTime;

        public VerificationCodeData(String code, LocalDateTime expirationTime) {
            this.code = code;
            this.expirationTime = expirationTime;
        }

        public String getCode() {
            return code;
        }

        public LocalDateTime getExpirationTime() {
            return expirationTime;
        }
    }
}
