package com.hackathon.KCThack.repository;

import com.hackathon.KCThack.entity.EventRegistration;
import com.hackathon.KCThack.dto.EventRegistrationDto;
import com.hackathon.KCThack.enums.RegistrationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRegistrationRepository extends JpaRepository<EventRegistration, Long> {
    List<EventRegistration> findByScheduleId(Long scheduleId);
    List<EventRegistration> findByUserId(String userId);
    Optional<EventRegistration> findByUserIdAndScheduleId(String userId, Long scheduleId);
    boolean existsByUserIdAndScheduleId(String userId, Long scheduleId);
    long countByScheduleIdAndType(Long scheduleId, RegistrationType type);

    @Query("""
        SELECT new com.hackathon.KDT_HACK.dto.EventRegistrationDto(
            er.id, 
            er.user.id,
            er.user.fullName, 
            er.schedule.id, 
            er.type, 
            er.status, 
            er.registeredAt
        ) 
        FROM EventRegistration er 
        WHERE er.schedule.id = :scheduleId
        
    """)
    List<EventRegistrationDto> findDtoByScheduleId(@Param("scheduleId") Long scheduleId);
}