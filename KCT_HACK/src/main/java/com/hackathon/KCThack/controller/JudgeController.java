package com.hackathon.KCThack.controller;

import com.hackathon.KCThack.dto.EventRegistrationDto;
import com.hackathon.KCThack.dto.TopParticipantsRequest;
import com.hackathon.KCThack.service.JudgeService;
import com.hackathon.KCThack.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/judge")
public class JudgeController {

    private final RegistrationService registrationService;
    private final JudgeService judgeService;

    @GetMapping("/{eventId}/get-registrations")
    @PreAuthorize("hasAnyRole('ADMIN','JUDGE')")
    public ResponseEntity<List<EventRegistrationDto>> getRegistrations(@PathVariable Long eventId) {
        return ResponseEntity.ok(registrationService.getRegistrationsForEvent(eventId));
    }

    @PostMapping("/{eventId}/post-results")
    public ResponseEntity<TopParticipantsRequest> postResults(@PathVariable Long eventId, @Valid @RequestBody TopParticipantsRequest resultDto){
        return ResponseEntity.ok(judgeService.postResults(eventId, resultDto));
    }
}
