package com.hackathon.KDT_HACK.Schedule;

import com.hackathon.KDT_HACK.Registration.User;
import com.hackathon.KDT_HACK.Registration.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final EventRegistrationRepository registrationRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    @Transactional
    public EventRegistration registerSolo(String userId, Long scheduleId) {
        User user = userRepository.findById(userId).orElseThrow();
        ScheduleEntity schedule = scheduleRepository.findById(scheduleId).orElseThrow();

        if (registrationRepository.existsByUserIdAndScheduleId(userId, scheduleId)) {
            throw new IllegalStateException("User already registered for this event");
        }

        EventRegistration reg = new EventRegistration();
        reg.setUser(user);
        reg.setSchedule(schedule);
        reg.setRegisteredAt(LocalDateTime.now());
        reg.setType(RegistrationType.SINGLE);
        reg.setStatus(RegistrationStatus.ACTIVE);
        return registrationRepository.save(reg);
    }



    public List<EventRegistrationDto> getRegistrationsForEvent(Long scheduleId) {
        return registrationRepository.findDtoByScheduleId(scheduleId);
    }
}