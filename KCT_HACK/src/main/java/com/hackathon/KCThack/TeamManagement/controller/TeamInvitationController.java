package com.hackathon.KCThack.TeamManagement.controller;


import com.hackathon.KCThack.TeamManagement.CurrentUser;
import com.hackathon.KCThack.TeamManagement.dto.CreateInvitationRequest;
import com.hackathon.KCThack.TeamManagement.dto.InvitationDto;
import com.hackathon.KCThack.TeamManagement.dto.RespondInvitationRequest;
import com.hackathon.KCThack.TeamManagement.service.TeamInvitationService;
import com.hackathon.KCThack.TeamManagement.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/invitations")
public class TeamInvitationController {

    private final TeamInvitationService invitationService;
    private final TeamService teamService;

    @PostMapping
    public InvitationDto create(@Valid @RequestBody CreateInvitationRequest request, Authentication auth) {
        var principal = CurrentUser.from(auth);
        request.setInvitedByUserId(principal.getId());
        request.setInvitedByUserName(principal.getName() + " " + principal.getLastName());
        return InvitationDto.fromEntity(invitationService.create(request));
    }

    @PostMapping("/respond")
    public InvitationDto respond(@Valid @RequestBody RespondInvitationRequest request, Authentication auth) {
        var principal = CurrentUser.from(auth);
        return InvitationDto.fromEntity(
                invitationService.respond(request.getInvitationId(), principal.getId(), request.getAction())
        );
    }

    @GetMapping("/user/{userId}")
    public List<InvitationDto> getUserInvitations(@PathVariable String userId, Authentication auth) {
        var principal = CurrentUser.from(auth);
        if (!principal.getId().equals(userId)) {
            throw new AccessDeniedException("Можно просматривать только свои приглашения");
        }
        return invitationService.getByUser(userId).stream()
                .map(InvitationDto::fromEntity)
                .toList();
    }

    @GetMapping("/user/{userId}/pending")
    public List<InvitationDto> getUserPendingInvitations(@PathVariable String userId, Authentication auth) {
        var principal = CurrentUser.from(auth);
        if (!principal.getId().equals(userId)) {
            throw new AccessDeniedException("Можно просматривать только свои приглашения");
        }
        return invitationService.getPendingByUser(userId).stream()
                .map(InvitationDto::fromEntity)
                .toList();
    }

    @GetMapping("/team/{teamId}")
    public List<InvitationDto> getTeamInvitations(@PathVariable String teamId, Authentication auth) {
        String authenticatedUserId = CurrentUser.from(auth).getId();
        if (!teamService.isCreator(authenticatedUserId, teamId)) {
            throw new AccessDeniedException("Только создатель команды может просматривать инвайты");
        }
        return invitationService.getByTeam(teamId).stream()
                .map(InvitationDto::fromEntity)
                .toList();
    }

    @GetMapping("/team/{teamId}/pending")
    public List<InvitationDto> getTeamPendingInvitations(@PathVariable String teamId, Authentication auth) {
        String authenticatedUserId = CurrentUser.from(auth).getId();
        if (!teamService.isCreator(authenticatedUserId, teamId)) {
            throw new AccessDeniedException("Только создатель команды может просматривать инвайты");
        }
        return invitationService.getPendingByTeam(teamId).stream()
                .map(InvitationDto::fromEntity)
                .toList();
    }
}
