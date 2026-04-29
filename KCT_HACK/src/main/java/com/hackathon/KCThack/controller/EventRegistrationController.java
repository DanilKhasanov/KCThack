package com.hackathon.KCThack.controller;

import com.hackathon.KCThack.dto.EventRegistrationDto;
import com.hackathon.KCThack.service.RegistrationService;
import com.hackathon.KCThack.entity.EventRegistration;
import com.hackathon.KCThack.service.UserDetailsImpl;
import com.hackathon.KCThack.dto.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventRegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("/{eventId}/register-solo")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> registerSolo(@PathVariable Long eventId, Authentication auth) {

        if (!(auth.getPrincipal() instanceof UserDetailsImpl userDetails)) {
            throw new AccessDeniedException("Unauthorized");
        }

        String userId = ((UserDetailsImpl) auth.getPrincipal()).getId();
        EventRegistration reg = registrationService.registerSolo(userId, eventId);
        return ResponseEntity.ok(new ResponseDTO("Успешная регистрация"));
    }

    @GetMapping("/{eventId}/registrations")
    @PreAuthorize("hasAnyRole('ADMIN','JUDGE')")
    public ResponseEntity<List<EventRegistrationDto>> getRegistrations(@PathVariable Long eventId) {
        return ResponseEntity.ok(registrationService.getRegistrationsForEvent(eventId));
    }
}