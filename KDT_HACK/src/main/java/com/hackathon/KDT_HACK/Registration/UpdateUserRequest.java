package com.hackathon.KDT_HACK.Registration;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UpdateUserRequest {

    @NotBlank(message = "Введите имя")
    @Size(min = 2, max = 100, message = "ФИО должно содержать от 2 до 100 символов")
    @Pattern(regexp = "^[\\p{L}\\s\\-']+$", message = "Только буквы, пробелы, дефис и апостроф")
    private String name;

    @NotBlank(message = "Введите фамилию")
    @Size(min = 2, max = 100, message = "Фамилия должна содержать от 2 до 100 символов")
    @Pattern(regexp = "^[\\p{L}\\s\\-']+$", message = "Только буквы, пробелы, дефис и апостроф")
    private String lastName;

    @NotBlank(message = "Введите имя пользователя")
    private String username;

    @NotNull(message = "Дата рождения не должна быть пустой")
    @Past(message = "Дата рождения должна быть в прошлом")
    private LocalDate birthday;

    @NotBlank(message = "Введите email")
    private String email;

    @Pattern(regexp = "^\\+79\\d{9}$", message = "Неправильный формат номера")
    private String phone;


    @Pattern(regexp = "^https://t\\.me/[a-zA-Z0-9][a-zA-Z0-9_]{4,31}$", message = "Неправильный формат телеграм аккаунта")
    private String telegram;

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
