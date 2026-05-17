package com.hackathon.KCThack.TeamManagement.service;


import com.hackathon.KCThack.Capcha.RecaptchaVerificationService;
import com.hackathon.KCThack.TeamManagement.config.TeamProperties;
import com.hackathon.KCThack.TeamManagement.dto.CreateTeamRequest;
import com.hackathon.KCThack.TeamManagement.dto.TeamMemberDto;
import com.hackathon.KCThack.TeamManagement.dto.UpdateTeamRequest;
import com.hackathon.KCThack.TeamManagement.model.Team;
import com.hackathon.KCThack.TeamManagement.model.TeamMember;
import com.hackathon.KCThack.TeamManagement.repository.*;
import com.hackathon.KCThack.entity.User;
import com.hackathon.KCThack.repository.EventRegistrationRepository;
import com.hackathon.KCThack.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final TeamJoinRequestRepository teamJoinRequestRepository;
    private final TeamInvitationRepository teamInvitationRepository;
    private final TeamHackathonRegistrationRepository teamHackathonRegistrationRepository;
    private final UserRepository userRepository;
    private final TeamProperties teamProperties;
    private final EventRegistrationRepository eventRegistrationRepository;
    private static final Logger log = LoggerFactory.getLogger(TeamService.class);

    @Transactional
    public Team createTeam(CreateTeamRequest request) {

        User user = userRepository.findById(request.getCreatorId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Пользователь не найден"));

        assertUserNotInAnyTeam(user.getId());

        Team team = new Team();
        team.setName(request.getName().trim());
        team.setDescription(request.getDescription());
        team.setCreator(user);
        team.setIsActive(true);

        Team saved = teamRepository.save(team);



        TeamMember creator = new TeamMember();
        creator.setTeam(saved);
        creator.setUser(user);


        teamMemberRepository.save(creator);

        return saved;
    }

    @Transactional(readOnly = true)
    public Team getById(String teamId) {
        return teamRepository.findByIdWithCreator(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Команда не найдена: " + teamId));
    }

    @Transactional(readOnly = true)
    public Team getActiveById(String teamId) {
        Team team = getById(teamId);
        if (!Boolean.TRUE.equals(team.getIsActive())) {
            throw new IllegalStateException("Команда неактивна");
        }
        return team;
    }

    @Transactional(readOnly = true)
    public List<Team> getByCreator(String creatorId) {
        return teamRepository.findByCreator_Id(creatorId);
    }

    @Transactional(readOnly = true)
    public List<Team> getUserTeams(String userId) {
        return teamRepository.findTeamsByUserIdWithCreator(userId);
    }

    @Transactional(readOnly = true)
    public List<Team> getAll() {
        return teamRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<Team> getAll(Pageable pageable) {
        return teamRepository.findAllWithCreator(pageable);
    }

    @Transactional(readOnly = true)
    public List<Team> getActive() {
        return teamRepository.findByIsActiveTrue();
    }

    @Transactional(readOnly = true)
    public Page<Team> getActive(Pageable pageable) {
        return teamRepository.findByIsActiveTrue(pageable);
    }

    @Transactional(readOnly = true)
    public List<Team> search(String query) {
        return teamRepository.findByNameContainingIgnoreCase(query);
    }

    @Transactional
    public Team updateTeam(String teamId, UpdateTeamRequest request) {
        Team team = getById(teamId);
        if (request.getName() != null) {
            String newName = request.getName().trim();
            if (!newName.equalsIgnoreCase(team.getName())) {
                assertNameUnique(newName, teamId);
            }
            team.setName(newName);
        }
        if (request.getDescription() != null) {
            team.setDescription(request.getDescription());
        }
        if (request.getIsActive() != null) {
            team.setIsActive(request.getIsActive());
        }
        return teamRepository.save(team);
    }

    @Transactional
    public void deleteTeam(String teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Команда не найдена: " + teamId));

//        List<User> usersWithThisTeam = userRepository.findByCurrentTeamId(teamId); // нужно добавить метод в UserRepository
//        for (User user : usersWithThisTeam) {
//            log.info("username: {}, teamId: {}",
//                    user.getFullName(),
//                    user.getTeam().getId());
//            Hibernate.initialize(user.getTeam());
//            user.setTeam(null);
//        }
//        userRepository.saveAll(usersWithThisTeam);
//        userRepository.flush();

        teamMemberRepository.deleteAllByTeamId(teamId);

        teamJoinRequestRepository.deleteAllByTeamId(teamId);

        teamInvitationRepository.deleteAllByTeamId(teamId);

        eventRegistrationRepository.deleteAllByTeamId(teamId);

        teamRepository.delete(team);
    }

    @Transactional(readOnly = true)
    public boolean isCreator(String userId, String teamId) {
        Team team = getById(teamId);
        return team.getCreator().getId().equals(userId);
    }

    @Transactional(readOnly = true)
    public List<TeamMemberDto> getMembers(String teamId) {
        getById(teamId);
        return teamMemberRepository.findByTeam_Id(teamId).stream()
                .map(TeamMemberDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public boolean isMember(String teamId, String userId) {
        return teamMemberRepository.existsByTeam_IdAndUser_Id(teamId, userId);
    }

    /**
     * Проверка перед заявкой/инвайтом: пользователь не состоит в другой команде.
     */
    @Transactional(readOnly = true)
    public void validateUserMayJoinTeam(String userId, String teamId) {
        assertUserNotInOtherTeam(userId, teamId);
    }

    @Transactional(readOnly = true)
    public long countMembers(String teamId) {
        return teamMemberRepository.countByTeam_Id(teamId);
    }

    @Transactional
    public void addMember(String teamId, String userId, String userName) {
        Team team = getById(teamId);
        if (!Boolean.TRUE.equals(team.getIsActive())) {
            throw new IllegalStateException("Нельзя добавлять участников в неактивную команду");
        }
        if (teamMemberRepository.existsByTeam_IdAndUser_Id(teamId, userId)) {
            return;
        }
        assertUserNotInOtherTeam(userId, teamId);
        int max = teamProperties.getMaxMembers();
        if (teamMemberRepository.countByTeam_Id(teamId) >= max) {
            throw new IllegalStateException("Достигнут максимальный размер команды (" + max + " участников)");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Пользователь не найден"
                ));

        TeamMember member = new TeamMember();
        member.setTeam(team);
        member.setUser(user);


        teamMemberRepository.save(member);
    }

    @Transactional
    public void leaveTeam(String teamId, String userId) {
        Team team = getById(teamId);
        if (team.getCreator().getId().equals(userId)) {
            throw new IllegalStateException("Создатель не может покинуть команду без передачи прав другому участнику");
        }
        TeamMember member = teamMemberRepository.findByTeam_IdAndUser_Id(teamId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не состоит в этой команде"));
        teamMemberRepository.delete(member);
    }

    @Transactional
    public void removeMember(String teamId, String memberUserId, String requesterId) {
        Team team = getById(teamId);
        if (!team.getCreator().getId().equals(requesterId)) {
            throw new AccessDeniedException("Только создатель команды может исключать участников");
        }
        if (memberUserId.equals(team.getCreator().getId())) {
            throw new IllegalArgumentException("Нельзя исключить создателя команды");
        }
        if (memberUserId.equals(requesterId)) {
            throw new IllegalArgumentException("Используйте выход из команды (leave), если хотите покинуть состав");
        }
        TeamMember member = teamMemberRepository.findByTeam_IdAndUser_Id(teamId, memberUserId)
                .orElseThrow(() -> new EntityNotFoundException("Участник не найден в команде"));
        teamMemberRepository.delete(member);
    }

    @Transactional
    public void transferOwnership(String teamId, String newCreatorUserId, String currentCreatorId) {
        Team team = getActiveById(teamId);
        if (!team.getCreator().getId().equals(currentCreatorId)) {
            throw new AccessDeniedException("Только текущий создатель может передать права");
        }
        if (newCreatorUserId.equals(currentCreatorId)) {
            throw new IllegalArgumentException("Укажите другого участника как нового создателя");
        }
        TeamMember newLeader = teamMemberRepository.findByTeam_IdAndUser_Id(teamId, newCreatorUserId)
                .orElseThrow(() -> new IllegalArgumentException("Новый создатель должен уже состоять в команде"));
        TeamMember oldLeader = teamMemberRepository.findByTeam_IdAndUser_Id(teamId, currentCreatorId)
                .orElseThrow(() -> new EntityNotFoundException("Запись создателя в команде не найдена"));

        User newUser = userRepository.findById(newCreatorUserId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден: " + newCreatorUserId));


        team.setCreator(newUser);
        teamRepository.save(team);
    }

    private void assertNameUnique(String name, String excludeTeamId) {
        boolean taken = excludeTeamId == null
                ? teamRepository.existsByNameIgnoreCase(name)
                : teamRepository.existsByNameIgnoreCaseAndIdNot(name, excludeTeamId);
        if (taken) {
            throw new IllegalArgumentException("Команда с таким названием уже существует");
        }
    }

    private void assertUserNotInAnyTeam(String userId) {
        if (!teamMemberRepository.findByUser_Id(userId).isEmpty()) {
            throw new IllegalStateException("Пользователь уже состоит в команде");
        }
    }

    private void assertUserNotInOtherTeam(String userId, String teamId) {
        for (TeamMember membership : teamMemberRepository.findByUser_Id(userId)) {
            if (!membership.getTeam().getId().equals(teamId)) {
                throw new IllegalStateException("Пользователь уже состоит в другой команде");
            }
        }
    }
}
