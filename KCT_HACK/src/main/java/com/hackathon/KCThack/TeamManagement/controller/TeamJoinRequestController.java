package com.hackathon.KCThack.TeamManagement.controller;


import com.hackathon.KCThack.TeamManagement.CurrentUser;
import com.hackathon.KCThack.TeamManagement.dto.CreateJoinRequestRequest;
import com.hackathon.KCThack.TeamManagement.dto.JoinRequestDto;
import com.hackathon.KCThack.TeamManagement.dto.ProcessJoinRequestRequest;
import com.hackathon.KCThack.TeamManagement.service.TeamJoinRequestService;
import com.hackathon.KCThack.TeamManagement.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/join-requests")
public class TeamJoinRequestController {

    private final TeamJoinRequestService joinRequestService;
    private final TeamService teamService;

    @PostMapping
    public JoinRequestDto create(@Valid @RequestBody CreateJoinRequestRequest request, Authentication auth) {
        var principal = CurrentUser.from(auth);
        request.setUserId(principal.getId());
        request.setUserName(principal.getName() + " " + principal.getLastName());
        return JoinRequestDto.fromEntity(joinRequestService.create(request));
    }

    @PostMapping("/process")
    public JoinRequestDto process(@Valid @RequestBody ProcessJoinRequestRequest request, Authentication auth) {
        var principal = CurrentUser.from(auth);
        return JoinRequestDto.fromEntity(
                joinRequestService.process(request.getRequestId(), request.getAction(), principal.getId())
        );
    }

    @GetMapping("/team/{teamId}/pending")
    public List<JoinRequestDto> getPendingTeamRequests(@PathVariable String teamId, Authentication auth) {
        String authenticatedUserId = CurrentUser.from(auth).getId();
        if (!teamService.isCreator(authenticatedUserId, teamId)) {
            throw new AccessDeniedException("Только создатель команды может просматривать заявки");
        }
        return joinRequestService.getPendingByTeam(teamId).stream()
                .map(JoinRequestDto::fromEntity)
                .toList();
    }

    @GetMapping("/user/{userId}")
    public List<JoinRequestDto> getUserRequests(@PathVariable String userId, Authentication auth) {
        var principal = CurrentUser.from(auth);
        if (!principal.getId().equals(userId)) {
            throw new AccessDeniedException("Можно просматривать только свои заявки");
        }
        return joinRequestService.getByUser(userId).stream()
                .map(JoinRequestDto::fromEntity)
                .toList();
    }
}
