//package com.hackathon.KCThack.TeamManagement.controller;
//
//
//import com.hackathon.KCThack.TeamManagement.CurrentUser;
//import com.hackathon.KCThack.TeamManagement.dto.RegisterTeamRequest;
//import com.hackathon.KCThack.TeamManagement.dto.TeamRegistrationDto;
//import com.hackathon.KCThack.TeamManagement.service.TeamRegistrationService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/hackathon-registrations")
//public class TeamRegistrationController {
//
//    private final TeamRegistrationService registrationService;
//
//    @PostMapping
//    public TeamRegistrationDto register(@Valid @RequestBody RegisterTeamRequest request, Authentication auth) {
//        request.setRegisteredBy(CurrentUser.from(auth).getId());
//        return TeamRegistrationDto.fromEntity(registrationService.registerTeam(request));
//    }
//
//
//    public List<TeamRegistrationDto> getByTeam(@PathVariable String teamId) {
//        return registrationService.getByTeam(teamId).stream()
//                .map(TeamRegistrationDto::fromEntity)
//                .toList();
//    }
//
//    @GetMapping("/hackathon/{hackathonId}")
//    public List<TeamRegistrationDto> getByHackathon(@PathVariable Long hackathonId) {
//        return registrationService.getByHackathon(hackathonId).stream()
//                .map(TeamRegistrationDto::fromEntity)
//                .toList();
//    }
//}
