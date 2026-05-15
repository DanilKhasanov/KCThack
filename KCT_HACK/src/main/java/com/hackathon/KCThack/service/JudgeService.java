package com.hackathon.KCThack.service;

import com.hackathon.KCThack.dto.TopParticipantDto;
import com.hackathon.KCThack.dto.TopParticipantsRequest;
import com.hackathon.KCThack.entity.EventRegistration;
import com.hackathon.KCThack.entity.ScheduleEntity;
import com.hackathon.KCThack.repository.EventRegistrationRepository;
import com.hackathon.KCThack.repository.ScheduleRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.Map;


@AllArgsConstructor
@Service
public class JudgeService {

    private final EventRegistrationRepository eventRegistrationRepository;
    private final UserPointsService userPoints;
    private final ScheduleRepository scheduleRepository;
    private final Map<Integer, Integer> FINAL_PLACE_TO_POINTS_MAP = Map.of(
            1, 10,
            2, 7,
            3, 5
    );



    @Transactional
    public TopParticipantsRequest postResults(Long eventId,
                                              TopParticipantsRequest resultDto) {
        ScheduleEntity schedule = scheduleRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (schedule.isResultsPublished()) {
            throw new IllegalStateException(
                    "Результаты уже были опубликованы"
            );
        }

        for (TopParticipantDto participant : resultDto.getTopParticipants()) {

            Long registrationId = participant.getRegistrationId();

            EventRegistration registration =
                    eventRegistrationRepository
                            .findByIdAndSchedule_Id(
                                    registrationId,
                                    eventId
                            )
                            .orElseThrow(() ->
                                    new IllegalArgumentException(
                                            "Регистрация " + registrationId +
                                                    " не принадлежит событию " + eventId
                                    )
                            );

            registration.setPlace(participant.getPlace());

            if (FINAL_PLACE_TO_POINTS_MAP.containsKey(
                    participant.getPlace()
            )) {

                int points = FINAL_PLACE_TO_POINTS_MAP.get(
                        participant.getPlace()
                );

                userPoints.addPointByRegistration(registration, points);

            }
        }
        schedule.setResultsPublished(true);
        return resultDto;
    }
}
