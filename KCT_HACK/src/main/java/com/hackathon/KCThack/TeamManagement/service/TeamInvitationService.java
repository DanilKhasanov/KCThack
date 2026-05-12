package com.hackathon.KCThack.TeamManagement.service;


import com.hackathon.KCThack.TeamManagement.dto.CreateInvitationRequest;
import com.hackathon.KCThack.TeamManagement.model.TeamInvitation;
import com.hackathon.KCThack.TeamManagement.repository.TeamInvitationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamInvitationService {

    private final TeamInvitationRepository invitationRepository;
    private final TeamService teamService;
    private final Clock clock;

    @Transactional
    public TeamInvitation create(CreateInvitationRequest request) {
        var team = teamService.getActiveById(request.getTeamId());

        if (!team.getCreator().getId().equals(request.getInvitedByUserId())) {
            throw new AccessDeniedException("Только создатель команды может отправлять инвайты");
        }
        teamService.validateUserMayJoinTeam(request.getInvitedUserId(), request.getTeamId());
        if (teamService.isMember(request.getTeamId(), request.getInvitedUserId())) {
            throw new IllegalArgumentException("Пользователь уже состоит в команде");
        }
        if (invitationRepository.existsByTeamIdAndInvitedUserIdAndStatus(
                request.getTeamId(), request.getInvitedUserId(), TeamInvitation.Status.PENDING)) {
            throw new IllegalArgumentException("Инвайт уже отправлен и ожидает ответа");
        }

        TeamInvitation invitation = new TeamInvitation();
        invitation.setTeam(team);
        invitation.setInvitedUserId(request.getInvitedUserId());
        invitation.setInvitedUserName(request.getInvitedUserName());
        invitation.setInvitedByUserId(request.getInvitedByUserId());
        invitation.setInvitedByUserName(request.getInvitedByUserName());
        invitation.setMessage(request.getMessage());
        invitation.setStatus(TeamInvitation.Status.PENDING);
        return invitationRepository.save(invitation);
    }

    @Transactional
    public TeamInvitation respond(Long invitationId, String userId, String action) {
        TeamInvitation invitation = invitationRepository.findByIdForUpdate(invitationId)
                .orElseThrow(() -> new EntityNotFoundException("Инвайт не найден: " + invitationId));

        if (!invitation.getInvitedUserId().equals(userId)) {
            throw new AccessDeniedException("Инвайт адресован другому пользователю");
        }
        if (invitation.getStatus() != TeamInvitation.Status.PENDING) {
            throw new IllegalStateException("Инвайт уже обработан");
        }

        if ("accept".equalsIgnoreCase(action)) {
            invitation.setStatus(TeamInvitation.Status.ACCEPTED);
            teamService.addMember(
                    invitation.getTeam().getId(),
                    invitation.getInvitedUserId(),
                    invitation.getInvitedUserName()
            );
        } else if ("reject".equalsIgnoreCase(action)) {
            invitation.setStatus(TeamInvitation.Status.REJECTED);
        } else {
            throw new IllegalArgumentException("Некорректное действие, используйте accept/reject");
        }
        invitation.setRespondedAt(LocalDateTime.now(clock));
        return invitationRepository.save(invitation);
    }

    @Transactional(readOnly = true)
    public List<TeamInvitation> getByUser(String userId) {
        return invitationRepository.findByInvitedUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<TeamInvitation> getPendingByUser(String userId) {
        return invitationRepository.findByInvitedUserIdAndStatus(userId, TeamInvitation.Status.PENDING);
    }

    @Transactional(readOnly = true)
    public List<TeamInvitation> getByTeam(String teamId) {
        return invitationRepository.findByTeamId(teamId);
    }

    @Transactional(readOnly = true)
    public List<TeamInvitation> getPendingByTeam(String teamId) {
        return invitationRepository.findByTeamIdAndStatus(teamId, TeamInvitation.Status.PENDING);
    }
}
