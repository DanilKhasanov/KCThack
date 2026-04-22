package com.hackathon.KDT_HACK.enums;

public enum UserRole {
    USER,
    ADMIN,
    JUDGE,
    PARTNER;


    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}
