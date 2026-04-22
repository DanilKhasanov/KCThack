package com.hackathon.KDT_HACK.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class SignupRequest {

    @NotBlank(message = "Введите имя")
    @Size(min = 2, max = 100, message = "ФИО должно содержать от 2 до 100 символов")
    @Pattern(regexp = "^[\\p{L}\\s\\-']+$", message = "Только буквы, пробелы, дефис и апостроф")
    private String name;

    @NotBlank(message = "Введите фамилию")
    @Size(min = 2, max = 100, message = "Фамилия должна содержать от 2 до 100 символов")
    @Pattern(regexp = "^[\\p{L}\\s\\-']+$", message = "Только буквы, пробелы, дефис и апостроф")
    private String lastname;

   @NotBlank(message = "Введите имя пользователя")
    private String username;


    private LocalDate birthday;

    @NotBlank(message = "Введите email")
    private String email;

    @NotBlank(message = "Введите пароль")
    @Size(min = 8, max = 100, message = "Пароль должен содержать от 8 до 100 символов")

    private String password;


}
