package com.hackathon.KCThack.service;

import com.hackathon.KCThack.TeamManagement.model.TeamMember;
import com.hackathon.KCThack.TeamManagement.repository.TeamMemberRepository;
import com.hackathon.KCThack.dto.AddPointsRequest;
import com.hackathon.KCThack.entity.Achievements;
import com.hackathon.KCThack.entity.EventRegistration;
import com.hackathon.KCThack.repository.AchievementsRepository;
import com.hackathon.KCThack.entity.UserAchievements;
import com.hackathon.KCThack.repository.UserAchievementsRepository;
import com.hackathon.KCThack.entity.User;
import com.hackathon.KCThack.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserPointsService {

    private final UserRepository userRepository;
    private final AchievementsRepository achievementsRepository;
    private final UserAchievementsRepository userAchievementsRepository;
    private TeamMemberRepository teamMemberRepository;
    private static final Logger log = LoggerFactory.getLogger(UserPointsService.class);

    @Transactional
    public User addPoints(String userId, int points) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        int newPoints = user.getPoints() + points;
        user.setPoints(newPoints);

        // Ищем максимальную доступную ачивку
        Achievements bestAchievement = achievementsRepository
                .findTopByPointsRequiredLessThanEqualOrderByPointsRequiredDesc(newPoints)
                .orElse(null);

        if (bestAchievement != null) {
            user.setAchievement(bestAchievement);
        }

        return userRepository.save(user);
    }

    @Transactional
    public void addPointByRegistration(EventRegistration eventRegistration, int points){
        switch (eventRegistration.getType()){
            case SINGLE ->
                addPoints(eventRegistration.getUser().getId(), points);
            case TEAM ->{
                    List<TeamMember> teammembers = teamMemberRepository.findByTeam_Id(eventRegistration.getTeam().getId());
                    if (teammembers != null){
                        for (int i = 0; i < teammembers.size(); i++) {
                            addPoints(teammembers.get(i).getUser().getId(), points);



                        }

                    }
            }
        }

    }
}