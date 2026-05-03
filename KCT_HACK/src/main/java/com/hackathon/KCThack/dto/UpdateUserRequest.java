package com.hackathon.KCThack.dto;

import com.hackathon.KCThack.annotation.NoProfanity;
import com.hackathon.KCThack.enums.Gender;
import com.hackathon.KCThack.enums.UserJob;
import com.hackathon.KCThack.enums.UserLevel;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UpdateUserRequest {

    @NotBlank(message = "Введите имя")
    @Size(min = 2, max = 100, message = "ФИО должно содержать от 2 до 100 символов")
    @Pattern(regexp = "^[\\p{L}\\s\\-']+$", message = "Только буквы, пробелы, дефис и апостроф")
    @NoProfanity
    private String name;

    @NotBlank(message = "Введите фамилию")
    @Size(min = 2, max = 100, message = "Фамилия должна содержать от 2 до 100 символов")
    @Pattern(regexp = "^[\\p{L}\\s\\-']+$", message = "Только буквы, пробелы, дефис и апостроф")
    @NoProfanity
    private String lastName;

    @NotBlank(message = "Введите имя пользователя")
    @NoProfanity
    private String username;

    @NotNull(message = "Дата рождения не должна быть пустой")
    @Past(message = "Дата рождения должна быть в прошлом")
    private LocalDate birthday;

    private String avatar;

    @NotBlank(message = "Введите email")
    private String email;

    private String bio;


    private Gender gender;

    @Pattern(regexp = "^\\+79\\d{9}$", message = "Неправильный формат номера")
    private String phone;


    @Pattern(regexp = "^https://t\\.me/[a-zA-Z0-9][a-zA-Z0-9_]{4,31}$", message = "Неправильный формат телеграм аккаунта")
    private String telegram;

    @Pattern(regexp = "^(https?://)?(www\\.)?github\\.com/[a-zA-Z0-9-]+/?$",
            message = "Неправильный формат ссылки на GitHub")
    @Size(min = 0, max = 200, message = "Ссылка на GitHub не должна превышать 200 символов")
    private String github;

    private UserJob job;

    private UserLevel level;

    private List<SkillDto> skills;

    @Data
    public static class SkillDto {
        @NotNull
        private Long skillId;
        @NotNull
        private Integer level;
    }
}
