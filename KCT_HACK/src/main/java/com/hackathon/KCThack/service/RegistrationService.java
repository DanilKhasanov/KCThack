package com.hackathon.KCThack.service;

import com.hackathon.KCThack.TeamManagement.config.TeamProperties;
import com.hackathon.KCThack.TeamManagement.model.Team;
import com.hackathon.KCThack.TeamManagement.repository.TeamMemberRepository;
import com.hackathon.KCThack.TeamManagement.service.TeamService;
import com.hackathon.KCThack.dto.UpdateRegistrationDto;
import com.hackathon.KCThack.enums.RegistrationStatus;
import com.hackathon.KCThack.enums.RegistrationType;
import com.hackathon.KCThack.entity.ScheduleEntity;
import com.hackathon.KCThack.dto.EventRegistrationDto;
import com.hackathon.KCThack.entity.EventRegistration;
import com.hackathon.KCThack.entity.User;
import com.hackathon.KCThack.enums.ScheduleStatus;
import com.hackathon.KCThack.repository.EventRegistrationRepository;
import com.hackathon.KCThack.repository.ScheduleRepository;
import com.hackathon.KCThack.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final EventRegistrationRepository registrationRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final TeamService teamService;
    private final TeamMemberRepository teamMemberRepository;
    private final TeamProperties teamProperties;

    @Transactional
    public EventRegistration registerSolo(String userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow();
        ScheduleEntity schedule = scheduleRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Not found event by id: "+ eventId));

        if (registrationRepository.existsByUserIdAndScheduleId(userId, eventId)) {
            throw new IllegalStateException("User already registered for this event");
        }

        EventRegistration reg = new EventRegistration();
        reg.setUser(user);
        reg.setSchedule(schedule);
        reg.setRegisteredAt(Instant.now());
        reg.setType(RegistrationType.SINGLE);
        reg.setStatus(RegistrationStatus.ACTIVE);
        return registrationRepository.save(reg);
    }
    @Transactional
    public EventRegistration registerTeam(String userId, Long eventId) {
        userRepository.findUserById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Не найден пользователь с id: " +userId));

        ScheduleEntity schedule = scheduleRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Не найден ивент с id: "+ eventId));

        Team team = teamService.getUserTeams(userId).getFirst();
        if (team == null) {
            throw new IllegalStateException("У вас нет команды");
        }

        if (!team.getCreator().getId().equals(userId)) {
            throw new IllegalStateException("Только создатель команды может зарегистрировать ее на хакатон");
        }

        if (registrationRepository.existsByTeamIdAndScheduleId(team.getId(), eventId)) {
            throw new IllegalStateException("Команда уже зарегистрирована на ивент");
        }


        int members = (int) teamMemberRepository.countByTeam_Id(team.getId());
        int min = teamProperties.getMinMembersForHackathonRegistration();
        if (members < min) {
            throw new IllegalStateException(
                    "Для регистрации на хакатон в команде должно быть не меньше " + min + " участников (сейчас: " + members + ")"
            );
        }
        EventRegistration reg = new EventRegistration();
        reg.setTeam(team);
        reg.setSchedule(schedule);
        reg.setRegisteredAt(Instant.now());
        reg.setType(RegistrationType.TEAM);
        reg.setStatus(RegistrationStatus.ACTIVE);
        return registrationRepository.save(reg);
    }



    public List<EventRegistrationDto> getRegistrationsForEvent(Long scheduleId) {
        return registrationRepository.findDtoByScheduleId(scheduleId);
    }

    @Transactional
    public EventRegistrationDto updateRegistration(UpdateRegistrationDto updateDto, String userId, Long eventId, Long registrationId){

        userRepository.findUserById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Не найден пользователь с id: " +userId ));

        ScheduleEntity schedule = scheduleRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("не найден ивент с id: "+ eventId));

        if (schedule.getStatus() == ScheduleStatus.COMPLETED ){
            throw  new IllegalStateException("Невозможно обновить регистрацию - ивент закончен");
        }

        EventRegistration reg = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new EntityNotFoundException("Регистрация не найдена: " + registrationId));

        // Проверка прав: для соло - владелец, для команды - создатель команды
        if (reg.getType() == RegistrationType.SINGLE) {
            if (!reg.getUser().getId().equals(userId)) {
                throw new AccessDeniedException("Вы не можете редактировать чужую регистрацию");
            }
        } else if (reg.getType() == RegistrationType.TEAM) {
            if (!reg.getTeam().getCreator().getId().equals(userId)) {
                throw new AccessDeniedException("Только создатель команды может редактировать регистрацию");
            }
        }

        reg.setProjectName(updateDto.getProjectName());
        reg.setProjectDescription(updateDto.getProjectDescription());
        reg.setResult(updateDto.getResult());

        registrationRepository.save(reg);
        return EventRegistrationDto.fromEntity(reg);
    }


}