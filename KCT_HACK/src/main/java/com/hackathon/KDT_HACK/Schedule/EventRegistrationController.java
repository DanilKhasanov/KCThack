package com.hackathon.KDT_HACK.Schedule;

import com.hackathon.KDT_HACK.Registration.UserDetailsImpl;
import com.hackathon.KDT_HACK.Response.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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