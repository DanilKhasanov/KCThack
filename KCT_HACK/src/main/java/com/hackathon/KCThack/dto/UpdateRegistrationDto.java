package com.hackathon.KCThack.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class UpdateRegistrationDto {

    private String projectName;

    private String projectDescription;

    @Pattern(regexp = "^https://github\\.com/[a-zA-Z0-9_-]+/?$",
            message = "Неправильный формат ссылки на GitHub")
    @Size(min = 0, max = 200, message = "Ссылка на GitHub не должна превышать 200 символов")
    private String result;
}
