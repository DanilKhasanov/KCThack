package com.hackathon.KDT_HACK.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SigninRequest {

    private String username;
    private String password;
}
