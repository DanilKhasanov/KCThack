package com.hackathon.KDT_HACK.Registration;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SigninRequest {

    private String username;
    private String password;
}
