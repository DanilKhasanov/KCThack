package com.hackathon.KCThack.TeamManagement.controller;

import com.hackathon.KCThack.TeamManagement.CurrentUser;
import com.hackathon.KCThack.TeamManagement.dto.CreateTeamRequest;
import com.hackathon.KCThack.TeamManagement.dto.TeamDto;
import com.hackathon.KCThack.TeamManagement.dto.TeamMemberDto;
import com.hackathon.KCThack.TeamManagement.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public TeamDto create(@Valid @RequestBody CreateTeamRequest request, Authentication auth) {
        var principal = CurrentUser.from(auth);
        request.setCreatorId(principal.getId());
        request.setCreatorName(principal.getName() + " " + principal.getLastName());
        return TeamDto.fromEntity(teamService.createTeam(request));
    }

    @GetMapping("/{teamId}")
    public TeamDto getById(@PathVariable String teamId) {
        return TeamDto.fromEntity(teamService.getById(teamId));
    }

    @GetMapping("/user/{userId}")
    public List<TeamDto> getUserTeams(@PathVariable String userId, Authentication auth) {
        var principal = CurrentUser.from(auth);
        String authenticatedUserId = principal.getId();
        // не доверяем path-параметру, чтобы нельзя было подделывать userId
        userId = authenticatedUserId;
        return teamService.getUserTeams(userId).stream()
                .map(TeamDto::fromEntity)
                .toList();
    }

    @GetMapping("/{teamId}/members")
    public List<TeamMemberDto> getMembers(@PathVariable String teamId) {
        return teamService.getMembers(teamId);
    }

    @GetMapping("/{teamId}/is-creator/{userId}")
    public Map<String, Boolean> isCreator(@PathVariable String teamId, @PathVariable String userId, Authentication auth) {
        var principal = CurrentUser.from(auth);
        userId = principal.getId();
        return Map.of("isCreator", teamService.isCreator(userId, teamId));
    }

    @GetMapping("/{teamId}/is-member/{userId}")
    public Map<String, Boolean> isMember(@PathVariable String teamId, @PathVariable String userId, Authentication auth) {
        var principal = CurrentUser.from(auth);
        userId = principal.getId();
        return Map.of("isMember", teamService.isMember(teamId, userId));
    }

    @PostMapping("/{teamId}/leave")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void leaveTeam(@PathVariable String teamId, Authentication auth) {
        teamService.leaveTeam(teamId, CurrentUser.from(auth).getId());
    }

    @DeleteMapping("/{teamId}/members/{memberUserId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeMember(@PathVariable String teamId, @PathVariable String memberUserId, Authentication auth) {
        teamService.removeMember(teamId, memberUserId, CurrentUser.from(auth).getId());
    }

    @PostMapping("/{teamId}/transfer-ownership/{newCreatorUserId}")
    public TeamDto transferOwnership(
            @PathVariable String teamId,
            @PathVariable String newCreatorUserId,
            Authentication auth
    ) {
        teamService.transferOwnership(teamId, newCreatorUserId, CurrentUser.from(auth).getId());
        return TeamDto.fromEntity(teamService.getById(teamId));
    }
}
