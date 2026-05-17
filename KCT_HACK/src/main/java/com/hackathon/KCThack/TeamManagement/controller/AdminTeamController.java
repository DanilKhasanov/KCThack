package com.hackathon.KCThack.TeamManagement.controller;

import com.hackathon.KCThack.Capcha.RecaptchaVerificationService;
import com.hackathon.KCThack.TeamManagement.dto.TeamDto;
import com.hackathon.KCThack.TeamManagement.dto.UpdateTeamRequest;
import com.hackathon.KCThack.TeamManagement.dto.UpdateTeamRequest;
import com.hackathon.KCThack.TeamManagement.service.TeamService;
import com.hackathon.KCThack.TeamManagement.service.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/teams")
@PreAuthorize("hasRole('ADMIN')")
public class AdminTeamController {

    private final TeamService teamService;
    private static final Logger log = LoggerFactory.getLogger(AdminTeamController.class);

    @GetMapping
    public Page<TeamDto> getAll(Pageable pageable) {
        return teamService.getAll(pageable).map(TeamDto::fromEntity);
    }

    @GetMapping("/{teamId}")
    public TeamDto getById(@PathVariable String teamId) {
        return TeamDto.fromEntity(teamService.getById(teamId));
    }

    @PutMapping("/{teamId}")
    public TeamDto update(@PathVariable String teamId, @RequestBody UpdateTeamRequest request) {
        return TeamDto.fromEntity(teamService.updateTeam(teamId, request));
    }

    @DeleteMapping("/{teamId}")
    public Map<String, String> delete(@PathVariable String teamId) {
        log.info("Called deleteTeam with id: " + teamId);
        teamService.deleteTeam(teamId);
        return Map.of("message", "Команда удалена");
    }

    @GetMapping("/search")
    public List<TeamDto> search(@RequestParam String query) {
        return teamService.search(query).stream()
                .map(TeamDto::fromEntity)
                .toList();
    }

    @GetMapping("/active")
    public Page<TeamDto> active(Pageable pageable) {
        return teamService.getActive(pageable).map(TeamDto::fromEntity);
    }

    @GetMapping("/user/{userId}")
    public List<TeamDto> getUserTeams(@PathVariable String userId) {
        return teamService.getUserTeams(userId).stream()
                .map(TeamDto::fromEntity)
                .toList();
    }
}
