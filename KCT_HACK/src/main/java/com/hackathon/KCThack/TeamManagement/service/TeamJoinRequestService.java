package com.hackathon.KCThack.TeamManagement.service;


import com.hackathon.KCThack.TeamManagement.dto.CreateJoinRequestRequest;
import com.hackathon.KCThack.TeamManagement.model.TeamJoinRequest;
import com.hackathon.KCThack.TeamManagement.repository.TeamJoinRequestRepository;
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
public class TeamJoinRequestService {

    private final TeamJoinRequestRepository joinRequestRepository;
    private final TeamService teamService;
    private final Clock clock;

    @Transactional
    public TeamJoinRequest create(CreateJoinRequestRequest request) {
        var team = teamService.getActiveById(request.getTeamId());
        teamService.validateUserMayJoinTeam(request.getUserId(), request.getTeamId());

        if (teamService.isMember(request.getTeamId(), request.getUserId())) {
            throw new IllegalArgumentException("Пользователь уже состоит в команде");
        }
        if (joinRequestRepository.existsByTeamIdAndUserIdAndStatus(
                request.getTeamId(), request.getUserId(), TeamJoinRequest.Status.PENDING)) {
            throw new IllegalArgumentException("У пользователя уже есть активная заявка");
        }

        TeamJoinRequest joinRequest = new TeamJoinRequest();
        joinRequest.setTeam(team);
        joinRequest.setUserId(request.getUserId());
        joinRequest.setUserName(request.getUserName());
        joinRequest.setMessage(request.getMessage());
        joinRequest.setStatus(TeamJoinRequest.Status.PENDING);
        return joinRequestRepository.save(joinRequest);
    }

    @Transactional
    public TeamJoinRequest process(Long requestId, String action, String actorUserId) {
        TeamJoinRequest joinRequest = joinRequestRepository.findByIdForUpdate(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Заявка не найдена: " + requestId));

        if (!joinRequest.getTeam().getCreator().getId().equals(actorUserId)) {
            throw new AccessDeniedException("Только создатель команды может обрабатывать заявки");
        }

        if (joinRequest.getStatus() != TeamJoinRequest.Status.PENDING) {
            throw new IllegalStateException("Заявка уже обработана");
        }

        if ("approve".equalsIgnoreCase(action)) {
            joinRequest.setStatus(TeamJoinRequest.Status.APPROVED);
            teamService.addMember(
                    joinRequest.getTeam().getId(),
                    joinRequest.getUserId(),
                    joinRequest.getUserName()
            );
        } else if ("reject".equalsIgnoreCase(action)) {
            joinRequest.setStatus(TeamJoinRequest.Status.REJECTED);
        } else {
            throw new IllegalArgumentException("Некорректное действие, используйте approve/reject");
        }
        joinRequest.setProcessedAt(LocalDateTime.now(clock));
        return joinRequestRepository.save(joinRequest);
    }

    @Transactional(readOnly = true)
    public List<TeamJoinRequest> getPendingByTeam(String teamId) {
        return joinRequestRepository.findByTeamIdAndStatus(teamId, TeamJoinRequest.Status.PENDING);
    }

    @Transactional(readOnly = true)
    public List<TeamJoinRequest> getByUser(String userId) {
        return joinRequestRepository.findByUserId(userId);
    }
}
