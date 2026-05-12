package com.hackathon.KCThack.TeamManagement;

import com.hackathon.KCThack.service.UserDetailsImpl;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;

public final class CurrentUser {

    private CurrentUser() {
    }

    public static UserDetailsImpl from(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Требуется аутентификация");
        }
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetailsImpl user)) {
            throw new AccessDeniedException("Некорректный тип principal");
        }
        return user;
    }
}
