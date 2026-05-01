package com.hackathon.KCThack.config;

import com.hackathon.KCThack.annotation.NoProfanity;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class NoProfanityValidator implements ConstraintValidator<NoProfanity, String> {

    private final ProfanityDictionary dictionary;

    public NoProfanityValidator(ProfanityDictionary dictionary) {
        this.dictionary = dictionary;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }
        String banned = dictionary.findBannedWord(value);
        if (banned != null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Недопустимое слово: " + banned
            ).addConstraintViolation();
            return false;
        }
        return true;
    }
}