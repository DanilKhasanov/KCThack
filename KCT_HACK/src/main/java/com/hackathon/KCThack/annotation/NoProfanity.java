package com.hackathon.KCThack.annotation;

import com.hackathon.KCThack.config.NoProfanityValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NoProfanityValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoProfanity {
    String message() default "Текст содержит недопустимые выражения";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}