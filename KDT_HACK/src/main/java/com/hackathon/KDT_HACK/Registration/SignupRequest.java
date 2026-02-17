package com.hackathon.KDT_HACK.Registration;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignupRequest {

    @NotBlank(message = "Введите ФИО")
    @Size(min = 2, max = 100, message = "ФИО должно содержать от 2 до 100 символов")
    @Pattern(regexp = "^[\\p{L}\\s\\-']+$", message = "Только буквы, пробелы, дефис и апостроф")
    private String fullName;

    @NotBlank(message = "Введите имя пользователя")
    private String userName;

    @NotBlank(message = "Введите email")
    private String email;

    @NotBlank(message = "Введите пароль")
    @Size(min = 8, max = 100, message = "Пароль должен содержать от 8 до 100 символов")

    private String password;


}
