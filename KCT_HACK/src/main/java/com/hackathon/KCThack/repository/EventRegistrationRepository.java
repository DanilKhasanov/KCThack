package com.hackathon.KCThack.repository;

import com.hackathon.KCThack.entity.EventRegistration;
import com.hackathon.KCThack.dto.EventRegistrationDto;
import com.hackathon.KCThack.enums.RegistrationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRegistrationRepository extends JpaRepository<EventRegistration, Long> {
    List<EventRegistration> findByScheduleId(Long scheduleId);

    Optional<EventRegistration> findByTeamIdAndScheduleId(String teamId, Long scheduleID);
    Optional<EventRegistration> findByUserIdAndScheduleId(String userId, Long scheduleId);
    List<EventRegistration> findAllByScheduleIdAndId(Long scheduleId, Long id);

    List<EventRegistration> findByUserId(String userId);
    List<EventRegistration> findByTeamId(String userId);
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM EventRegistration r WHERE r.team.id = :teamId")
    void deleteAllByTeamId(@Param("teamId") String teamId);
    Optional<EventRegistration> findByIdAndSchedule_Id(Long registrationId, Long eventId);

    boolean existsByUserIdAndScheduleId(String userId, Long scheduleId);
    boolean existsByTeamIdAndScheduleId(String userId, Long scheduleId);
    long countByScheduleIdAndType(Long scheduleId, RegistrationType type);

    @Query("""
    SELECT new com.hackathon.KCThack.dto.EventRegistrationDto(
        er.id,
        er.user.id,
        er.user.fullName,
        er.schedule.id,
        er.type,
        er.status,
        er.registeredAt,
        er.projectName,
        er.projectDescription,
        er.result,
        er.team.id,
        er.team.name
    )
    FROM EventRegistration er
    LEFT JOIN er.user
    LEFT JOIN er.team
    WHERE er.schedule.id = :scheduleId
""")
    List<EventRegistrationDto> findDtoByScheduleId(@Param("scheduleId") Long scheduleId);
}