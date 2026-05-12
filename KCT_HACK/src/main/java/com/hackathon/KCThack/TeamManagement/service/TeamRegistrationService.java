package com.hackathon.KCThack.TeamManagement.service;


import com.hackathon.KCThack.TeamManagement.config.TeamProperties;
import com.hackathon.KCThack.TeamManagement.dto.RegisterTeamRequest;
import com.hackathon.KCThack.TeamManagement.model.Team;
import com.hackathon.KCThack.TeamManagement.model.TeamHackathonRegistration;
import com.hackathon.KCThack.TeamManagement.repository.TeamHackathonRegistrationRepository;
import com.hackathon.KCThack.TeamManagement.repository.TeamMemberRepository;
import com.hackathon.KCThack.entity.ScheduleEntity;
import com.hackathon.KCThack.repository.ScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeamRegistrationService {

    private final TeamService teamService;
    private final TeamHackathonRegistrationRepository registrationRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final TeamProperties teamProperties;
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public TeamHackathonRegistration registerTeam(RegisterTeamRequest request) {
        String registeredBy = Objects.requireNonNull(request.getRegisteredBy(), "registeredBy");
        ScheduleEntity schedule = scheduleRepository.findById(request.getEventId())
                .orElseThrow(() -> new EntityNotFoundException("Not found event by id: "+ request.getEventId()));
        Team team = teamService.getActiveById(request.getTeamId());

        if (!team.getCreator().getId().equals(registeredBy)) {
            throw new IllegalStateException("Только создатель команды может зарегистрировать ее на хакатон");
        }

        int members = (int) teamMemberRepository.countByTeam_Id(request.getTeamId());
        int min = teamProperties.getMinMembersForHackathonRegistration();
        if (members < min) {
            throw new IllegalStateException(
                    "Для регистрации на хакатон в команде должно быть не меньше " + min + " участников (сейчас: " + members + ")"
            );
        }

        registrationRepository.findByTeamIdAndEventId(request.getTeamId(), request.getEventId())
                .ifPresent(r -> {
                    throw new IllegalStateException(
                            "Команда уже зарегистрирована на этот хакатон (hackathonId=" + request.getEventId() + ")"
                    );
                });

        assertNoOverlappingRegistrations(request.getTeamId(), request.getEventId());

        TeamHackathonRegistration registration = new TeamHackathonRegistration();
        registration.setTeam(team);
        registration.setEventId(request.getEventId());
        registration.setEventName(request.getEventName());
        registration.setRegisteredBy(registeredBy);
        try {
            return registrationRepository.save(registration);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException(
                    "Команда уже зарегистрирована на этот хакатон (нарушение уникальности пары команда–хакатон)",
                    e
            );
        }
    }

    private void assertNoOverlappingRegistrations(String teamId, Long newHackathonId) {
        if (!teamProperties.isForbidOverlappingHackathonRegistrations()) {
            return;
        }
        Optional<ScheduleEntity> newEvent = resolveSchedule(newHackathonId);
        if (newEvent.isEmpty()) {
            return;
        }
        Instant na = newEvent.get().getStartDateTime();
        Instant nb = newEvent.get().getEndDateTime();

        for (TeamHackathonRegistration existing : registrationRepository.findByTeamId(teamId)) {
            if (existing.getEventId().equals(newHackathonId)) {
                continue;
            }
            resolveSchedule(existing.getEventId()).ifPresent(ev -> {
                Instant oa = ev.getStartDateTime();
                Instant ob = ev.getEndDateTime();
                if (na.isBefore(ob) && oa.isBefore(nb)) {
                    throw new IllegalStateException(
                            "Даты выбранного хакатона пересекаются с уже зарегистрированным событием (id=" + existing.getEventId() + ")"
                    );
                }
            });
        }
    }

    private Optional<ScheduleEntity> resolveSchedule(Long hackathonId) {
        try {
            return scheduleRepository.findById(hackathonId);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    @Transactional(readOnly = true)
    public List<TeamHackathonRegistration> getByTeam(String teamId) {
        return registrationRepository.findByTeamId(teamId);
    }

    @Transactional(readOnly = true)
    public List<TeamHackathonRegistration> getByHackathon(Long eventId) {
        return registrationRepository.findByEventId(eventId);
    }
}
